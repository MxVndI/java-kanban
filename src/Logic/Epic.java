package Logic;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<SubTask> subTasks;

    public Epic(String description, String name, TaskType type) {
        super(description, name, type);
        subTasks = new ArrayList<>();
    }

    public void addSubTask(SubTask task) {
        subTasks.add(task);
    }

    public void printSubTasks() {
        for (SubTask task : subTasks)
            System.out.println(task);
    }

    public void removeSubtasks() {
        subTasks.removeAll(subTasks);
    }

    public void removeSubtask(int id) {
        for (SubTask task : subTasks)
            if (task.getId() == id)
                subTasks.remove(task);
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

    public void checkStatus() {
        if (subTasks.isEmpty())
            status = TaskStatus.NEW;
        else {
            for (SubTask subTask : subTasks)
                if (subTask.getStatus() != TaskStatus.DONE) {
                    status = TaskStatus.IN_PROGRESS;
                    return;
                }
            status = TaskStatus.DONE;
        }
    }
}
