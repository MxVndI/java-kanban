package com.yandex.app.model;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<SubTask> subTasks;

    public Epic(String description, String name) {
        super(description, name);
        subTasks = new ArrayList<>();
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    public void addSubTask(SubTask task) {
        subTasks.add(task);
        checkStatus();
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks);
    }

    public void removeSubtasks() {
        subTasks.clear();
        checkStatus();
    }

    public void removeSubtask(int id) {
        for (SubTask task : subTasks)
            if (task.getId() == id) {
                subTasks.remove(task);
                checkStatus();
                break;
            }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", subTasks=" + subTasks +
                '}';
    }

    private void checkStatus() {
        if (subTasks.isEmpty())
            status = TaskStatus.NEW;
        else {
            for (SubTask subTask : subTasks)
                if (subTask.getStatus() != TaskStatus.DONE && subTask.getStatus() != TaskStatus.NEW) {
                    status = TaskStatus.IN_PROGRESS;
                    return;
                }
            status = TaskStatus.DONE;
        }
    }
}
