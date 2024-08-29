package test;

import com.google.gson.*;

import com.yandex.app.model.Task;
import com.yandex.app.service.HttpTaskManager;
import com.yandex.app.service.KVServer;
import com.yandex.app.service.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {
    URI uri;
    KVServer kvServer;
    HttpClient client;
    HttpTaskManager manager;
    Gson gson;
    Task task1;
    Task task2;


    @BeforeEach
    public void launchBefore() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = (HttpTaskManager) Managers.getDefault();
        uri = URI.create("http://localhost:8080/tasks/");
        client = HttpClient.newHttpClient();
        gson = Managers.getGson();
        task1 = new Task("name", "description",
                LocalDateTime.of(2020, 1, 1, 0, 0), 5);
        task2 = new Task("name", "description",
                LocalDateTime.of(2023, 1, 1, 0, 0), 10);
        manager.addTask(task1);
        manager.addTask(task2);

    }

    @AfterEach
    public void stop() {
        kvServer.stop();
    }

    @Test
    public void getTaskByIdTest() {
        int id = task1.getId();
        String jsonTaskTest = gson.toJson(manager.getById(id));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri + "task"))
                .version(HttpClient.Version.HTTP_1_1)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTaskTest))
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Неверный адрес");
        }
        Task saveTask = null;
        HttpRequest getRequestTask = HttpRequest.newBuilder()
                .uri(URI.create(uri + "task/?id=" + id))
                .version(HttpClient.Version.HTTP_1_1)
                .GET()
                .build();
        try {
            HttpResponse<String> responseTaskTest = client.send(getRequestTask, HttpResponse.BodyHandlers.ofString());

            if (responseTaskTest.statusCode() == 200) {
                String jsonTask = responseTaskTest.body();
                JsonElement jsonElement = JsonParser.parseString(jsonTask);
                if (!jsonElement.isJsonObject()) {
                    throw new RuntimeException("Получен не JsonObject.");
                }
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                saveTask = gson.fromJson(jsonObject, Task.class);

            } else {
                System.out.println("Что то пошло не так, сервер вернул код: " + responseTaskTest.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Неверный адрес!");
        }
        int taskId = saveTask.getId();
        assertEquals(id, taskId, "Задача не вернулась с сервера");
    }

    @Test
    public void shouldBeNoTask() {
        int id1 = task1.getId();
        int id2 = task2.getId();
        String jsonTaskTest1 = gson.toJson(manager.getById(id1));
        String jsonTaskTest2 = gson.toJson(manager.getById(id2));
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create(uri + "task"))
                .version(HttpClient.Version.HTTP_1_1)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTaskTest1))
                .build();
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(uri + "task"))
                .version(HttpClient.Version.HTTP_1_1)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTaskTest2))
                .build();
        try {
            client.send(request1, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Неверный адрес");
        }
        try {
            client.send(request2, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Неверный адрес");
        }
        HttpRequest requestDelete = HttpRequest.newBuilder()
                .uri(URI.create(uri + "task"))
                .version(HttpClient.Version.HTTP_1_1)
                .DELETE()
                .build();
        try {
            client.send(requestDelete, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Неверный адрес");
        }
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(URI.create(uri + "task"))
                .version(HttpClient.Version.HTTP_1_1)
                .GET()
                .build();
        try {
            HttpResponse<String> responseGetTaskAfterDelete = client.send(request3,
                    HttpResponse.BodyHandlers.ofString());
            if (responseGetTaskAfterDelete.statusCode() != 200) {
                System.out.println("Что-то пошло не так при запросе с сервера.");
                System.out.println("Сервер вернул код состояния: " + responseGetTaskAfterDelete.statusCode());
            }
            assertEquals("[]", responseGetTaskAfterDelete.body(), "задачи не удалены");
        } catch (IOException | InterruptedException e) {
            System.out.println("Неверный адрес");
        }
    }

    @Test
    public void getTask() {
        int id1 = task1.getId();
        int id2 = task2.getId();
        String jsonTask1 = gson.toJson(manager.getById(id1));
        String jsonTask2 = gson.toJson(manager.getById(id2));
        HttpRequest requestPut1 = HttpRequest.newBuilder()
                .uri(URI.create(uri + "task"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask1))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpRequest requestPut2 = HttpRequest.newBuilder()
                .uri(URI.create(uri + "task"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask2))
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        try {
            client.send(requestPut1, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Неверный адрес");
        }
        try {
            client.send(requestPut2, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Неверный адрес");
        }
        HttpRequest requestTaskById = HttpRequest.newBuilder()
                .uri(URI.create(uri + "task"))
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        try {

            HttpResponse<String> responseGetTaskId = client.send(requestTaskById,
                    HttpResponse.BodyHandlers.ofString());
            if (responseGetTaskId.statusCode() != 200) {
                System.out.println("Что-то пошло не так при запросе с сервера.");
                System.out.println("Сервер вернул код состояния: " + responseGetTaskId.statusCode());
            }
            String jsonTaskGet = responseGetTaskId.body();
            JsonElement jsonElement = JsonParser.parseString(jsonTaskGet);
            if (!jsonElement.isJsonArray()) {
                System.out.println("пришел не isJsonArray");
            }
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            List<String> list = new ArrayList<>();
            for (JsonElement element : jsonArray) {
                list.add(element.toString());
            }
            assertEquals(1, list.size(), "задача не вернулась");
        } catch (IOException | InterruptedException e) {
            System.out.println("Неверный адрес");
        }
    }

}