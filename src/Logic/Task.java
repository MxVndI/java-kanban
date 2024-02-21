package Logic;

import java.util.Objects;

public class Task {
    protected int id;
    protected String description;
    protected String name;
    protected TaskStatus status;
    protected TaskType type;

    public Task(String description, String name, TaskType type) {
        this.description = description;
        this.name = name;
        status = TaskStatus.NEW;
        id = hashCode();
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public TaskType getType() {
        return type;
    }

    public void setStatus(TaskStatus status) {
        if (this.type != TaskType.EPIC)
            this.status = status;
        id = hashCode();
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
        if(obj == null || obj.getClass().equals(this.getClass()) == false)
            return false;
        Task task = (Task) obj;
        return task.name == name && task.status == status && task.id == id && task.description == description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, type);
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
