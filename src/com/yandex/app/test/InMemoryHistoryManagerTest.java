package com.yandex.app.test;

import com.yandex.app.model.Task;
import com.yandex.app.service.InMemoryHistoryManager;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.Managers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
        Task task = new Task("fg", "fdg");
        assertNotNull(historyManager.getHistory());
    }
}
