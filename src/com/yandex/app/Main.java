package com.yandex.app;

import com.yandex.app.service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        while (taskManager.menu()) ;

    }
}
