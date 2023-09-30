package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.*;

import java.util.ArrayList;
import java.util.List;

public class SCVFormatter {

    public static String toString(Task task) {
        if (task != null) {
            if (task.getType() == TaskType.valueOf("SUBTASK")) {
                return String.format("%d,%s,%s,%s,%s,%d,", task.getId(),
                        task.getType(), task.getName(),
                        task.getStatus(), task.getDescription(), ((Subtask) task).getEpicId());
            }
            return String.format("%d,%s,%s,%s,%s,", task.getId(),
                    task.getType(), task.getName(),
                    task.getStatus(), task.getDescription());
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
        if (tokens.length == 6) {
            if (!tokens[5].isEmpty()) {
                epicId = Long.parseLong(tokens[5]);
            }
        }
        TaskType type = TaskType.valueOf(tokens[1]);

        switch (type) {
            case TASK:
                return new Task(id, type, name, status, desc);
            case SUBTASK:
                return new Subtask(id, type, name, status, desc, epicId);
            case EPIC:
                return new Epic(id, type, name, status, desc);
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
        String[] tokens = value.split(",");

        for (String token : tokens) {
            id.add(Integer.parseInt(token));
        }
        return id;
    }
}
