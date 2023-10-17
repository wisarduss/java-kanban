package ru.practicum.tracker.tasks;

import ru.practicum.tracker.tasks.models.Status;
import ru.practicum.tracker.tasks.models.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Long> subtasksIds;
    private LocalDateTime endTime;

    public Epic() {
        subtasksIds = new ArrayList<>();
    }

    public Epic(long id, TaskType taskType, String name, Status status, String description,
                LocalDateTime startTime, Long duration) {
        super(id, taskType, name, status, description, startTime, duration);
        this.subtasksIds = new ArrayList<>();
    }

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
        this.subtasksIds = new ArrayList<>();
    }

    public Epic(String name, String description, long id) {
        super(name, description, id, Status.NEW);
        this.subtasksIds = new ArrayList<>();
    }

    public Epic(long id, TaskType taskType, String name, Status status, String description) {
        super(id, taskType, name, status, description);
        this.subtasksIds = new ArrayList<>();
    }


    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    public ArrayList<Long> getSubtasksIds() {
        return subtasksIds;
    }

    public void addSubtaskId(long subtaskId) {
        if (subtasksIds == null) {
            subtasksIds = new ArrayList<>();
        }
        subtasksIds.add(subtaskId);
    }

    public void clears() {
        subtasksIds.clear();
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


    @Override
    public String toString() {
        return "Epic{" +
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
        Epic epic = (Epic) o;
        return Objects.equals(subtasksIds, epic.subtasksIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksIds);
    }
}
