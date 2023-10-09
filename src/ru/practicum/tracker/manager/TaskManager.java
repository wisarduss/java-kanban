package ru.practicum.tracker.manager;

import ru.practicum.tracker.tasks.Epic;
import ru.practicum.tracker.tasks.Subtask;
import ru.practicum.tracker.tasks.Task;

import java.util.List;

public interface TaskManager {

    long addNewEpic(Epic epic);

    Long addNewSubtask(Subtask subtask);

    Long addNewTask(Task task);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void removeTasks();

    void removeSubtasks();

    void removeEpics();

    Task getTaskById(long taskId);

    Subtask getSubtaskById(long subtaskId);

    Epic getEpicById(long epicId);

    void removeTaskById(long taskId);

    void removeSubtaskById(long subtaskId);

    void removeEpicById(long epicId);

    List<Subtask> getAllSubtasksOfEpic(long epicId);

    List<Task> getHistory();

    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    HistoryManager getHistoryManager();

    void calculateStartTimeForEpic(long epicId);

    void calculateDurationForEpic(long epicId);

    void calculateEndTimeForEpic(long epicId);


}