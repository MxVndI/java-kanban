package com.yandex.app.service;

import com.yandex.app.model.*;
import java.util.HashMap;

public class TaskManager {

    private int specCode = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public void addSubtask(SubTask task, int epicId) {
        specCode++;
        task.setId(specCode);
        subTasks.put(task.getId(), task);
        epics.get(epicId).addSubTask(task);
    }

    public void addSubtask(int epicId, String name, String description) {
        SubTask task = new SubTask(description, name, epicId);
        addSubtask(task, epicId);
    }

    public void addTask(Task task) {
        specCode++;
        task.setId(specCode);
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic task) {
        specCode++;
        task.setId(specCode);
        epics.put(task.getId(), task);
    }

    public HashMap<Integer, Task> getTasks() {
        System.out.println(tasks);
        return tasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        System.out.println(epics);
        return epics;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        System.out.println(subTasks);
        return subTasks;
    }

    public void removeAll(TaskType type) {
        if (type.equals(TaskType.TASK))
            tasks.clear();
        else if (type.equals(TaskType.SUBTASK)) {
            for (Epic epic : epics.values())
                epic.removeSubtasks();
            subTasks.clear();
        } else if (type.equals(TaskType.EPIC)) {
            for (Epic epic : epics.values())
                epic.removeSubtasks();
            subTasks.clear();
            epics.clear();
        }
    }

    public Task printByCode(TaskType type, Integer code) {
        if (type.equals(TaskType.TASK)) {
            return tasks.get(code);
        } else if (type.equals(TaskType.SUBTASK)) {
            return subTasks.get(code);
        } else if (type.equals(TaskType.EPIC)) {
            return epics.get(code);
        } else {
            System.out.println("Неверный идентификатор");
            return null;
        }
    }

    public void removeByCode(TaskType type, Integer id) {
        if (type == TaskType.TASK) {
            tasks.remove(id);
        } else if (type == TaskType.EPIC) {
            epics.get(id).removeSubtasks();
            for (SubTask subTask: subTasks.values())
                if (subTask.getEpicId() == id)
                    subTasks.remove(subTask);
            epics.remove(id);
        } else if (type == TaskType.SUBTASK) {
            epics.get(subTasks.get(id).getEpicId()).removeSubtask(id);
            subTasks.remove(id);
        }
    }

    public void refresh(TaskType type, Integer code, TaskStatus newStatus) {
        if (type == TaskType.EPIC)
            System.out.println("Изменить тип задачи невозможно");
        else {

            if (type == TaskType.TASK) {
                tasks.get(code).setStatus(newStatus);
            }
            if (type == TaskType.SUBTASK) {
                subTasks.get(code).setStatus(newStatus);
            }
        }
    }
}
