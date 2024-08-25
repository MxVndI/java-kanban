package com.yandex.app.service;

import com.yandex.app.exception.TaskValidationExeption;
import com.yandex.app.model.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private HistoryManager historyManager = Managers.getDefaultHistory();
    private int specCode = 0;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    protected Map<LocalDateTime, Task> prioritizedTask = new TreeMap<>();

    @Override
    public void addSubtask(SubTask task) {
        task.setId(++specCode);
        subTasks.put(task.getId(), task);
        epics.get(task.getEpicId()).addSubTask(task);
        prioritizedTask.put(task.getStartTime(), task);
    }

    @Override
    public void addTask(Task task) {
        task.setId(++specCode);
        tasks.put(task.getId(), task);
        prioritizedTask.put(task.getStartTime(), task);
    }

    @Override
    public void addEpic(Epic task) {
        task.setId(++specCode);
        epics.put(task.getId(), task);
        prioritizedTask.put(task.getStartTime(), task);
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
        if (type.equals(TaskType.TASK)) {
            for (Integer taskID : tasks.keySet())
                historyManager.remove(taskID);
            tasks.clear();
        } else if (type.equals(TaskType.SUBTASK)) {
            for (Epic epic : epics.values())
                epic.removeSubtasks();
            for (Integer taskID : subTasks.keySet())
                historyManager.remove(taskID);
            subTasks.clear();
        } else if (type.equals(TaskType.EPIC)) {
            for (Integer taskID : subTasks.keySet())
                historyManager.remove(taskID);
            for (Integer taskID : epics.keySet())
                historyManager.remove(taskID);
            subTasks.clear();
            epics.clear();
        }
    }

    @Override
    public Task getByCode(Integer id) {
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
    public void remove(Integer id) {  // вот его реализация
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
        historyManager.remove(id);
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

    public List<Task> getPrioritizedTask() {
        return new ArrayList<>((Collection) prioritizedTask);
    }

    private void calculateEpicDuration(ArrayList<Integer> subTasksId) {
        if (subTasksId.isEmpty()) {
            return;
        }
        for (Integer ids : subTasksId) {
            if (subTasks.get(ids).getStartTime().equals(LocalDateTime.of(0, 1, 1, 0, 1))) {
                return;
            }
        }
        int epicDuration = 0;
        for (Integer ids : subTasksId) {
            epicDuration = epicDuration + subTasks.get(ids).getDuration();
        }
        epics.get(subTasks.get(subTasksId.get(0)).getEpicId()).setDuration(epicDuration);

    }

    public void validate(Task task) {
        if (task.getStartTime().equals(LocalDateTime.of(0, 1, 1, 0, 1))) {
            task.setStartTime(LocalDateTime.now().withNano(0));
        }
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();
        Integer result = prioritizedTask.values().stream()
                .map(it -> {
                    if (startTime.isAfter(it.getStartTime()) && endTime.isBefore(it.getEndTime())) {
                        return 1;
                    }
                    if (endTime.isAfter(it.getStartTime()) && endTime.isBefore(it.getEndTime())) {
                        return 1;
                    }
                    if (startTime.isAfter(it.getStartTime()) && startTime.isBefore(it.getEndTime())) {
                        return 1;
                    }
                    return 0;
                })
                .reduce(Integer::sum)
                .orElse(0);
        if (result > 0) {
            throw new TaskValidationExeption("Время выполнения пересекается с другой задачей!");
        }
    }

}
