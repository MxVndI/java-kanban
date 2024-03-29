package com.yandex.app.test;

import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;
import com.yandex.app.service.InMemoryHistoryManager;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.Managers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EpicTest {
    static InMemoryTaskManager taskManager;
    static InMemoryHistoryManager historyManager;

    @BeforeAll
    public static void createManager() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
        historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
    }

    @Test
    public void TestEpicEquals() {
        Epic task = new Epic("epic", "desc");
        taskManager.addEpic(task);
        final int taskID = task.getId();
        final Task savedTask = taskManager.getByCode(taskID);
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

        /*
    @Test
    public void AddEpicAsSubtask() {
        Epic task = new Epic("epic", "desc");
        taskManager.addEpic(task);
        Epic.addSubTask(task);

        просит сделать метод статическим, а после этого просит
        преобразовать task из Epic в SubTask, на уровне кода добавить нельзя
    }
    */

    @Test
    public void TestEPicFieldsEquals() {
        Epic task = new Epic("Test addNewTask", "Test addNewTask description");
        taskManager.addTask(task);
        assertEquals(task.getName(), taskManager.getByCode(task.getId()).getName());
        assertEquals(task.getDescription(), taskManager.getByCode(task.getId()).getDescription());
        assertEquals(task.getStatus(), taskManager.getByCode(task.getId()).getStatus());
        assertEquals(task.getType(), taskManager.getByCode(task.getId()).getType());
        assertEquals(task.getId(), taskManager.getByCode(task.getId()).getId());
    }

    //тут проблемки(
    @Test
    public void TestChangesTaskInHistory() {
        Epic task = new Epic("nm", "dsc");
        taskManager.addEpic(task);
        historyManager.add(task);

        task.setName("bla bla");
        task.setId(2);
        assertEquals(historyManager.getHistory().get(0), task);
    }
}
