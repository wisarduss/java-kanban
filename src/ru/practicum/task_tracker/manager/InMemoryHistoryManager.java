package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int LIMIT = 10;
    private static final int FIRST_ELEMENT = 0;
    private final List<Task> history = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        if (task == null) {
            return;
        }
        history.add(task);
        if (history.size() > LIMIT) {
            history.remove(FIRST_ELEMENT);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
