package com.yandex.app.test;

import com.yandex.app.model.Task;
import com.yandex.app.service.InMemoryHistoryManager;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.Managers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TaskTest {
    static InMemoryTaskManager taskManager;
    static InMemoryHistoryManager historyManager;

    @BeforeAll
    public static void createManager() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
        historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
    }

    @Test //проверка по ID
    public void TestTaskIdEquals() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.addTask(task);
        final int taskID = task.getId();
        final Task savedTask = taskManager.getByCode(taskID);
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    public void TestTaskFieldsEquals() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.addTask(task);
        assertEquals(task.getName(), taskManager.getByCode(task.getId()).getName());
        assertEquals(task.getDescription(), taskManager.getByCode(task.getId()).getDescription());
        assertEquals(task.getStatus(), taskManager.getByCode(task.getId()).getStatus());
        assertEquals(task.getType(), taskManager.getByCode(task.getId()).getType());
        assertEquals(task.getId(), taskManager.getByCode(task.getId()).getId());
    }

    @Test
    public void TestChangesTaskInHistory() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        historyManager.add(task);

        task.setName("aya");
        assertEquals(task, historyManager.getHistory().get(0));
    }

}
