package ru.practicum.tracker.manager;

import ru.practicum.tracker.tasks.Task;

import java.util.List;

public interface HistoryManager {

    void remove(long id);

    void addTask(Task task);

    List<Task> getHistory();
}