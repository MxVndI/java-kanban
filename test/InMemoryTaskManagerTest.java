package test;

import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.Managers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest {

    static InMemoryTaskManager taskManager;

    @BeforeAll
    public static void createManager() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
    }

    @Test
    public void createDifferentTasks() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.addTask(task);
        Epic epic = new Epic("epic", "desc");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("gf", "hghg", epic.getId());
        taskManager.addTask(subTask);

        assertNotNull(taskManager.getTasks());
        assertNotNull(taskManager.getEpics());
        assertNotNull(taskManager.getSubTasks());
        assertNotNull(taskManager.getByCode(task.getId()));
        assertNotNull(taskManager.getByCode(epic.getId()));
        assertNotNull(taskManager.getByCode(subTask.getId()));
    }

}
