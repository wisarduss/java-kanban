package ru.practicum.tracker.manager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

/*    public static TaskManager getHttpTaskManager(String Url) {
        return new HttpTaskManager(Url);
    }*/
}
