package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    static List<Task> getHistory() {
        return null;
    }

    void remove(int id);
}
