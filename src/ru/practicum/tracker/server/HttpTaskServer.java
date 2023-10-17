package ru.practicum.tracker.server;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.practicum.tracker.exception.HttpTaskServerException;
import ru.practicum.tracker.manager.HttpTaskManager;
import ru.practicum.tracker.tasks.Epic;
import ru.practicum.tracker.tasks.Subtask;
import ru.practicum.tracker.tasks.Task;
import ru.practicum.tracker.tasks.models.Endpoint;
import ru.practicum.tracker.utils.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static ru.practicum.tracker.tasks.models.TaskType.TASK;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;
    private final HttpTaskManager httpTaskManager;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskConverterToGson())
            .registerTypeAdapter(Epic.class, new EpicConverterToGson())
            .registerTypeAdapter(Subtask.class, new SubtaskConverterToGson())
            .registerTypeAdapter(Task.class, new TaskConverterFromGson())
            .registerTypeAdapter(Epic.class, new EpicConverterFromGson())
            .registerTypeAdapter(Subtask.class, new SubtaskConverterFromGson())
            .create();


    public HttpTaskServer() throws IOException{
        httpServer = HttpServer.create(new InetSocketAddress("localhost",PORT), 0);
        httpServer.createContext("/tasks",this::TaskHandler);
        httpTaskManager = new HttpTaskManager();
    }

    public void start() {
        System.out.println("Запускаем HttpTaskServer на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT);
        httpServer.start();
    }

    public void stop() {
        System.out.println("HttpTaskServer завершает работу");
        httpServer.stop(5);
    }


    public void TaskHandler(HttpExchange exchange) {
        Endpoint endpoint = getEndpoint(exchange);
        switch (endpoint) {
            case GET_TASKS -> getTasks(exchange);
            case POST_TASK -> addTask(exchange);
            case GET_TASK_BY_ID -> getTaskById(exchange);
            case DELETE_TASKS -> removeTasks(exchange);
            case DELETE_TASK_BY_ID -> removeTaskById(exchange);
            case GET_EPICS -> getEpics(exchange);
            case POST_EPIC -> addEpic(exchange);
            case GET_EPIC_BY_ID -> getEpicById(exchange);
            case DELETE_EPICS -> removeEpics(exchange);
            case DELETE_EPIC_BY_ID -> removeEpicById(exchange);
            case GET_SUBTASKS -> getSubtasks(exchange);
            case POST_SUBTASK -> addSubtask(exchange);
            case GET_SUBTASK_BY_ID -> getSubtaskById(exchange);
            case DELETE_SUBTASKS -> removeSubtasks(exchange);
            case DELETE_SUBTASK_BY_ID -> removeSubtaskById(exchange);
            case GET_PRIORITIZED_TASKS -> getPrioritizedTasks(exchange);
            case GET_EPIC_SUBTASKS_BY_ID -> getAllSubtasksOfEpic(exchange);
            case GET_HISTORY -> getHistory(exchange);
            default -> defaultEndpoint(exchange);
        }
    }

    private void getTasks(HttpExchange exchange) {
        try {
            if (!httpTaskManager.getTasks().isEmpty()) {
                String TaskToGson = gson.toJson(httpTaskManager.getTasks());
                writeResponse(exchange, TaskToGson, 200);
            } else {
                writeResponse(exchange, "Список tasks пустой", 404);
            }
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка в методе getTasks()");
        }
    }

    private void addTask(HttpExchange exchange)  {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Task task = gson.fromJson(body, Task.class);
            JsonObject jsonObject = extractJsonObject(exchange, body);

            int id = jsonObject.get("id").getAsInt();
            if (id == 0) {
                httpTaskManager.addNewTask(task);
                writeResponse(exchange, "Task успешно добавлена", 200);
            } else {
                for (Task task1 : httpTaskManager.getTasks()) {
                    if (task1.getId() == id) {
                        httpTaskManager.updateTask(task);
                        writeResponse(exchange, "Task c id=" + id + " успешно обновлена", 200);
                    }
                }
                writeResponse(exchange, "Task c id=" + id + " не существует", 400);

            }
        }catch (IOException e) {
            throw new HttpTaskServerException("Ошибка в методе addTask()");
        }
    }

    private void getTaskById(HttpExchange exchange) {
        try {
            long id = Integer.parseInt(exchange.getRequestURI().getQuery().substring(3));
            if (httpTaskManager.getTaskById(id) != null) {
                String TaskToGson = gson.toJson(httpTaskManager.getTaskById(id));
                writeResponse(exchange, TaskToGson, 200);
            } else {
                writeResponse(exchange, "Task c id=" + id + " не найден", 404);
            }
        } catch (IOException exp) {
            throw new HttpTaskServerException("Ошибка в методе getTaskById()");
        }
    }

    private void removeTasks(HttpExchange exchange) {
        try {
            httpTaskManager.removeTasks();
            writeResponse(exchange, "tasks удалены", 200);
        } catch (IOException exp) {
            throw new HttpTaskServerException("Ошибка в методе removeTasks()");
        }
    }

    private void removeTaskById(HttpExchange exchange) {
        try {
            long id = Integer.parseInt(exchange.getRequestURI().getQuery().substring(3));

            for (Task task : httpTaskManager.getTasks()) {
                if ( task.getId() == id) {
                    httpTaskManager.removeTaskById(id);
                    writeResponse(exchange, "Task c id=" + id + " удален", 200);
                    return;
                }
            }
            writeResponse(exchange, "Task c id=" + id + " не существует", 404);
        } catch (IOException exp) {
            throw new HttpTaskServerException("Ошибка в методе removeTaskById()");
        }
    }

    private void getEpics(HttpExchange exchange) {
        try {
            if (!httpTaskManager.getEpics().isEmpty()) {
                String epicToGson = gson.toJson(httpTaskManager.getEpics());
                writeResponse(exchange, epicToGson, 200);
            } else {
                writeResponse(exchange, "epics пустой", 404);
            }
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка в методе getEpics()");
        }
    }
    private void addEpic(HttpExchange exchange) {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(body, Epic.class);
            JsonObject jsonObject = extractJsonObject(exchange, body);

            int id = jsonObject.get("id").getAsInt();
            if (id == 0) {
                httpTaskManager.addNewEpic(epic);
                writeResponse(exchange, "Epic добавлен", 200);
            } else {
                for (Epic epic1 : httpTaskManager.getEpics()) {
                    if (epic1.getId() == id) {
                        httpTaskManager.updateEpic(epic);
                        writeResponse(exchange, "Epic c id=" + id + " обновлен", 200);

                    }
                }
                writeResponse(exchange, "Epic c id=" + id + " не существует", 400);

            }
        } catch (IOException e) {
            throw new HttpTaskServerException("ошибка в методе addEpic()");
        }
    }

    private void getEpicById(HttpExchange exchange) {
        try {
            long id = Integer.parseInt(exchange.getRequestURI().getQuery().substring(3));
            if (httpTaskManager.getEpicById(id) != null) {
                String resp = gson.toJson(httpTaskManager.getEpicById(id));
                writeResponse(exchange, resp, 200);
            } else {
                writeResponse(exchange, "Epic c id=" + id + " не найден", 404);
            }
        } catch (IOException exp) {
            throw new HttpTaskServerException("Ошибка в методе getEpicById()");
        }
    }

    private void removeEpics(HttpExchange exchange) {
        try {
            httpTaskManager.removeEpics();
            writeResponse(exchange, "epics удалены", 200);
        } catch (IOException exp) {
            throw new HttpTaskServerException("Ошибка в методе removeEpics()");
        }
    }

    private void removeEpicById(HttpExchange exchange) {
        try {
            long id = Integer.parseInt(exchange.getRequestURI().getQuery().substring(3));

            for (Epic epic : httpTaskManager.getEpics()) {
                if (epic.getId() == id) {
                    httpTaskManager.removeEpicById(id);
                    writeResponse(exchange, "Epic c id=" + id + " удален", 200);
                }
            }
            writeResponse(exchange, "Epic c id=" + id + " не существует", 404);
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка в методе removeEpicById()");
        }
    }

    private void getSubtasks(HttpExchange exchange) {
        try {
            if (!httpTaskManager.getSubtasks().isEmpty()) {
                String subtaskToGson = gson.toJson(httpTaskManager.getSubtasks());
                writeResponse(exchange, subtaskToGson, 200);
            } else {
                writeResponse(exchange, "Список subtasks пустой", 404);
            }
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка в методе getSubtasks()");
        }
    }

    private void addSubtask(HttpExchange exchange) {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtask = gson.fromJson(body, Subtask.class);
            JsonObject jsonObject = extractJsonObject(exchange, body);

            int id = jsonObject.get("id").getAsInt();
            int epicId = jsonObject.get("epicId").getAsInt();
            if (id == 0) {
                if (epicId == 0) {
                    writeResponse(exchange, "epicId=" + epicId + ", не существует", 400);
                }
                for (Epic epic : httpTaskManager.getEpics()) {
                    if (epic.getId() == epicId) {
                        httpTaskManager.addNewSubtask(subtask);
                        writeResponse(exchange, "subtask добавлен под эпиком с epicID=" + epicId
                                , 200);
                    }
                }
                writeResponse(exchange, " epicId=" + epicId + ", не существует", 400);
            }

            for (Subtask subtask1 : httpTaskManager.getSubtasks()) {
                if ( subtask1.getId() == id)  {
                    for (Epic epic : httpTaskManager.getEpics()) {
                        if (epic.getId() == epicId) {
                            httpTaskManager.updateSubtask(subtask);
                            writeResponse(exchange, "subtask с id=" + id +  " обновлен"
                                    , 200);
                        }
                    }
                    writeResponse(exchange, "Указан неверный epicId", 400);
                }
            }
            writeResponse(exchange, "Subtask c id=" + id + " не существует", 400);
        }catch (IOException e) {
            throw new HttpTaskServerException("ошибка в методе addSubtask()");
        }
    }

    private void getSubtaskById(HttpExchange exchange) {
        try {
            long id = Integer.parseInt(exchange.getRequestURI().getQuery().substring(3));
            if (httpTaskManager.getSubtaskById(id) != null) {
                String subtaskToGson = gson.toJson(httpTaskManager.getSubtaskById(id));
                writeResponse(exchange, subtaskToGson, 200);
            } else {
                writeResponse(exchange, "Subtask c id=" + id + " не найден", 404);
            }
        } catch (IOException exp) {
            throw new HttpTaskServerException("ошибка в методе getSubtaskById()");
        }
    }

    private void removeSubtasks(HttpExchange exchange) {
        try {
            httpTaskManager.removeSubtasks();
            writeResponse(exchange, "subtasks удалены", 200);
        } catch (IOException exp) {
            throw new HttpTaskServerException("ошибка в методе removeSubtasks()");
        }
    }

    private void removeSubtaskById(HttpExchange exchange) {
        try {
            long id = Integer.parseInt(exchange.getRequestURI().getQuery().substring(3));

            for (Subtask subtask : httpTaskManager.getSubtasks()) {
                if (subtask.getId() == id) {
                    httpTaskManager.removeSubtaskById(id);
                    writeResponse(exchange, "Subtask c id=" + id + " удален" , 200);
                }
            }
            writeResponse(exchange, "Subtask c id=" + id + " не существует", 404);
        } catch (IOException exp) {
            throw new HttpTaskServerException("ошибка в методе removeSubtaskById()");
        }
    }
    private void getPrioritizedTasks(HttpExchange exchange) {
        try {
            if (!httpTaskManager.getPrioritizedTasks().isEmpty()) {
                String taskToJson = gson.toJson(httpTaskManager.getPrioritizedTasks());
                writeResponse(exchange, taskToJson, 200);
            } else {
                writeResponse(exchange, "PrioritizedTasks пустой", 404);
            }
        } catch (IOException exp) {
            throw new HttpTaskServerException("ошибка в методе getPrioritizedTasks()");
        }
    }

    private void getAllSubtasksOfEpic(HttpExchange exchange) {
        try {
            long id = Integer.parseInt(exchange.getRequestURI().getQuery().substring(3));
            if (!httpTaskManager.getAllSubtasksOfEpic(id).isEmpty()) {
                String subtaskToGson = gson.toJson(httpTaskManager.getAllSubtasksOfEpic(id));
                writeResponse(exchange, subtaskToGson, 200);
            } else {
                writeResponse(exchange, "subtasks эпика с id=" + id + " пустой"
                        , 404);
            }
        } catch (IOException exp) {
            throw new HttpTaskServerException("ошибка в методе getAllSubtasksOfEpic()");
        }
    }

    private void getHistory(HttpExchange exchange) {
        try {
            if (!httpTaskManager.getHistory().isEmpty()) {
                String historyToGson = gson.toJson(httpTaskManager.getHistory());
                writeResponse(exchange, historyToGson, 200);
            } else {
                writeResponse(exchange, "История просмотров пуста", 404);
            }
        } catch (IOException exp) {
            throw new RuntimeException("Ошибка при запросе getHistory()");
        }
    }

    private Endpoint getEndpoint(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        String query = exchange.getRequestURI().getQuery();

        switch (requestMethod) {
            case "GET" -> {
                if (pathParts.length == 2 && query == null) {
                    return Endpoint.GET_PRIORITIZED_TASKS;
                }
                if (pathParts.length == 3 && query == null) {
                    if ((pathParts[2].equals("task"))) {
                        return Endpoint.GET_TASKS;
                    }
                    if ((pathParts[2].equals("epic"))) {
                        return Endpoint.GET_EPICS;
                    }
                    if ((pathParts[2].equals("subtask"))) {
                        return Endpoint.GET_SUBTASKS;
                    }
                    if (pathParts[2].equals("history")) {
                        return Endpoint.GET_HISTORY;
                    }
                }
                if (pathParts.length == 3 && query != null) {
                    if ((pathParts[2].equals("task"))) {
                        return Endpoint.GET_TASK_BY_ID;
                    }
                    if ((pathParts[2].equals("epic"))) {
                        return Endpoint.GET_EPIC_BY_ID;
                    }
                    if ((pathParts[2].equals("subtask"))) {
                        return Endpoint.GET_SUBTASK_BY_ID;
                    }
                }
                if (pathParts.length == 4 && pathParts[2].equals("subtask") && query != null && pathParts[3].equals("epic")) {
                    return Endpoint.GET_EPIC_SUBTASKS_BY_ID;
                }
            }
            case "POST" -> {
                if (pathParts.length == 3 && query == null) {
                    if ((pathParts[2].equals("task"))) {
                        return Endpoint.POST_TASK;
                    }
                    if ((pathParts[2].equals("epic"))) {
                        return Endpoint.POST_EPIC;
                    }
                    if ((pathParts[2].equals("subtask"))) {
                        return Endpoint.POST_SUBTASK;
                    }
                }
            }
            case "DELETE" -> {
                if (pathParts.length == 3 && query == null) {
                    if ((pathParts[2].equals("task"))) {
                        return Endpoint.DELETE_TASKS;
                    }
                    if ((pathParts[2].equals("epic"))) {
                        return Endpoint.DELETE_EPICS;
                    }
                    if ((pathParts[2].equals("subtask"))) {
                        return Endpoint.DELETE_SUBTASKS;
                    }
                }
                if (pathParts.length == 3 && query != null) {
                    if ((pathParts[2].equals("task"))) {
                        return Endpoint.DELETE_TASK_BY_ID;
                    }
                    if ((pathParts[2].equals("epic"))) {
                        return Endpoint.DELETE_EPIC_BY_ID;
                    }
                    if ((pathParts[2].equals("subtask"))) {
                        return Endpoint.DELETE_SUBTASK_BY_ID;
                    }
                }
            }
        }

        return Endpoint.UNKNOWN;
    }

    private void defaultEndpoint(HttpExchange exchange) {
        try {
            writeResponse(exchange, "Некорректный запрос, попробуйте еще раз", 400);
        } catch (IOException e) {
            throw new HttpTaskServerException("Ошибка в методе defaultEndpoint()");
        }
    }
    private JsonObject extractJsonObject(HttpExchange h, String body) throws IOException {
        JsonElement jsonElement = JsonParser.parseString(body);

        if (!jsonElement.isJsonObject()) { // проверяем, точно ли мы получили JSON-объект
            writeResponse(h, "Передано тело не в формате JSON", 400);
            System.out.println("Передано тело не в формате JSON");
        }
        return jsonElement.getAsJsonObject();
    }

    private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }
}
