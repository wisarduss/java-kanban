package ru.practicum.task_tracker.tasks;

import java.util.Objects;

public class Subtask extends Task {
    private long epicId;

    public Subtask(String name, String description, String progress, Long epicId) {
        super(name, description, progress);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, long id, String progress, long epicId) {
        super(name, description, id, progress);
        this.epicId = epicId;
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
                ", progress='" + progress + '\'' +
                ", epicId='" + epicId + '\'' +
                '}';
    }
}
