package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;
import com.yandex.app.model.TaskType;

import java.util.ArrayList;

public interface TaskManager {
    void addSubtask(SubTask task);

    void addTask(Task task);

    void addEpic(Epic task);

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<SubTask> getSubTasks();

    void removeAll(TaskType type);

    Task getByCode(Integer id);

    ArrayList<SubTask> getSubTasksEpic(Integer id);

    void removeByCode(Integer id);

    void refresh(Task task);

}
