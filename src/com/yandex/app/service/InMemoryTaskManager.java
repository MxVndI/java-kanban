package com.yandex.app.service;

import com.yandex.app.model.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private HistoryManager historyManager = Managers.getDefaultHistory();
    private int specCode = 0;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();

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
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getSubTasks() {
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
    public Task getByCode(Integer id) { // хитро))
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        } else if ((task = subTasks.get(id)) != null) {
            historyManager.add(task);
        } else if ((task = epics.get(id)) != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public List<SubTask> getSubTasksEpic(Integer id) {
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

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
