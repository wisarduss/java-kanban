package ru.practicum.tracker.exception;

public class TaskValidateException extends RuntimeException {
    public TaskValidateException() {
        super();
    }

    public TaskValidateException(String message) {
        super(message);
    }

    public TaskValidateException(String message, Throwable cause) {
        super(message, cause);
    }
}
