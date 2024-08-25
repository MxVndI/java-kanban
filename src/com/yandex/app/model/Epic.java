package com.yandex.app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<SubTask> subTasks;
    private LocalDateTime endTime;

    public Epic(String name, String description, LocalDateTime startTime, int duration) {
        super(description, name, startTime, duration);
        subTasks = new ArrayList<SubTask>();
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
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }

    private void checkStatus() {
        if (subTasks.isEmpty())
            status = TaskStatus.NEW;
        else {
            int countSubtasks = getSubTasks().size();
            int countDone = 0;
            int countNew = 0;
            for (SubTask task : subTasks) {
                if (task.getStatus() == TaskStatus.NEW)
                    countNew++;
                else if (task.getStatus() == TaskStatus.DONE)
                    countDone++;
                else {
                    status = TaskStatus.IN_PROGRESS;
                    return;
                }
            }
            if (countNew == countSubtasks) {
                status = TaskStatus.NEW;
            } else if (countDone == countSubtasks) {
                status = TaskStatus.DONE;
            } else status = TaskStatus.IN_PROGRESS;
        }
    }

    public void swapSubTask(SubTask oldTask, SubTask newTask) {
        subTasks.remove(oldTask);
        subTasks.add(newTask);
        checkStatus();
    }

    @Override
    public LocalDateTime getEndTime() {
        endTime = subTasks.get(0).getEndTime();
        for (SubTask subTask: subTasks) {
            if (subTask.getEndTime().isAfter(endTime))
                endTime = subTask.startTime;
        }
        return endTime;
    }

    @Override
    public LocalDateTime getStartTime() {
        if (!subTasks.isEmpty())
            startTime = subTasks.get(0).startTime;
        else startTime = LocalDateTime.of(1970,1,1,1,1,1);
        for (SubTask subTask: subTasks) {
            if (subTask.startTime.isBefore(startTime))
                startTime = subTask.startTime;
        }
        return startTime;
    }
}
