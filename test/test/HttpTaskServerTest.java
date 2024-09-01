package test;

import com.google.gson.*;

import com.yandex.app.model.Task;
import com.yandex.app.service.HttpTaskManager;
import com.yandex.app.service.KVServer;
import com.yandex.app.service.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.LocalDateTime;

public class HttpTaskServerTest {
    URI uri;
    KVServer kvServer;
    HttpClient client;
    HttpTaskManager manager;
    Gson gson;
    Task task1;
    Task task2;


    @BeforeEach
    public void launchBefore() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = (HttpTaskManager) Managers.getDefault();
        uri = URI.create("http://localhost:8080/tasks/");
        client = HttpClient.newHttpClient();
        gson = Managers.getGson();
        task1 = new Task("name", "description",
                LocalDateTime.of(2020, 1, 1, 0, 0), 5);
        task2 = new Task("name", "description",
                LocalDateTime.of(2023, 1, 1, 0, 0), 10);
        manager.addTask(task1);
        manager.addTask(task2);

    }

    @AfterEach
    public void stop() {
        kvServer.stop();
    }

}