package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private long generatorId = 0;
    private final HashMap<Long, Epic> epics = new HashMap<>();
    private final HashMap<Long, Task> tasks = new HashMap<>();
    private final HashMap<Long, Subtask> subtasks = new HashMap<>();

    public long addNewEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    public Long addNewSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return null;
        }
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        epic.addSubtaskId(subtask.getId());
        updateStatusEpic(subtask.getEpicId());
        return subtask.getId();
    }

    public Long addNewTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public void updateTask(Task task) {
        Task saveTask = tasks.get(task.getId());
        if (saveTask == null) {
            return;
        }
        saveTask.setName(task.getName());
        saveTask.setDescription(task.getDescription());
        saveTask.setProgress(task.getName());
    }

    public void updateSubtask(Subtask subtask) {
        Subtask saveSubtask = subtasks.get(subtask.getId());
        long checkEpicId = subtask.getEpicId();
        if (saveSubtask == null || (subtask.getEpicId() != checkEpicId)) {
            return;
        }
        saveSubtask.setName(subtask.getName());
        saveSubtask.setDescription(subtask.getDescription());
        saveSubtask.setProgress(subtask.getProgress());
        updateStatusEpic(subtask.getEpicId());
    }

    public void updateEpic(Epic epic) {
        Epic saveEpic = epics.get(epic.getId());
        if (saveEpic == null) {
            return;
        }
        saveEpic.setName(epic.getName());
        saveEpic.setDescription(epic.getDescription());
    }

    private void updateStatusEpic(long epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Long> subtaskIds = epic.getSubtasksIds();
        if (subtaskIds.isEmpty()) {
            epic.setProgress("NEW");
            return;
        }

        String progress = null;
        for (long subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);

            if (progress == null) {
                progress = subtask.getProgress();
                continue;
            }

            if (progress.equals(subtask.getProgress()) && !progress.equals("IN_PROGRESS")) {
                continue;
            }

            epic.setProgress("IN_PROGRESS");
            return;
        }
        epic.setProgress(progress);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    private long generateId() {
        return generatorId++;
    }


    public void removeTasks() {
        tasks.clear();
    }

    public void removeSubtasks() {
        for (Long id : epics.keySet()) {
            epics.get(id).clears();
        }
        for (Long id : subtasks.keySet()) {
            updateStatusEpic(subtasks.get(id).getEpicId());
        }
        subtasks.clear();
    }

    public void removeEpics() {
        epics.clear();
        subtasks.clear();
    }

    public Task getTaskById(long taskId) {
        for (Long id : tasks.keySet()) {
            if (id.equals(taskId)) {
                return tasks.get(id);
            }
        }
        return null;
    }

    public Subtask getSubtaskById(long subtaskId) {
        for (Long id : subtasks.keySet()) {
            if (id.equals(subtaskId)) {
                return subtasks.get(id);
            }
        }
        return null;
    }

    public Epic getEpicById(long epicId) {
        for (Long id : epics.keySet()) {
            if (id.equals(epicId)) {
                return epics.get(id);
            }
        }
        return null;
    }

    public void removeTaskById(long taskId) {
        if (!tasks.containsKey(taskId)) {
            return;
        }

        tasks.remove(taskId);
    }

    public void removeSubtaskById(long subtaskId) {
        for (Long id : epics.keySet()) {
            epics.get(id).removeById(subtaskId);
        }
        for (Long id : subtasks.keySet()) {
            updateStatusEpic(subtasks.get(id).getEpicId());
        }
        subtasks.remove(subtaskId);
    }

    public void removeEpicById(long epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Long> subtaskIds = epic.getSubtasksIds();

        for (Long id : subtaskIds) {
            subtasks.remove(id);
        }
        epics.remove(epicId);
    }

    public ArrayList<Subtask> getAllSubtasksOfEpic(long epicId) {
        ArrayList<Subtask> subtasksOfEpicId = new ArrayList<>();
        for (Long id : subtasks.keySet()) {
            if (subtasks.get(id).getEpicId() == epicId) {
                subtasksOfEpicId.add(subtasks.get(id));
            }
        }
        return subtasksOfEpicId;
    }

}
