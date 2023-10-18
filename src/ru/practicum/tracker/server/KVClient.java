package ru.practicum.tracker.server;

import ru.practicum.tracker.exception.RegisterException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVClient {
    private final String url;

    private String apiToken;
    private final HttpClient httpClient;

    public KVClient(final String url) {
        this.url = url;
        httpClient = HttpClient.newHttpClient();
        apiToken = register();
    }

    public String load(String key) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                return null;
            }
            if (response.statusCode() != 200) {
                throw new RuntimeException("Плохой ответ, не 200, а: " + response.statusCode());
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Не получается сделать запрос");
        }
    }

    public void put(String key, String value) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Ожидался ответ 200, а пришел: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Не получается сделать запрос");
        }
    }

    protected String register() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "register"))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RegisterException("Ожидался ответ 200, а пришел: " + response.statusCode());
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RegisterException("Не получается сделать запрос");
        }
    }
}
