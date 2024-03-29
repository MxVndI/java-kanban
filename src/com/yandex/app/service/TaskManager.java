package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;
import com.yandex.app.model.TaskType;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void addSubtask(SubTask task);

    void addTask(Task task);

    void addEpic(Epic task);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<SubTask> getSubTasks();

    void removeAll(TaskType type);

    Task getByCode(Integer id);

    List<SubTask> getSubTasksEpic(Integer id);

    void removeByCode(Integer id);

    void refresh(Task task);

    List<Task> getHistory();
}
