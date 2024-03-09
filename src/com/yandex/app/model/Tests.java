package com.yandex.app.model;

import com.yandex.app.service.InMemoryHistoryManager;
import com.yandex.app.service.InMemoryTaskManager;
import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Tests {

    static Managers managers = new Managers();
    static InMemoryTaskManager taskManager;
    static InMemoryHistoryManager historyManager;

    @BeforeAll
    public static void createManager() {
        taskManager = (InMemoryTaskManager) managers.getDefault();
        historyManager = (InMemoryHistoryManager) managers.getDefaultHistory();
    }

    @Test
    public void TestTaskEquals() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.addTask(task);
        final int taskID = task.getId();
        final Task savedTask = taskManager.getByCode(taskID);
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    public void TestSubTaskEquals() {
        Epic epic = new Epic("epic", "desc");
        taskManager.addEpic(epic);
        SubTask task = new SubTask("Test addNewTask", "Test addNewTask description", epic.getId());
        taskManager.addSubtask(task);
        final int taskID = task.getId();
        final Task savedTask = taskManager.getByCode(taskID);
        assertEquals(task, savedTask, "Задачи не совпадают.");
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
    подзадачу (70 строка), это сработает если обращаться
    напрямую к SubTask, но у нас взаимодействие через менеджеры, поэтому со стороны пользователя это невозможно
    */

    @Test
    public void TestManagers() {
        Managers manag = new Managers();
        assertNotNull(manag.getDefault());
        assertNotNull(manag.getDefault());
    }

    @Test
    public void CreateDifferentTasks() {
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

    @Test
    public void TestFields() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.addTask(task);
        assertEquals(task.getName(), taskManager.getByCode(task.getId()).getName());
        assertEquals(task.getDescription(), taskManager.getByCode(task.getId()).getDescription());
        assertEquals(task.getStatus(), taskManager.getByCode(task.getId()).getStatus());
        assertEquals(task.getType(), taskManager.getByCode(task.getId()).getType());
        assertEquals(task.getId(), taskManager.getByCode(task.getId()).getId());
    }

    @Test
    public void TestChangesTaskInHistory() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.addTask(task);
        historyManager.add(taskManager.getByCode(task.getId()));

        task.setName("bla bla");
        task.setId(2);
        taskManager.refresh(task);
        historyManager.add(taskManager.getByCode(task.getId()));
        assertNotEquals(historyManager.getHistory().get(0), historyManager.getHistory().get(1));
    }
}