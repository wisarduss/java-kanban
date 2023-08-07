package ru.practicum.task_tracker.tasks;

import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Long> subtasksIds;

    public Epic(String name, String description) {
        super(name, description, "NEW");
        this.subtasksIds = new ArrayList<>();
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

    public void removeById(Long subtaskId) {
        for (int i = 0; i < subtasksIds.size(); i++) {
            subtasksIds.remove(subtaskId);
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", progress='" + progress + '\'' +
                '}';
    }
}
