package ru.practicum.tracker.tasks;

import java.util.ArrayList;

public class Epic extends Task{
    private final ArrayList<Long> subtasksIds;

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
    public TaskType getType (){
        return TaskType.EPIC;
    }
    public ArrayList<Long> getSubtasksIds() {
        return subtasksIds;
    }

    public void addSubtaskId(long subtaskId) {
        subtasksIds.add(subtaskId);
    }

    public void clears() {
        subtasksIds.clear();
    }


    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", progress='" + status + '\'' +
                '}';
    }
}
