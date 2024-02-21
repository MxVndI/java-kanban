package Manager;

import Logic.*;


import java.util.HashMap;
import java.util.Scanner;

public class TaskManager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
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
        tasks.put(task.getId(), task);
        Epic epic = (Epic) tasks.get(id);
        epic.addSubTask(task);
    }

    private void addSubtask(int id) {
        System.out.println("Введите название задачи");
        String name = scanner.nextLine();
        System.out.println("Введите описание задачи");
        String description = scanner.nextLine();
        SubTask task = new SubTask(description, name, TaskType.SUBTASK, id);
        addSubtask(task, id);
    }

    private void addTask(Task task) {
        tasks.put(task.getId(), task);

    }

    private void addEpic(Epic task) {
        tasks.put(task.getId(), task);
    }

    private void printTasks(TaskType type) {
        for (Task task : tasks.values()) {
            if (task.getType() == type)
                System.out.println(task.toString());
        }
    }

    private void removeAll(TaskType type) {
        for (Integer task : tasks.keySet()) {
            if (tasks.get(task).getType() == type) {
                System.out.println(task);
                if (tasks.get(task).getType() == TaskType.EPIC) {
                    Epic epic = (Epic) tasks.get(task);
                    epic.removeSubtasks();
                }
                if (tasks.get(task).getType() == TaskType.SUBTASK) {
                    SubTask subTask = (SubTask) tasks.get(task);
                    Epic epic = (Epic) tasks.get(subTask.getEpicId());
                    epic.removeSubtask(task);
                }
                System.out.println(task);
                tasks.remove(task);
            }
        }
    }

    private void printByCode(TaskType type) {
        System.out.println("Введите идентификатор");
        Integer code = Integer.parseInt(scanner.nextLine());
        if (tasks.get(code).getType() == type)
            System.out.println(tasks.get(code));
        else
            System.out.println("Неверно указан тип задачи");
    }

    private void removeByCode(TaskType type) {
        System.out.println("Введите идентификатор");
        Integer code = Integer.parseInt(scanner.nextLine());
        if (tasks.get(code).getType() == type) {
            if (tasks.get(code).getType() == TaskType.EPIC) {
                Epic epic = (Epic) tasks.get(code);
                epic.removeSubtasks();
            }
            tasks.remove(code);
        }
            else
                System.out.println("Неверно указан тип задачи");
    }

    private void refresh(TaskType type) {
        if (type == TaskType.EPIC)
            System.out.println("Изменить тип задачи невозможно");
        else {
            System.out.println("Введите идентификатор");
            Integer code = Integer.parseInt(scanner.nextLine());
            if (tasks.get(code).getType() == TaskType.EPIC) { // если выбрали не EPIC но ввели ID эпика
                System.out.println("Не-а, нельзя");
                return;
            }
            System.out.println("Введите новый статус задачи \n" +
                    "NEW \n" +
                    "DONE \n" +
                    "IN_PROGRESS \n");
            String temp = scanner.nextLine();
            TaskStatus status = TaskStatus.valueOf(temp);
            tasks.get(code).setStatus(status);
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
                    Task task = new Task(description, name, TaskType.TASK);
                    addTask(task);
                } else if (type == TaskType.EPIC) {
                    Epic task = new Epic(description, name, TaskType.EPIC);
                    addEpic(task);
                } else if (type == TaskType.SUBTASK) {
                    System.out.println("Введите id главной задачи");
                    int id = Integer.parseInt(scanner.nextLine());
                    SubTask task = new SubTask(description, name, TaskType.SUBTASK, id);
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
                    if (tasks.containsKey(id) && tasks.get(id).getType() == TaskType.EPIC) {
                        Epic epic = (Epic) tasks.get(id);
                        epic.printSubTasks();
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
                    if (tasks.containsKey(id) && tasks.get(id).getType() == TaskType.EPIC) {
                        Epic epic = (Epic) tasks.get(id);
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
