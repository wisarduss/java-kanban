package ru.practicum.tracker.server;

import java.io.IOException;

public class HttpTaskServerStarter {
    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }
}
