package ru.practicum.tracker.manager;

import ru.practicum.tracker.tasks.Status;
import ru.practicum.tracker.tasks.Subtask;
import ru.practicum.tracker.tasks.Task;
import ru.practicum.tracker.tasks.TaskType;
import ru.practicum.tracker.tasks.Epic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SCVFormatterUtils {

    public static String toString(Task task) {
        if (task != null) {
            if (task.getType() == TaskType.valueOf("SUBTASK")) {
                return String.format("%d,%s,%s,%s,%s,%s,%d,%d,", task.getId(),
                        task.getType(), task.getName(),
                        task.getStatus(), task.getDescription(), task.getStartTime(), task.getDuration(),
                        ((Subtask) task).getEpicId()
                );
            }
            return String.format("%d,%s,%s,%s,%s,%s,%d,", task.getId(),
                    task.getType(), task.getName(),
                    task.getStatus(), task.getDescription(),
                    task.getStartTime(), task.getDuration());
        }
        return null;
    }

    public static Task fromString(String taskStr) {
        String[] tokens = taskStr.split(",");
        int id = Integer.parseInt(tokens[0]);
        String name = tokens[2];
        Status status = Status.valueOf(tokens[3]);
        String desc = tokens[4];
        long epicId = 0;
        if (tokens.length == 8) {
            if (!tokens[7].isEmpty()) {
                epicId = Long.parseLong(tokens[7]);
            }
        }
        TaskType type = TaskType.valueOf(tokens[1]);
        switch (type) {
            case TASK:
                if (Objects.equals(tokens[5], "null") && Objects.equals(tokens[6], "null")) {
                    return new Task(id, type, name, status, desc);
                } else {
                    LocalDateTime startTime = LocalDateTime.parse(tokens[5]);
                    long duration = Long.parseLong(tokens[6]);
                    return new Task(id, type, name, status, desc, startTime, duration);
                }
            case SUBTASK:
                if (Objects.equals(tokens[5], "null") && Objects.equals(tokens[6], "null")) {
                    return new Subtask(id, type, name, status, desc, epicId);
                } else {
                    LocalDateTime startTime = LocalDateTime.parse(tokens[5]);
                    long duration = Long.parseLong(tokens[6]);
                    return new Subtask(id, type, name, status, desc, epicId, startTime, duration);
                }
            case EPIC:
                if (Objects.equals(tokens[5], "null") && Objects.equals(tokens[6], "null")) {
                    return new Epic(id, type, name, status, desc);
                } else {
                    LocalDateTime startTime = LocalDateTime.parse(tokens[5]);
                    long duration = Long.parseLong(tokens[6]);
                    return new Epic(id, type, name, status, desc, startTime, duration);
                }
        }
        return null;
    }

    public static String historyToString(HistoryManager manager) {
        if (manager.getHistory().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getId()).append(",");
        }
        return sb.toString();
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> id = new ArrayList<>();
        if (!value.isEmpty()) {
            String[] tokens = value.split(",");

            for (String token : tokens) {
                id.add(Integer.parseInt(token));
            }
        }
        return id;
    }
}
