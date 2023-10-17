package ru.practicum.tracker.exception;

public class HttpTaskServerException extends RuntimeException{

    public HttpTaskServerException() {
        super();
    }

    public HttpTaskServerException(String message) {
        super(message);
    }

    public HttpTaskServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
