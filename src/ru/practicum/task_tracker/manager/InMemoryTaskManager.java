package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Status;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private long generatorId = 0;
    private final HashMap<Long, Epic> epics = new HashMap<>();
    private final HashMap<Long, Task> tasks = new HashMap<>();
    private final HashMap<Long, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public long addNewEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
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

    @Override
    public Long addNewTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public void updateTask(Task task) {
        Task saveTask = tasks.get(task.getId());
        if (saveTask == null) {
            return;
        }
        saveTask.setName(task.getName());
        saveTask.setDescription(task.getDescription());
        saveTask.setStatus(task.getStatus());
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask saveSubtask = subtasks.get(subtask.getId());
        long checkEpicId = subtask.getEpicId();
        if (saveSubtask == null || (subtask.getEpicId() != checkEpicId)) {
            return;
        }
        saveSubtask.setName(subtask.getName());
        saveSubtask.setDescription(subtask.getDescription());
        saveSubtask.setStatus(subtask.getStatus());
        updateStatusEpic(subtask.getEpicId());
    }

    @Override
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
            epic.setStatus(Status.NEW);
            return;
        }

        Status status = null;
        for (long subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);

            if (status == null) {
                status = subtask.getStatus();
                continue;
            }

            if (status.equals(subtask.getStatus()) && !status.equals(Status.IN_PROGRESS)) {
                continue;
            }

            epic.setStatus(Status.IN_PROGRESS);
            return;
        }
        epic.setStatus(status);
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    private long generateId() {
        return generatorId++;
    }

    @Override
    public void removeTasks() {
        for (Long id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void removeSubtasks() {
        for (Long id : epics.keySet()) {
            epics.get(id).clears();
        }
        for (Long id : subtasks.keySet()) {
            updateStatusEpic(subtasks.get(id).getEpicId());
        }
        for(Long id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        subtasks.clear();
    }

    @Override
    public void removeEpics() {
        for (Long id : epics.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Task getTaskById(long taskId) {
        historyManager.addTask(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public Subtask getSubtaskById(long subtaskId) {
        historyManager.addTask(subtasks.get(subtaskId));
        return subtasks.get(subtaskId);
    }

    @Override
    public Epic getEpicById(long epicId) {
        historyManager.addTask(epics.get(epicId));
        return epics.get(epicId);
    }

    @Override
    public void removeTaskById(long taskId) {
        historyManager.remove(taskId);
        tasks.remove(taskId);
    }

    @Override
    public void removeSubtaskById(long subtaskId) {
        Epic epic = epics.get(subtasks.get(subtaskId).getEpicId());
        if (epic == null) {
            return;
        }
        ArrayList<Long> subtaskIds = epic.getSubtasksIds();
        for (int i = 0; i < subtaskIds.size(); i++) {
            if (subtaskIds.get(i) == subtaskId) {
                subtaskIds.remove(i);
                break;
            }
        }
        updateStatusEpic(subtasks.get(subtaskId).getEpicId());
        historyManager.remove(subtaskId);
        subtasks.remove(subtaskId);
    }

    @Override
    public void removeEpicById(long epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Long> subtaskIds = epic.getSubtasksIds();
        for (Long id : subtaskIds) {
            historyManager.remove(id);
            subtasks.remove(id);
        }
        historyManager.remove(epicId);
        epics.remove(epicId);
    }

    @Override
    public List<Subtask> getAllSubtasksOfEpic(long epicId) {
        List<Subtask> subtasksOfEpicId = new ArrayList<>();
        for (Long id : subtasks.keySet()) {
            if (subtasks.get(id).getEpicId() == epicId) {
                subtasksOfEpicId.add(subtasks.get(id));
            }
        }
        return subtasksOfEpicId;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}