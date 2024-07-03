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

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest {
    File file = new File("test/data.test.csv");
    FileBackedTaskManager manager;

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTaskManager(file);
    }

    @Test
    public void shouldCorrectlyLoadDataFromFile() {
        Task task = new Task("Description", "Title");
        manager.addTask(task);
        Epic epic = new Epic("Description", "Title");
        manager.addEpic(epic);
        //manager
        FileBackedTaskManager secondManager = new FileBackedTaskManager(file);
        secondManager.loadFromFile(file);
        assertEquals(manager.getTasks(), secondManager.getTasks());
        assertEquals(manager.getTasks(), secondManager.getTasks());
    }
}