package com.yandex.app.service;

import com.yandex.app.model.*;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private File file;

    private static final String HEADER_FILE = "id,type,name,status,description,epic \n";

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public void save() {
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(HEADER_FILE);
            for (Task task : getTasks()) {
                fw.write(toString(task));
            }
            for (Epic epic : getEpics()) {
                fw.write(toString(epic));
            }
            for (SubTask subTask : getSubTasks()) {
                fw.write(toString(subTask));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось найти файл для записи данных");
        }
    }

    @Override
    public void addSubtask(SubTask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void removeAll(TaskType type) {
        super.removeAll(type);
        save();
    }

    @Override
    public void remove(Integer id) {
        super.remove(id);
        save();
    }

    private String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription() + ",\n";
    }

    private String toString(Epic task) {
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription() + ",\n";
    }

    private String toString(SubTask task) {
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription() + "," + task.getEpicId() + "\n";
    }

    private Task fromStringTask(String value) {
        String[] data = value.split(",");
        switch (data[1]) {
            case "TASK":
                Task task = new Task(data[2], data[4]);
                task.setId(Integer.parseInt(data[0]));
                task.setStatus(TaskStatus.valueOf(data[3]));
                return task;
            case "EPIC":
                Epic epic = new Epic(data[2], data[4]);
                epic.setId(Integer.parseInt(data[0]));
                epic.setStatus(TaskStatus.valueOf(data[3]));
                return epic;
            case "SUBTASK":
                SubTask subTask = new SubTask(data[2], data[4], Integer.parseInt(data[5]));
                subTask.setId(Integer.parseInt(data[0]));
                subTask.setStatus(TaskStatus.valueOf(data[3]));
                return subTask;
        }
        return null;
    }

    public void loadFromFile(File file) {
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            br.readLine(); // удаляем первую строку с описанием параметров в файле
            while (br.ready()) {
                String line = br.readLine();
                Task task = fromStringTask(line);
                if (task instanceof Epic epic) {
                    addEpic(epic);
                } else if (task instanceof SubTask subtask) {
                    addSubtask(subtask);
                } else {
                    addTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать данные из файла.");
        }
    }
}
