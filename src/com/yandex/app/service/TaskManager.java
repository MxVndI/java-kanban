package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;
import com.yandex.app.model.TaskType;

import java.util.List;

public interface TaskManager {
    void addSubtask(SubTask task);

    void addTask(Task task);

    void addEpic(Epic task);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<SubTask> getSubTasks();

    void removeAll(TaskType type);

    Task getById(Integer id);

    List<SubTask> getSubTasksEpic(Integer id);

    void remove(Integer id);

    void update(Task task);

    List<Task> getHistory();

    List<Task> getPrioritizedTask();

    void save();
}
