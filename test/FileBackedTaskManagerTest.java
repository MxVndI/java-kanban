package test;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Task;
import com.yandex.app.service.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest {
    public static final Path path = Path.of("data.test.csv");
    File file = new File(String.valueOf(path));
    FileBackedTaskManager manager;
    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTaskManager(file);
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(path);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    public void shouldCorrectlyLoadDataFromFile() {
        Task task = new Task("Description", "Title");
        manager.addTask(task);
        Epic epic = new Epic("Description", "Title");
        manager.addEpic(epic);
        FileBackedTaskManager secondManager = new FileBackedTaskManager(file);
        secondManager.loadFromFile(file);
        assertEquals(manager.getTasks(), secondManager.getTasks());
        assertEquals(manager.getTasks(), secondManager.getTasks());
    }
}