package test;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Task;
import com.yandex.app.service.FileBackedTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FileBackedTaskManagerTest {
    File file = new File("test/test/data.test.csv");
    FileBackedTaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTaskManager(file);
    }

    @Test
    public void shouldCorrectlyLoadDataFromFile() {
        Task task = new Task("ЧЕЛ", "Description", LocalDateTime.of(1,1,1,1,1,1), 10);
        manager.addTask(task);
        Epic epic = new Epic("desc", "Aaaa", LocalDateTime.of(1,1,1,1,1,1), 10);
        manager.addEpic(epic);
        FileBackedTaskManager secondManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(manager.getTasks().toString(), secondManager.getTasks().toString());
        assertEquals(manager.getEpics().toString(), secondManager.getEpics().toString());
    }
}
