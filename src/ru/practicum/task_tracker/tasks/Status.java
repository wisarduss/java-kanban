package ru.practicum.task_tracker.tasks;

public enum Status {
    NEW("Новый"),
    IN_PROGRESS("В процессе"),
    DONE("Готово");

    private final String rus;

    Status(String rus) {
        this.rus = rus;
    }

    @Override
    public String toString() {
        return "[" + rus + "]";
    }
}


