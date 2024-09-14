package test;

import com.yandex.app.model.Task;
import com.yandex.app.service.InMemoryHistoryManager;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.Managers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {
    static InMemoryTaskManager taskManager;
    static InMemoryHistoryManager historyManager;

    @BeforeAll
    public static void createManager() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
        historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
    }

    @Test
    public void testTaskIdEquals() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", LocalDateTime.now(), 10);
        taskManager.addTask(task);
        final int taskID = task.getId();
        final Task savedTask = taskManager.getById(taskID);
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    public void testTaskFieldsEquals() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", LocalDateTime.now(), 10);
        taskManager.addTask(task);
        assertEquals(task.getName(), taskManager.getById(task.getId()).getName());
        assertEquals(task.getDescription(), taskManager.getById(task.getId()).getDescription());
        assertEquals(task.getStatus(), taskManager.getById(task.getId()).getStatus());
        assertEquals(task.getType(), taskManager.getById(task.getId()).getType());
        assertEquals(task.getId(), taskManager.getById(task.getId()).getId());
    }

    @Test
    public void testChangesTaskInHistory() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", LocalDateTime.now(), 10);
        historyManager.add(task);
        task.setName("aya");
        assertEquals(task, historyManager.getHistory().get(0));
    }

}
