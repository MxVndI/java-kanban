package com.yandex.app.service;

import com.yandex.app.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

public class InMemoryTaskManager implements TaskManager {

    private int specCode = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    @Override
    public void addSubtask(SubTask task) {
        task.setId(++specCode);
        subTasks.put(task.getId(), task);
        epics.get(task.getEpicId()).addSubTask(task);
    }

    @Override
    public void addTask(Task task) {
        task.setId(++specCode);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic task) {
        task.setId(++specCode);
        epics.put(task.getId(), task);
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void removeAll(TaskType type) {
        if (type.equals(TaskType.TASK))
            tasks.clear();
        else if (type.equals(TaskType.SUBTASK)) {
            for (Epic epic : epics.values())
                epic.removeSubtasks();
            subTasks.clear();
        } else if (type.equals(TaskType.EPIC)) {
            subTasks.clear();
            epics.clear();
        }
    }

    @Override
    public Task getByCode(Integer id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (subTasks.containsKey(id)) {
            return subTasks.get(id);
        } else if (epics.containsKey(id)) {
            return epics.get(id);
        } else {
            System.out.println("Неверный идентификатор");
            return null;
        }
    }

    @Override
    public ArrayList<SubTask> getSubTasksEpic(Integer id) {
        return epics.get(id).getSubTasks();
    }

    @Override
    public void removeByCode(Integer id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            for (SubTask subTask : getSubTasksEpic(id))
                subTasks.remove(subTask.getId());
            epics.remove(id);
        } else if (subTasks.containsKey(id)) {
            SubTask subtask = subTasks.remove(id);
            epics.get(subtask.getEpicId()).removeSubtask(id);
        }
    }

    @Override
    public void refresh(Task task) {
        if (task.getType() == TaskType.EPIC) {
            Epic oldEpic = epics.get(task.getId());
            oldEpic.setName(task.getName());
            oldEpic.setDescription(task.getDescription());
        } else if (task.getType() == TaskType.TASK) {
            tasks.replace(task.getId(), task);
        } else if (task.getType() == TaskType.SUBTASK) {
            Epic epic = epics.get(((SubTask) task).getEpicId());
            epic.swapSubTask(subTasks.get(task.getId()), (SubTask) task);
            subTasks.replace(task.getId(), (SubTask) task);
        }
    }

}
