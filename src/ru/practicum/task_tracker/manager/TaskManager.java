package ru.practicum.task_tracker.manager;

import org.w3c.dom.ls.LSOutput;
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

    public void updateTask(Task task, long taskId) {
        Task saveTask = tasks.get(taskId);
        if (saveTask == null) {
            return;
        }
        tasks.put(taskId, task);
    }

    public void updateSubtask(Subtask subtask, long subtaskId) {
        Subtask saveSubtask = subtasks.get(subtaskId);
        long checkEpicId = subtask.getEpicId();
        if (saveSubtask == null || (subtask.getEpicId() != checkEpicId)) {
            return;
        }

        subtasks.put(subtaskId, subtask);
        updateStatusEpic(subtask.getEpicId());
    }

    public void updateEpic(Epic epic, long epicId) {
        Epic saveEpic = epics.get(epicId);
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

    public void printAllTasks() {
        printEpics();
        System.out.println();
        printSubtasks();
        System.out.println();
        printTasks();
    }

    private long generateId() {
        return generatorId++;
    }


    private void printEpics() {
        System.out.println("все эпики");
        for (Long id : epics.keySet()) {
            System.out.println(epics.get(id));
        }
    }

    private void printSubtasks() {
        System.out.println("все подзадачи для эпиков");
        for (Long id : subtasks.keySet()) {
            System.out.println(subtasks.get(id));
        }
    }

    private void printTasks() {
        System.out.println("Все задачи");
        for (Long id : tasks.keySet()) {
            System.out.println(tasks.get(id));
        }
    }

    public void removeTasks() {
        tasks.clear();
        System.out.println("Все задачи удалены");
    }

    public void removeSubtasks() {
        for (Long id : epics.keySet()) {
            epics.get(id).clears();
        }
        for (Long id : subtasks.keySet()) {
            updateStatusEpic(subtasks.get(id).getEpicId());
        }
        subtasks.clear();
        System.out.println("Все подзадачи удалены");
    }

    public void removeEpics() {
        epics.clear();
        subtasks.clear();
        System.out.println("Все эпики удалены");
    }

    public void getTaskById(long taskId) {
        if (!tasks.containsKey(taskId)) {
            System.out.println("Задачи с id=" + taskId + " не существует");
            return;
        }
        for (Long id : tasks.keySet()) {
            if (id.equals(taskId)) {
                System.out.println("Задача с таким id=" + id + " найдена");
                System.out.println(tasks.get(id));
            }
        }
    }

    public void getSubtaskById(long subtaskId) {
        if (!subtasks.containsKey(subtaskId)) {
            System.out.println("Подзадачи с id=" + subtaskId + " не существует");
            return;
        }
        for (Long id : subtasks.keySet()) {
            if (id.equals(subtaskId)) {
                System.out.println("Подзадача с таким id=" + id + " найдена");
                System.out.println(subtasks.get(id));
            }
        }
    }

    public void getEpicById(long epicId) {
        if (!epics.containsKey(epicId)) {
            System.out.println("Эпик с id=" + epicId + " не существует");
            return;
        }
        for (Long id : epics.keySet()) {
            if (id.equals(epicId)) {
                System.out.println("Эпик с таким id=" + epicId + " найден");
                System.out.println(epics.get(id));
            }
        }
    }

    public void removeTaskById(long taskId) {
        if (!tasks.containsKey(taskId)) {
            System.out.println("Задачи с id=" + taskId + " нет");
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

    public void getAllSubtasksOfEpic(long epicId) {
        for (Long id : epics.keySet()) {
            if (id.equals(epicId)) {
                System.out.println(epics.get(id));
            }
        }
        for (Long id : subtasks.keySet()) {
            if (subtasks.get(id).getEpicId() == epicId) {
                System.out.println(subtasks.get(id));
            }
        }
    }

}
