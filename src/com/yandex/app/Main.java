package com.yandex.app;

import com.yandex.app.model.*;
import com.yandex.app.service.TaskManager;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        while (menu()) ;
    }

    private static boolean menu() {
        TaskType type = choseType();
        System.out.println("Выберите операцию \n" +
                "1. Получение списка всех задач.\n" +
                "2. Удаление всех задач.\n" +
                "3. Получение по идентификатору.\n" +
                "4. Создание.\n" + //Сам объект должен передаваться в качестве параметра. по условию.. :(
                "5. Обновление.\n" + //Новая версия объекта с верным идентификатором передаётся в виде параметра.
                "6. Удаление по идентификатору. \n" +
                "7. Получение списка подзадач эпика. \n" +
                "8. Добавить подзадачу в эпик \n" +
                "9. Выход из приложения");
        String temp = scanner.nextLine();
        switch (temp) {
            case "1":
                if (type == TaskType.TASK) {
                    taskManager.getTasks();
                } else if (type == TaskType.EPIC) {
                    taskManager.getEpics();
                } else if (type == TaskType.SUBTASK) {
                    taskManager.getSubTasks();
                }

                break;
            case "2":
                taskManager.removeAll(type);
                break;
            case "3":
                System.out.println("Введите идентификатор");
                Integer code = Integer.parseInt(scanner.nextLine());
                taskManager.printByCode(type, code);
                break;
            case "4":
                System.out.println("Введите название задачи");
                String name = scanner.nextLine();
                System.out.println("Введите описание задачи");
                String description = scanner.nextLine();
                if (type == TaskType.TASK) {
                    Task task = new Task(description, name);
                    taskManager.addTask(task);
                } else if (type == TaskType.EPIC) {
                    Epic task = new Epic(description, name);
                    taskManager.addEpic(task);
                } else if (type == TaskType.SUBTASK) {
                    System.out.println("Введите id главной задачи");
                    int id = Integer.parseInt(scanner.nextLine());
                    SubTask task = new SubTask(description, name, id);
                    taskManager.addSubtask(task);
                }
                break;
            case "5":
                System.out.println("Введите идентификатор");
                code = Integer.parseInt(scanner.nextLine());
                System.out.println("Введите новый статус задачи \n" +
                        "NEW \n" +
                        "DONE \n" +
                        "IN_PROGRESS \n");
                String newStatus = scanner.nextLine();
                TaskStatus status = TaskStatus.valueOf(newStatus);
                taskManager.refresh(type, code, status);
                break;
            case "6":
                System.out.println("Введите идентификатор");
                code = Integer.parseInt(scanner.nextLine());
                taskManager.removeByCode(type, code);
                break;
            case "7":
                if (type == TaskType.EPIC) {
                    System.out.println("Введите идентификатор эпика");
                    int epicId = Integer.parseInt(scanner.nextLine());
                    if (taskManager.getEpics().containsKey(epicId)) {
                        taskManager.getEpics().get(epicId).getSubTasks();
                    } else
                        System.out.println("Такой задачи нет");
                } else {
                    System.out.println("Неверно выбран тип");
                }
                break;
            case "8":
                if (type == TaskType.EPIC) {
                    System.out.println("Введите название задачи");
                    name = scanner.nextLine();
                    System.out.println("Введите описание задачи");
                    description = scanner.nextLine();
                    System.out.println("Введите идентификатор эпика");
                    int epicId = Integer.parseInt(scanner.nextLine());
                    if (taskManager.getEpics().containsKey(epicId)) {

                        taskManager.addSubtask(epicId, name, description);
                    } else
                        System.out.println("Такой задачи нет");
                } else {
                    System.out.println("Неверно выбран тип");
                }
                break;
            case "9":
                return false;
            default:
                System.out.println("Такой операции нет");
        }
        return true;
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
