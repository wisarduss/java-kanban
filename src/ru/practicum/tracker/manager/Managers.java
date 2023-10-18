package ru.practicum.tracker.manager;

public class Managers {
    public static TaskManager getDefault() {
        return new HttpTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


}
