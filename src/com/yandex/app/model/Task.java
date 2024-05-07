package com.yandex.app.model;

import java.util.Objects;

public class Task {
    protected int id;
    protected String description;
    protected String name;
    protected TaskStatus status;

    public Task(String description, String name) {
        this.description = description;
        this.name = name;
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
                '}';
    }
}
