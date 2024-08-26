package test;

import com.yandex.app.model.Epic;
import com.yandex.app.model.SubTask;
import com.yandex.app.model.Task;
import com.yandex.app.service.InMemoryHistoryManager;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.Managers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubTaskTest {
    static InMemoryTaskManager taskManager;
    static InMemoryHistoryManager historyManager;

    @BeforeAll
    public static void createManager() {
        taskManager = (InMemoryTaskManager) Managers.getDefault();
        historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
    }

    @Test
    public void testSubTaskEquals() {
        Epic epic = new Epic("epic", "AAAA", LocalDateTime.now(), 10);
        System.out.println(epic.toString());
        taskManager.addEpic(epic);
        SubTask task = new SubTask("Test addNewTask", "Test addNewTask description", epic.getId(), LocalDateTime.now(), 10);
        taskManager.addSubtask(task);
        final int taskID = task.getId();
        final Task savedTask = taskManager.getById(taskID);
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }
 /*
   @Test
    public void MakeSubTaskEpic() {
        Epic task = new Epic("epic", "desc");
        taskManager.addEpic(task);
        SubTask subTask = new SubTask("Name", "Desc", subTask.getId());
        SubTask subTask1 = new SubTask("Namegfg", "Descgfd", subTask.getId());
    }

    для создания подзадачи требуется ввести ID эпика, так что изначально нельзя задать себя как эпик.
    Также нет метода для изменения изначально заданного эпика, поэтому после поменять тоже не получится

    Можно создать эпик, подзадачу к нему и создать еще одну подзадачу, у которой в качестве эпика указать прошлую
    подзадачу (38 строка), это сработает если обращаться
    напрямую к SubTask, но у нас взаимодействие через менеджеры, поэтому со стороны пользователя это невозможно
    */

    @Test
    public void testSubTaskFieldsEquals() {
        Epic epic = new Epic("fds", "f", LocalDateTime.now(), 10);
        SubTask task = new SubTask("Test addNewTask", "Test addNewTask description", epic.getId(), LocalDateTime.now(), 10);
        taskManager.addTask(task);
        assertEquals(task.getName(), taskManager.getById(task.getId()).getName());
        assertEquals(task.getDescription(), taskManager.getById(task.getId()).getDescription());
        assertEquals(task.getStatus(), taskManager.getById(task.getId()).getStatus());
        assertEquals(task.getType(), taskManager.getById(task.getId()).getType());
        assertEquals(task.getId(), taskManager.getById(task.getId()).getId());
    }

    @Test
    public void testChangesTaskInHistory() {
        Epic epic = new Epic("nm", "dsc", LocalDateTime.now(), 10);
        taskManager.addEpic(epic);
        SubTask task = new SubTask("Test addNewTask", "Test addNewTask description", epic.getId(), LocalDateTime.now(), 10);
        historyManager.add(task);

        task.setName("bla bla");
        task.setId(2);
        assertEquals(historyManager.getHistory().get(0), task);
    }
}
