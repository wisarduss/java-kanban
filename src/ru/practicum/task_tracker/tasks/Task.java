package ru.practicum.task_tracker.tasks;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected long id;
    protected String progress;

    public Task(String name, String description, String progress) {
        this.name = name;
        this.description = description;
        this.progress = progress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", progress='" + progress + '\'' +
                '}';
    }

}
