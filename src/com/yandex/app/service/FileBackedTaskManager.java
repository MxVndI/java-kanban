package com.yandex.app.service;

import com.yandex.app.model.SubTask;
import com.yandex.app.model.Epic;
import com.yandex.app.model.Task;
import com.yandex.app.model.TaskType;
import com.yandex.app.model.TaskStatus;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    private static final String HEADER_FILE = "id,type,name,status,description,epic \n";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
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
        switch (TaskType.valueOf(data[1])) {
            case TASK:
                Task task = new Task(data[2], data[4]);
                task.setId(Integer.parseInt(data[0]));
                task.setStatus(TaskStatus.valueOf(data[3]));
                return task;
            case EPIC:
                Epic epic = new Epic(data[4], data[2]);
                epic.setId(Integer.parseInt(data[0]));
                epic.setStatus(TaskStatus.valueOf(data[3]));
                return epic;
            case SUBTASK:
                SubTask subTask = new SubTask(data[2], data[4], Integer.parseInt(data[5]));
                subTask.setId(Integer.parseInt(data[0]));
                subTask.setStatus(TaskStatus.valueOf(data[3]));
                return subTask;
        }
        return null;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager f = new FileBackedTaskManager(file);
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            br.readLine();
            while (br.ready()) {
                String line = br.readLine();
                Task task = f.fromStringTask(line);
                if (task instanceof Epic epic) {
                    f.addEpic(epic);
                } else if (task instanceof SubTask subtask) {
                    f.addSubtask(subtask);
                } else {
                    f.addTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать данные из файла.");
        }
        return f;
    }

    @Override
    public void refresh(Task task) {
        FileBackedTaskManager f = new FileBackedTaskManager(file);
        if (task.getType() == TaskType.EPIC) {
            Epic oldEpic = (Epic)f.getByCode(task.getId());
            oldEpic.setName(task.getName());
            oldEpic.setDescription(task.getDescription());
        } else if (task.getType() == TaskType.TASK) {
            Task oldTask = f.getByCode(task.getId());
            oldTask.setName(task.getName());
            oldTask.setDescription(task.getDescription());
        } else if (task.getType() == TaskType.SUBTASK) {
            Epic epic = (Epic)f.getByCode(((SubTask) task).getEpicId());
            epic.swapSubTask((SubTask)getByCode(task.getId()), (SubTask) task);
            SubTask oldSubTask = (SubTask) f.getByCode(task.getId());
            oldSubTask.setName(task.getName());
            oldSubTask.setDescription(task.getDescription());
        }
        save();
    }
}
