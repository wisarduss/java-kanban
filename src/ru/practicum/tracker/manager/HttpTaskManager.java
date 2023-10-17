package ru.practicum.tracker.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.practicum.tracker.server.KVClient;
import ru.practicum.tracker.tasks.Epic;
import ru.practicum.tracker.tasks.Subtask;
import ru.practicum.tracker.tasks.Task;
import ru.practicum.tracker.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HttpTaskManager  extends FileBackedTasksManager{
    private static final String TASK = "task";
    private static final String EPIC = "epic";
    private static final String SUBTASK = "subtask";
    private static final String HISTORY = "history";
    private final KVClient client;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Task.class, new TaskConverterToGson())
            .registerTypeAdapter(Epic.class, new EpicConverterToGson())
            .registerTypeAdapter(Subtask.class, new SubtaskConverterToGson())
            .registerTypeAdapter(Task.class, new TaskConverterFromGson())
            .registerTypeAdapter(Epic.class, new EpicConverterFromGson())
            .registerTypeAdapter(Subtask.class, new SubtaskConverterFromGson())
            .create();



    public HttpTaskManager() {
        super();
        this.client = new KVClient();
        load();
    }

    private void load() {
        String tasksGson = client.load(TASK);
        if (tasksGson != null) {
            List<Task> uploadedTasks = gson.fromJson(tasksGson, new TypeToken<ArrayList<Task>>() {
            });
            uploadedTasks.forEach(task -> {
                tasks.put(task.getId(), task);
                prioritizedTasks.add(task);
            });
        }
        String epicGson = client.load(EPIC);
        if (epicGson != null) {
            List<Epic> uploadedEpic = gson.fromJson(epicGson, new TypeToken<ArrayList<Epic>>() {});
            uploadedEpic.forEach(epic -> epics.put(epic.getId(),epic));
        }
        String subtaskGson = client.load(SUBTASK);
        if (subtaskGson != null) {
            List<Subtask> uploadedSubtasks = gson.fromJson(subtaskGson,new TypeToken<ArrayList<Subtask>>() {});
            uploadedSubtasks.forEach(subtask -> {
                subtasks.put(subtask.getId(), subtask);
                prioritizedTasks.add(subtask);
            });
        }

        String historyGson = client.load(HISTORY);
        if (historyGson != null) {
            List<Task> uploadedHistory = gson.fromJson(historyGson,new TypeToken<ArrayList<Task>>() {});
            uploadedHistory.forEach(task -> getHistoryManager().addTask(task));

        }
        additionEpicAndSubtasksIds();
    }

    private void additionEpicAndSubtasksIds() {
        if (!this.epics.isEmpty()) {
            if (!this.subtasks.isEmpty()) {
                for (Epic epic : epics.values()) {
                    for (Subtask subtask : subtasks.values()) {
                        if (Objects.equals(subtask.getEpicId(), epic.getId())) {
                            epic.getSubtasksIds().add(subtask.getId());
                        }
                    }
                }
            }
        }
    }
    @Override
    public void save() {
        client.put(TASK, gson.toJson(tasks.values()));
        client.put(EPIC,gson.toJson(epics.values()));
        client.put(SUBTASK,gson.toJson(subtasks.values()));
        client.put(HISTORY,gson.toJson(getHistoryManager().getHistory()));
    }
}
