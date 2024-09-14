package com.yandex.app.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.app.exception.ManagerSaveException;
import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTaskManager {

    private KVTaskClient client;
    private Gson gson;
    public static final String TASKS_KEY = "tasks";
    public static final String HISTORY_KEY = "tasks";
    public static final String EPICS_KEY = "epics";
    public static final String SUBTASKS_KEY = "subtasks";
    private Type historyType = new TypeToken<ArrayList<HistoryManager>>() {
    }.getType();

    public HttpTaskManager(int port) {
        super(null);
        this.client = new KVTaskClient(port);
        gson = Managers.getGson();
        loadFromServer(port);
    }

    @Override
    public void save() throws ManagerSaveException {
        String jsonTask = gson.toJson(tasks.values());
        client.save(TASKS_KEY, jsonTask);
        jsonTask = gson.toJson(epics.values());
        client.save(EPICS_KEY, jsonTask);
        jsonTask = gson.toJson(subTasks.values());
        client.save(SUBTASKS_KEY, jsonTask);
        List<Integer> historyIds = HistoryManager.getHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        String historyJson = gson.toJson(historyIds);
        client.save(HISTORY_KEY, historyJson);
    }

    private HttpTaskManager loadFromServer(int port) {
        ArrayList<Task> tasks = gson.fromJson(client.load(TASKS_KEY), new TypeToken<ArrayList<Task>>() {}.getType());
        ArrayList<Epic> epics = gson.fromJson(client.load(EPICS_KEY), new TypeToken<ArrayList<Task>>() {}.getType());
        ArrayList<SubTask> subtasks = gson.fromJson(client.load(SUBTASKS_KEY), new TypeToken<ArrayList<Task>>() {}.getType());
        ArrayList<Task> history = gson.fromJson(client.load(HISTORY_KEY), new TypeToken<ArrayList<Task>>() {}.getType());
        for (Task task: tasks)
            update(task);
        for (Epic epic: epics)
            update(epic);
        for (SubTask subTask: subtasks)
            update(subTask);
        for (Task task: history)
            historyManager.add(task);
        return this;
    }

}
