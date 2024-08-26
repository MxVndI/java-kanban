package test;

import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.Managers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest {

    static InMemoryTaskManager taskManager;

    @BeforeAll
    public static void createManager() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
    }

    @Test
    public void createDifferentTasks() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", LocalDateTime.now(), 10);
        taskManager.addTask(task);
        Epic epic = new Epic("epic", "desc", LocalDateTime.now(), 10);
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("gf", "hghg", epic.getId(), LocalDateTime.now(), 10);
        taskManager.addTask(subTask);

        assertNotNull(taskManager.getTasks());
        assertNotNull(taskManager.getEpics());
        assertNotNull(taskManager.getSubTasks());
        assertNotNull(taskManager.getById(task.getId()));
        assertNotNull(taskManager.getById(epic.getId()));
        assertNotNull(taskManager.getById(subTask.getId()));
    }

}
