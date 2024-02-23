package com.yandex.app.service;

import com.yandex.app.model.*;


import java.util.HashMap;
import java.util.Scanner;

public class TaskManager {

    private static int specCode = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    public TaskType choseType() {
        System.out.println("Введите тип задачи: \n" +
                "TASK \n" +
                "EPIC \n" +
                "SUBTASK");
        String temp = scanner.nextLine();
        TaskType type = TaskType.valueOf(temp);
        System.out.println(type);
        return type;
    }

    private void addSubtask(SubTask task, int id) {
        subTasks.put(task.getId(), task);
        epics.get(id).addSubTask(task);
    }

    private void addSubtask(int id) {
        specCode++;
        System.out.println("Введите название задачи");
        String name = scanner.nextLine();
        System.out.println("Введите описание задачи");
        String description = scanner.nextLine();
        SubTask task = new SubTask(description, name, specCode, id);
        addSubtask(task, id);
    }

    private void addTask(Task task) {
        specCode++;
        tasks.put(task.getId(), task);
    }

    private void addEpic(Epic task) {
        specCode++;
        epics.put(task.getId(), task);
    }

    private void printTasks(TaskType type) {
        if (type == TaskType.TASK)
            for (Task task : tasks.values())
                System.out.println(task.toString());
        else if (type == TaskType.EPIC)
            for (Epic epic : epics.values())
                System.out.println(epic.toString());
        else if (type == TaskType.SUBTASK)
            for (SubTask subTask : subTasks.values())
                System.out.println(subTask.toString());
    }

    private void removeAll(TaskType type) {
        if (type.equals(TaskType.TASK))
            tasks.clear();
        else if (type.equals(TaskType.SUBTASK)) {
            for (Epic epic : epics.values())
                epic.removeSubtasks();
            subTasks.clear();
        } else if (type.equals(TaskType.EPIC)) {
            for (Epic epic : epics.values())
                epic.removeSubtasks();
            epics.clear();
        }
    }

    private Task printByCode(TaskType type) {
        System.out.println("Введите идентификатор");
        Integer code = Integer.parseInt(scanner.nextLine());
        if (type.equals(TaskType.TASK)) {
            System.out.println(tasks.get(code).toString());
            return tasks.get(code);
        } else if (type.equals(TaskType.SUBTASK)) {
            System.out.println(subTasks.get(code).toString());
            return subTasks.get(code);
        } else if (type.equals(TaskType.EPIC)) {
            System.out.println(epics.get(code).toString());
            return epics.get(code);
        } else {
            System.out.println("Неверный идентификатор");
            return null;
        }
    }

    private void removeByCode(TaskType type) {
        System.out.println("Введите идентификатор");
        Integer code = Integer.parseInt(scanner.nextLine());
        if (type == TaskType.TASK) {
            tasks.remove(code);
        } else if (type == TaskType.EPIC) {
            epics.get(code).removeSubtasks();
            epics.remove(code);
        } else if (type == TaskType.SUBTASK) {
            epics.get(subTasks.get(code).getId()).removeSubtask(code);
            subTasks.remove(code);
        } else
            System.out.println("Неверно указан тип задачи");
    }

    private void refresh(TaskType type) {
        if (type == TaskType.EPIC)
            System.out.println("Изменить тип задачи невозможно");
        else {
            System.out.println("Введите идентификатор");
            Integer code = Integer.parseInt(scanner.nextLine());
            if (type == TaskType.TASK) {
                System.out.println("Введите новый статус задачи \n" +
                        "NEW \n" +
                        "DONE \n" +
                        "IN_PROGRESS \n");
                String temp = scanner.nextLine();
                TaskStatus status = TaskStatus.valueOf(temp);
                tasks.get(code).setStatus(status);
            }
            if (type == TaskType.SUBTASK) {
                System.out.println("Введите новый статус задачи \n" +
                        "NEW \n" +
                        "DONE \n" +
                        "IN_PROGRESS \n");
                String temp = scanner.nextLine();
                TaskStatus status = TaskStatus.valueOf(temp);
                subTasks.get(code).setStatus(status);
            }
        }
    }

    private void refreshEpics() {
        for (Task task : tasks.values()) {
            if (task.getType() == TaskType.EPIC) {
                Epic epic = (Epic) task;
                epic.checkStatus();
            }
        }
    }

    public boolean menu() {
        refreshEpics();
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
                printTasks(type);
                break;
            case "2":
                removeAll(type);
                break;
            case "3":
                printByCode(type);
                break;
            case "4":
                System.out.println("Введите название задачи");
                String name = scanner.nextLine();
                System.out.println("Введите описание задачи");
                String description = scanner.nextLine();
                if (type == TaskType.TASK) {
                    Task task = new Task(description, name, specCode);
                    addTask(task);
                } else if (type == TaskType.EPIC) {
                    Epic task = new Epic(description, name, specCode);
                    addEpic(task);
                } else if (type == TaskType.SUBTASK) {
                    System.out.println("Введите id главной задачи");
                    int id = Integer.parseInt(scanner.nextLine());
                    SubTask task = new SubTask(description, name, specCode, id);
                    addSubtask(task, id);
                }
                break;
            case "5":
                refresh(type);
                break;
            case "6":
                removeByCode(type);
                break;
            case "7":
                if (type == TaskType.EPIC) {
                    System.out.println("Введите идентификатор эпика");
                    int id = Integer.parseInt(scanner.nextLine());
                    if (epics.containsKey(id)) {
                        Epic epic = (Epic) tasks.get(id);
                        epic.getSubTasks();
                    } else
                        System.out.println("Такой задачи нет");
                } else {
                    System.out.println("Неверно выбран тип");
                }
                break;
            case "8":
                if (type == TaskType.EPIC) {
                    System.out.println("Введите идентификатор эпика");
                    int id = Integer.parseInt(scanner.nextLine());
                    if (epics.containsKey(id)) {
                        addSubtask(id);
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
}
