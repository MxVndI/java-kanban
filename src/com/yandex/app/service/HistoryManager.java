package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();
}
