package test;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Task;
import com.yandex.app.service.InMemoryHistoryManager;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.Managers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {
    static InMemoryTaskManager taskManager;
    static InMemoryHistoryManager historyManager;

    @BeforeAll
    public static void createManager() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
        historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
    }

    @Test
    public void testEpicEquals() {
        Epic task = new Epic("epic", "desc",LocalDateTime.now(), 10);
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
    public void testEPicFieldsEquals() {
        Epic task = new Epic("Test addNewTask", "Test addNewTask description", LocalDateTime.now(), 10);
        taskManager.addTask(task);
        assertEquals(task.getName(), taskManager.getByCode(task.getId()).getName());
        assertEquals(task.getDescription(), taskManager.getByCode(task.getId()).getDescription());
        assertEquals(task.getStatus(), taskManager.getByCode(task.getId()).getStatus());
        assertEquals(task.getType(), taskManager.getByCode(task.getId()).getType());
        assertEquals(task.getId(), taskManager.getByCode(task.getId()).getId());
    }

    //тут проблемки(
    @Test
    public void testChangesTaskInHistory() {
        Epic task = new Epic("nm", "dsc", LocalDateTime.now(), 10);
        taskManager.addEpic(task);
        historyManager.add(task);

        task.setName("bla bla");
        task.setId(2);
        assertEquals(historyManager.getHistory().get(0), task);
    }
}
