package com.yandex.app.test;

import com.yandex.app.model.Task;
import com.yandex.app.service.InMemoryHistoryManager;
import com.yandex.app.service.Managers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {

    static InMemoryHistoryManager historyManager;

    @BeforeAll
    public static void createManager() {
        historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
    }

    @Test
    public void historyManagerCreateTest() {
        assertNotNull(historyManager);
    }

    @Test
    public void historyManagerAddTest() {
        Task task1 = new Task("fg", "fdg");
        historyManager.add(task1);
        Task task2 = new Task("task", "second");
        historyManager.add(task2);
        assertNotNull(historyManager.getHistory());
        historyManager.add(task1);
        assertEquals(historyManager.getHistory().size(), 2);
    }

    @Test
    public void historyManagerDeleteTest() {
        Task task1 = new Task("fg", "fdg");
        historyManager.add(task1);
        Task task2 = new Task("task", "second");
        historyManager.add(task2);
        assertNotNull(historyManager.getHistory());
        historyManager.add(task1);
        historyManager.remove(task1.getId());
        assertEquals(historyManager.getHistory().size(), 1);
    }
}
