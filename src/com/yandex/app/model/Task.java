package com.yandex.app.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected int id;
    protected String description;
    protected String name;
    protected TaskStatus status;
    protected int duration;
    protected LocalDateTime startTime;

    public Task(String name, String description, LocalDateTime startTime, int duration) {
        this.description = description;
        this.name = name;
        this.startTime = startTime;
        this.duration = duration;
        status = TaskStatus.NEW;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public void setStatus(TaskStatus status) {
        if (this.getType() != TaskType.EPIC)
            this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || !obj.getClass().equals(this.getClass()))
            return false;
        Task task = (Task) obj;
        return Objects.equals(task.name, name) && task.status == status && task.id == id &&
                Objects.equals(task.description, description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        LocalDateTime endTime = startTime.plusMinutes(duration);
        return endTime;
    }
}
