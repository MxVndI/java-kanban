package com.yandex.app;

import com.yandex.app.model.*;
import com.yandex.app.service.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        FileBackedTaskManager taskManager = (FileBackedTaskManager) Managers.getDefault();
        Task task1 = new Task("задача 1", "описание 1-ой задачи");
        Task task2 = new Task("задача 2", "описание 2-ой задачи");
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.getById(1);
        taskManager.getById(2);
        Task task3 = new Task("задача 1", "описание 1-ой задачи");
        Task task4 = new Task("задача 2", "описание 2-ой задачи");
        taskManager.addTask(task3);
        taskManager.addTask(task4);
        Epic epic = new Epic("name", "description");
        taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("задача 1", "описание 1-ой задачи", 5);
        SubTask subTask2 = new SubTask("задача 1", "описание 1-ой задачи", 5, LocalDateTime.of(2018, 9, 18, 10, 18), 250);
        taskManager.addSubtask(subTask1);
        taskManager.addSubtask(subTask2);
        taskManager.save();

        HttpTaskServer httpTaskServer = null;
        try {
            httpTaskServer = new HttpTaskServer((FileBackedTaskManager) taskManager);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        httpTaskServer.start();
//        TaskType type = choseType();
//        System.out.println("Выберите операцию \n" +
//                "1. Получение списка всех задач.\n" +
//                "2. Удаление всех задач.\n" +
//                "3. Получение по идентификатору.\n" +
//                "4. Создание.\n" + //Сам объект должен передаваться в качестве параметра. по условию.. :(
//                "5. Обновление.\n" + //Новая версия объекта с верным идентификатором передаётся в виде параметра.
//                "6. Удаление по идентификатору. \n" +
//                "7. Получение списка подзадач эпика. \n" +
//                "8. Добавить подзадачу в эпик \n" +
//                "9. Получить историю \n" +
//                "10. Выход из приложения");
//        String temp = scanner.nextLine();
//        switch (temp) {
//            case "1":
//                if (type == TaskType.TASK) {
//                    System.out.println(taskManager.getTasks());
//                } else if (type == TaskType.EPIC) {
//                    System.out.println(taskManager.getEpics());
//                } else if (type == TaskType.SUBTASK) {
//                    System.out.println(taskManager.getSubTasks());
//                }
//                break;
//            case "2":
//                taskManager.removeAll(type);
//                break;
//            case "3":
//                System.out.println("Введите идентификатор");
//                Integer id = Integer.parseInt(scanner.nextLine());
//                System.out.println(taskManager.getById(id));
//                break;
//            case "4":
//                System.out.println("Введите название задачи");
//                String name = scanner.nextLine();
//                System.out.println("Введите описание задачи");
//                String description = scanner.nextLine();
//                String startTime = scanner.nextLine();
//                LocalDateTime startTimeDate = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern(DATE_PATTERN));
//                String duration = scanner.nextLine();
//                if (type == TaskType.TASK) {
//                    Task task = new Task(description, name, startTimeDate, Integer.parseInt(duration));
//                    taskManager.addTask(task);
//                } else if (type == TaskType.EPIC) {
//                    Epic task = new Epic(description, name, startTimeDate, Integer.parseInt(duration));
//                    taskManager.addEpic(task);
//                } else if (type == TaskType.SUBTASK) {
//                    System.out.println("Введите id главной задачи");
//                    id = Integer.parseInt(scanner.nextLine());
//                    SubTask task = new SubTask(description, name, id, startTimeDate, Integer.parseInt(duration));
//                    taskManager.addSubtask(task);
//                }
//                break;
//            case "5":
//                TaskStatus status = null;
//                System.out.println("Введите новое название задачи");
//                name = scanner.nextLine();
//                System.out.println("Введите новое описание задачи");
//                description = scanner.nextLine();
//                System.out.println("Введите идентификатор задачи");
//                id = Integer.parseInt(scanner.nextLine());
//                if (type != TaskType.EPIC) {
//                    System.out.println("Введите новый статус задачи \n" +
//                            "NEW \n" +
//                            "DONE \n" +
//                            "IN_PROGRESS");
//                    String newStatus = scanner.nextLine();
//                    status = TaskStatus.valueOf(newStatus);
//                }
//                if (type == TaskType.TASK) {
//                    LocalDateTime startTimeDate2 = LocalDateTime.parse(DATE_PATTERN,
//                            DateTimeFormatter.ofPattern(DATE_PATTERN));
//                    String duration2 = scanner.nextLine();
//                    Task task = new Task(description, name, startTimeDate2, Integer.parseInt(duration2));
//                    task.setId(id);
//                    task.setStatus(status);
//                    taskManager.update(task);
//                } else if (type == TaskType.SUBTASK) {
//                    System.out.println("Введите идентификатор основной задачи");
//                    int epicId = Integer.parseInt(scanner.nextLine());
//                    LocalDateTime startTimeDate2 = LocalDateTime.parse(DATE_PATTERN,
//                            DateTimeFormatter.ofPattern(DATE_PATTERN));
//                    String duration2 = scanner.nextLine();
//                    SubTask task = new SubTask(description, name, epicId, startTimeDate2, Integer.parseInt(duration2));
//                    task.setId(id);
//                    task.setStatus(status);
//                    taskManager.update(task);
//                } else if (type == TaskType.EPIC) {
//                    LocalDateTime startTimeDate2 = LocalDateTime.parse(DATE_PATTERN,
//                            DateTimeFormatter.ofPattern(DATE_PATTERN));
//                    String duration2 = scanner.nextLine();
//                    Epic task = new Epic(description, name, startTimeDate2, Integer.parseInt(duration2));
//                    task.setId(id);
//                    taskManager.update(task);
//                }
//                break;
//            case "6":
//                System.out.println("Введите идентификатор");
//                id = Integer.parseInt(scanner.nextLine());
//                taskManager.remove(id);
//                break;
//            case "7":
//                if (type == TaskType.EPIC) {
//                    System.out.println("Введите идентификатор эпика");
//                    int epicId = Integer.parseInt(scanner.nextLine());
//                    if (taskManager.getEpics().contains(epicId)) {
//                        taskManager.getSubTasksEpic(epicId);
//                    } else
//                        System.out.println("Такой задачи нет");
//                } else {
//                    System.out.println("Неверно выбран тип");
//                }
//                break;
//            case "8":
//                if (type == TaskType.EPIC) {
//                    System.out.println("Введите название задачи");
//                    name = scanner.nextLine();
//                    System.out.println("Введите описание задачи");
//                    description = scanner.nextLine();
//                    System.out.println("Введите идентификатор эпика");
//                    int epicId = Integer.parseInt(scanner.nextLine());
//                    LocalDateTime startTimeDate2 = LocalDateTime.parse(DATE_PATTERN,
//                            DateTimeFormatter.ofPattern(DATE_PATTERN));
//                    String duration2 = scanner.nextLine();
//                    SubTask task = new SubTask(description, name, epicId, startTimeDate2, Integer.parseInt(duration2));
//                    taskManager.addSubtask(task);
//                } else {
//                    System.out.println("Неверно выбран тип");
//                }
//                break;
//            case "9":
//                System.out.println(taskManager.getHistory());
//                break;
//            case "10":
//                return false;
//            default:
//                System.out.println("Такой операции нет");
//        }
//        return true;
    }

    public static TaskType choseType() {
        System.out.println("Введите тип задачи: \n" +
                "TASK \n" +
                "EPIC \n" +
                "SUBTASK");
        String temp = scanner.nextLine();
        TaskType type = TaskType.valueOf(temp);
        System.out.println(type);
        return type;
    }
}
