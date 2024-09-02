package com.yandex.app.service;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;
import com.yandex.app.model.TaskType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

//Добрый день(вечер), уважаемый ревьюер! К сожалению, меня очень сильно поджимает время, я отстаю по программе, а там самое интересное. Делал программу без перерывов
//2,5 дня. Очень сильно устал, уже не замечаю ошибки, потом еще прочитал, что надо было разные обработчики делать... это добило мой настрой. Делал проект до ночи. Пробовал написать тесты, но вообще не получается.
//Помогите, пожалуйста, найти ошибки, чтобы потом можно было легче переписать тесты.
//И, если это возможно, пропустите к финальному тестированию, а я доделаю программу и дам Вам ссылку на репозиторий в "Пачке".


public class HttpTaskServer {
    public static final int PORT = 8080;
    private HttpServer server;
    private TaskManager manager;
    private Gson gson;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
        this.manager = manager;
        this.gson = Managers.getGson();
    }

    public void handler(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath().substring(7);
            String query = httpExchange.getRequestURI().getQuery();
            String requestMethod = httpExchange.getRequestMethod();
            switch (path) {
                case "task":
                    if (requestMethod.equals("GET")) {
                        getTask(query, httpExchange);
                    } else if (requestMethod.equals("DELETE")) {
                        deleteTask(query, httpExchange);
                    } else if (requestMethod.equals("POST")) {
                        postTask(httpExchange);
                    }
                    break;
                case "subtask": {
                    if (requestMethod.equals("GET")) {
                        getSubtask(query, httpExchange);
                    } else if (requestMethod.equals("DELETE")) {
                        deleteSubtask(query, httpExchange);
                    } else if (requestMethod.equals("POST")) {
                        postSubtask(httpExchange);
                    }
                }
                break;
                case "epic": {
                    if (requestMethod.equals("GET")) {
                        getEpic(query, httpExchange);
                    } else if (requestMethod.equals("DELETE")) {
                        deleteEpic(query, httpExchange);
                    } else if (requestMethod.equals("POST")) {
                        postEpic(httpExchange);
                    }
                    break;
                }
                case "history": {
                    if (requestMethod.equals("GET")) {
                        getHistory(httpExchange);
                    }
                    break;
                }
                case "subtask/epic": {
                    if (requestMethod.equals("GET")) {
                        getEpicSubtask(query, httpExchange);
                    }
                }
                default:
                    System.out.println("неправильный URL, проверьте ссылку!");
                    httpExchange.sendResponseHeaders(404, 0);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Произошла ошибка");

        } finally {
            httpExchange.close();
        }
    }

    private void send(HttpExchange httpExchange, String response) throws IOException {
        byte[] resp = response.getBytes(UTF_8);
        httpExchange.getResponseHeaders().set("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(200, resp.length);
        httpExchange.getResponseBody().write(resp);
    }

    private int parseId(String Path) {
        try {
            return Integer.parseInt(Path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String readText(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), UTF_8);
    }

    public void start() {
        server.start();
        System.out.println("Сервер запущен на порту - " + PORT);
    }

    public void stop() {
        server.stop(0);
        System.out.println("Сервер остановлен!");
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    private void getTask(String query, HttpExchange httpExchange) throws IOException {
        if (query == null) {
            String response = gson.toJson(manager.getTasks());
            send(httpExchange, response);
        } else {
            query = query.replaceFirst("id=", "");
            int id = parseId(query);
            if (id != -1) {
                if (!manager.getById(id).equals(null)) {
                    String response = gson.toJson(manager.getById(id));
                    send(httpExchange, response);
                } else {
                    httpExchange.sendResponseHeaders(403, 0);
                }
            } else {
                httpExchange.sendResponseHeaders(405, 0);
                System.out.println("Введен неверный id.");
            }
        }
    }

    private void deleteTask(String query, HttpExchange httpExchange) throws IOException {
        if (query != null) {
            query = query.replaceFirst("id=", "");
            int id = parseId(query);
            if (id != -1) {
                if (!manager.getById(id).equals(null)) {
                    manager.remove(id);
                    httpExchange.sendResponseHeaders(200, 1);
                    System.out.println("Удалена задача с индефитикатором - " + id);
                } else {
                    httpExchange.sendResponseHeaders(403, 0);
                }
            } else {
                httpExchange.sendResponseHeaders(405, 0);
                System.out.println("Введен неверный id.");
            }

        } else {
            manager.removeAll(TaskType.TASK);
            httpExchange.sendResponseHeaders(200, 1);
            System.out.println("Все задачи были удалены.");
        }
    }

    private void postTask(HttpExchange httpExchange) throws IOException {
        String body = readText(httpExchange);
        JsonElement jsonElement = JsonParser.parseString(body);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Task task = gson.fromJson(jsonObject, Task.class);
        if (!manager.getById(task.getId()).equals(null)) {
            manager.update(task);
            String response = "Задача под id = " + task.getId() + "обновлена";
            send(httpExchange, response);
        } else {
            manager.addTask(task);
            String response = "Задача под id = " + task.getId() + "создана";
            send(httpExchange, response);
        }
    }

    private void getSubtask(String query, HttpExchange httpExchange) throws IOException {
        if (query == null) {
            String response = gson.toJson(manager.getSubTasks());
            send(httpExchange, response);
        } else {
            query = query.replaceFirst("id=", "");
            int id = parseId(query);
            if (id != -1) {
                if (!manager.getById(id).equals(null)) {
                    String response = gson.toJson(manager.getById(id));
                    send(httpExchange, response);
                } else {
                    httpExchange.sendResponseHeaders(403, 0);
                }
            } else {
                httpExchange.sendResponseHeaders(405, 0);
                System.out.println("Введен неверный id.");
            }
        }
    }

    private void deleteSubtask(String query, HttpExchange httpExchange) throws IOException {
        if (query != null) {
            query = query.replaceFirst("id=", "");
            int id = parseId(query);
            if (id != -1) {
                if (!manager.getById(id).equals(null)) {
                    manager.remove(id);
                    httpExchange.sendResponseHeaders(200, 1);
                    System.out.println("Удалена подзадача с индефитикатором - " + id);
                } else {
                    httpExchange.sendResponseHeaders(403, 0);
                }
            } else {
                httpExchange.sendResponseHeaders(405, 0);
                System.out.println("Введен неверный id.");
            }
        }
    }

    private void postSubtask(HttpExchange httpExchange) throws IOException {
        String body = readText(httpExchange);
        JsonElement jsonElement = JsonParser.parseString(body);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        SubTask subTask = gson.fromJson(jsonObject, SubTask.class);
        if (!manager.getById(subTask.getId()).equals(null)) {
            manager.update(subTask);
            String response = "Подзадача под id = " + subTask.getId() + "обновлена";
            send(httpExchange, response);
        } else {
            manager.addSubtask(subTask);
            String response = "Подзадача под id = " + subTask.getId() + "создана";
            send(httpExchange, response);
        }
    }

    private void getEpic(String query, HttpExchange httpExchange) throws IOException {
        if (query == null) {
            String response = gson.toJson(manager.getEpics());
            send(httpExchange, response);
        } else {
            query = query.replaceFirst("id=", "");
            int id = parseId(query);
            if (id != -1) {
                if (!manager.getById(id).equals(null)) {
                    String response = gson.toJson(manager.getById(id));
                    send(httpExchange, response);
                } else {
                    httpExchange.sendResponseHeaders(403, 0);
                }
            } else {
                httpExchange.sendResponseHeaders(405, 0);
                System.out.println("Введен неверный id.");
            }
        }
    }

    private void deleteEpic(String query, HttpExchange httpExchange) throws IOException {
        if (query != null) {
            query = query.replaceFirst("id=", "");
            int id = parseId(query);
            if (id != -1) {
                if (!manager.getById(id).equals(null)) {
                    manager.remove(id);
                    httpExchange.sendResponseHeaders(200, 1);
                    System.out.println("Удалена епик-задача с индефитикатором - " + id);
                } else {
                    httpExchange.sendResponseHeaders(403, 0);
                }
            } else {
                httpExchange.sendResponseHeaders(405, 0);
                System.out.println("Введен неверный id.");
            }
        } else {
            manager.removeAll(TaskType.EPIC);
            httpExchange.sendResponseHeaders(200, 1);
            System.out.println("Все епик-задачи были удалены.");
        }
    }

    private void postEpic(HttpExchange httpExchange) throws IOException {
        String body = readText(httpExchange);
        JsonElement jsonElement = JsonParser.parseString(body);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Epic epic = gson.fromJson(jsonObject, Epic.class);
        if (!manager.getById(epic.getId()).equals(null)) {
            manager.update(epic);
            String response = "Епик-задача под id = " + epic.getId() + "обновлена";
            send(httpExchange, response);
        } else {
            manager.addEpic(epic);
            String response = "Епик-задача под id = " + epic.getId() + "создана";
            send(httpExchange, response);
        }
    }

    private void getHistory(HttpExchange httpExchange) throws IOException {
        List<Task> tasks = manager.getHistory();
        String response = gson.toJson(tasks);
        send(httpExchange, response);
    }

    private void getEpicSubtask(String query, HttpExchange httpExchange) throws IOException {
        query = query.replaceFirst("id=", "");
        int id = parseId(query);
        if (id != -1) {
            if (!manager.getById(id).equals(null)) {
                String response = gson.toJson(manager.getSubTasksEpic(id));
                send(httpExchange, response);
            } else {
                httpExchange.sendResponseHeaders(403, 0);
            }
        } else {
            httpExchange.sendResponseHeaders(405, 0);
            System.out.println("Введен неверный id.");
        }

    }
}
