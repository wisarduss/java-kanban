package ru.practicum.tracker.manager;

import ru.practicum.tracker.exception.TaskValidateException;
import ru.practicum.tracker.tasks.Epic;
import ru.practicum.tracker.tasks.models.Status;
import ru.practicum.tracker.tasks.Subtask;
import ru.practicum.tracker.tasks.Task;
import ru.practicum.tracker.tasks.models.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.List;
import java.util.Collections;

public class InMemoryTaskManager implements TaskManager {
    private long generatorId = 0;
    protected final HashMap<Long, Epic> epics = new HashMap<>();
    protected final HashMap<Long, Task> tasks = new HashMap<>();
    protected final HashMap<Long, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.nullsLast((o1, o2) -> {
        if (o1.getStartTime() != null && o2.getStartTime() != null) {
            return o1.getStartTime().compareTo(o2.getStartTime());
        } else if (o1 == o2) {
            return 0;
        } else {
            return 1;
        }
    }));

    @Override
    public void calculateEndTimeForEpic(long epicId) {
        for (Epic epic : epics.values()) {
            if (epic.getId() == epicId) {
                calculateStartTimeForEpic(epic.getId());
                if (epic.getStartTime() == null || epic.getDuration() == null) {
                    return;
                }
                epic.setEndTime(epic.getStartTime().plusMinutes(epic.getDuration()));
            }
        }
    }

    @Override
    public void calculateStartTimeForEpic(long epicId) {
        List<LocalDateTime> localDateTimes = new ArrayList<>();
        if (epics.isEmpty()) {
            return;
        }
        for (Epic epic : epics.values()) {
            if (epic.getId() == epicId) {
                List<Subtask> subtasksOfEpic = getAllSubtasksOfEpic(epicId);
                if (subtasksOfEpic.isEmpty()) {
                    return;
                }
                for (Subtask subtask : subtasksOfEpic) {
                    if (subtask.getStartTime() == null) {
                        continue;
                    }
                    localDateTimes.add(subtask.getStartTime());
                    LocalDateTime minTime = Collections.min(localDateTimes);
                    epic.setStartTime(minTime);
                }
            }
        }
    }

    @Override
    public void calculateDurationForEpic(long epicId) {
        long duration = 0;
        if (epics.isEmpty()) {
            return;
        }
        for (Epic epic : epics.values()) {
            if (epic.getId() == epicId) {
                List<Subtask> subtasksOfEpic = getAllSubtasksOfEpic(epicId);
                if (subtasksOfEpic.isEmpty()) {
                    return;
                }
                for (Subtask subtask : subtasksOfEpic) {
                    if (subtask.getDuration() == null) {
                        epic.setDuration(duration);
                        return;
                    }
                    duration = duration + subtask.getDuration();
                    epic.setDuration(duration);
                }
            }
        }
    }

    @Override
    public long addNewEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public Long addNewSubtask(Subtask subtask) {
        validate(subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return null;
        }
        subtask.setId(generateId());
        subtasks.put(subtask.getId(), subtask);
        epic.addSubtaskId(subtask.getId());
        prioritizedTasks.add(subtask);
        calculateDurationForEpic(subtask.getEpicId());
        calculateStartTimeForEpic(subtask.getEpicId());
        calculateEndTimeForEpic(subtask.getEpicId());
        updateStatusEpic(subtask.getEpicId());
        return subtask.getId();
    }

    @Override
    public Long addNewTask(Task task) {
        validate(task);
        task.setId(generateId());
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
        return task.getId();
    }

    @Override
    public void updateTask(Task task) {
        validate(task);
        Task saveTask = tasks.get(task.getId());
        if (saveTask == null) {
            return;
        }
        saveTask.setName(task.getName());
        saveTask.setDescription(task.getDescription());
        saveTask.setStatus(task.getStatus());
        prioritizedTasks.remove(task);
        prioritizedTasks.add(saveTask);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        validate(subtask);
        Subtask saveSubtask = subtasks.get(subtask.getId());
        long checkEpicId = subtask.getEpicId();
        if (saveSubtask == null || (subtask.getEpicId() != checkEpicId)) {
            return;
        }
        saveSubtask.setName(subtask.getName());
        saveSubtask.setDescription(subtask.getDescription());
        saveSubtask.setStatus(subtask.getStatus());
        saveSubtask.setStartTime(subtask.getStartTime());
        saveSubtask.setDuration(subtask.getDuration());
        calculateStartTimeForEpic(subtask.getEpicId());
        calculateEndTimeForEpic(subtask.getEpicId());
        updateStatusEpic(subtask.getEpicId());
        prioritizedTasks.remove(subtask);
        prioritizedTasks.add(saveSubtask);
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

    private long generateId() {
        return generatorId++;
    }

    @Override
    public void removeTasks() {
        for (Long id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
        prioritizedTasks.removeIf(task -> task.getType() == TaskType.TASK);
    }

    @Override
    public void removeSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clears();
            calculateStartTimeForEpic(epic.getId());
            calculateDurationForEpic(epic.getId());
            calculateEndTimeForEpic(epic.getId());
            updateStatusEpic(epic.getId());
        }
        for (Long id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        prioritizedTasks.removeIf(subtasks -> subtasks.getType() == TaskType.SUBTASK);
        subtasks.clear();
    }

    @Override
    public void removeEpics() {
        for (Long id : epics.keySet()) {
            historyManager.remove(id);
        }
        for (Long id : subtasks.keySet()) {
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
        if (subtasks.get(subtaskId) != null) {
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
            calculateStartTimeForEpic(subtasks.get(subtaskId).getEpicId());
            calculateDurationForEpic(subtasks.get(subtaskId).getEpicId());
            calculateEndTimeForEpic(subtasks.get(subtaskId).getEpicId());
            updateStatusEpic(subtasks.get(subtaskId).getEpicId());
            historyManager.remove(subtaskId);
            subtasks.remove(subtaskId);
        }
    }

    @Override
    public void removeEpicById(long epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            ArrayList<Long> subtaskIds = epic.getSubtasksIds();
            if (subtaskIds != null) {
                for (Long id : subtaskIds) {
                    historyManager.remove(id);
                    subtasks.remove(id);
                }
            }
            historyManager.remove(epicId);
            epics.remove(epicId);
        }
    }

    @Override
    public List<Subtask> getAllSubtasksOfEpic(long epicId) {
        List<Subtask> subtasksOfEpicId = new ArrayList<>();
        if (epics.get(epicId) != null) {
            for (Long id : subtasks.keySet()) {
                if (subtasks.get(id).getEpicId() == epicId) {
                    subtasksOfEpicId.add(subtasks.get(id));
                }
            }
        }
        return subtasksOfEpicId;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    private void validate(Task task) {
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();

        Integer result = prioritizedTasks.stream()
                .map(it -> {
                    if (startTime.isBefore(it.getEndTime()) && endTime.isAfter(it.getStartTime())) {
                        return 1;
                    }
                    if (startTime.isBefore(it.getEndTime()) && endTime.isAfter(it.getEndTime())) {
                        return 1;
                    }
                    return 0;
                })
                .reduce(Integer::sum)
                .orElse(0);
        if (result > 0) {
            throw new TaskValidateException("Задача пересекается");
        }
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }
}