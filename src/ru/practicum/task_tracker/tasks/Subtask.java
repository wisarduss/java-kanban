package ru.practicum.task_tracker.tasks;

public class Subtask extends Task {
    private final long epicId;

    public Subtask(String name, String description, Status status, Long epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, long id, Status status, long epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public Subtask(long id, TaskType taskType, String name, Status status, String description, long epicId) {
        super(id, taskType, name, status, description);
        this.epicId = epicId;
    }

    @Override
    public TaskType getType (){
        return TaskType.SUBTASK;
    }

    public long getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", progress='" + status + '\'' +
                ", epicId='" + epicId + '\'' +
                '}';
    }
}
