package ru.practicum.tracker.tasks;

import ru.practicum.tracker.tasks.models.Status;
import ru.practicum.tracker.tasks.models.TaskType;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private long epicId;


    public Subtask() {

    }

    public Subtask(String name, String description, Status status, Long epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }


    public Subtask(String name, String description, Status status, LocalDateTime startTime, Long duration, Long epicId) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, Long epicId, LocalDateTime startTime, Long duration) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, long id, Status status, long epicId,
                   LocalDateTime startTime, Long duration) {
        super(name, description, id, status, startTime, duration);
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

    public Subtask(long id, TaskType taskType, String name, Status status, String description, long epicId,
                   LocalDateTime startTime, Long duration) {
        super(id, taskType, name, status, description, startTime, duration);
        this.epicId = epicId;
    }

    @Override
    public TaskType getType() {
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
                ", status=" + status +
                ", taskType=" + taskType +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }

    @Override
    public LocalDateTime getStartTime() {
        return super.getStartTime();
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        super.setStartTime(startTime);
    }

    @Override
    public Long getDuration() {
        return super.getDuration();
    }

    @Override
    public void setDuration(long duration) {
        super.setDuration(duration);
    }

    public void setEpicId(long epicId) {
        this.epicId = epicId;
    }
}
