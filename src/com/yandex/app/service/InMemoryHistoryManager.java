package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final static int MAX_COUNT_ELEMENTS = 10;
    private final LinkedList<Task> history = new LinkedList<>();

    public void add(Task task) {
        if (history.size() >= MAX_COUNT_ELEMENTS)
            history.remove(0);
        history.add(task);
    }

    public List<Task> getHistory() {
        return List.copyOf(history);
    }
}
