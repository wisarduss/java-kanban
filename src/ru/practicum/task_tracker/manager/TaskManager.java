package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {

    long addNewEpic(Epic epic);

    Long addNewSubtask(Subtask subtask);

    Long addNewTask(Task task);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    List<Task> getAllTasks();

    List<Subtask> getAllSubtasks();

    List<Epic> getAllEpics();

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
}