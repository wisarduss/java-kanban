package ru.practicum.tracker.manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import ru.practicum.tracker.tasks.Status;
import ru.practicum.tracker.tasks.Task;

import java.time.LocalDateTime;
import java.util.List;

public class InMemoryHistoryManagerTest {
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void testAddTask() {
        Task task = new Task("test", "test disc", Status.IN_PROGRESS);
        final long taskId = taskManager.addNewTask(task);
        historyManager.addTask(task);
        final List<Task> history = historyManager.getHistory();
        assertEquals("[" + task + "]", history.toString());
        assertEquals(1, history.size(), "История пустая");
    }

    @Test
    public void testAddTaskWithEmptyHistory() {
        final List<Task> history = historyManager.getHistory();
        assertEquals("[]", history.toString());
        assertEquals(0, history.size());
    }

    @Test
    public void testAddTaskWithDuplicate() {
        Task task = new Task("test", "test disc", Status.IN_PROGRESS);
        final long taskId = taskManager.addNewTask(task);
        historyManager.addTask(task);
        historyManager.addTask(task);
        historyManager.addTask(task);
        historyManager.addTask(task);
        historyManager.addTask(task);
        final List<Task> history = historyManager.getHistory();
        assertEquals("[" + task + "]", history.toString());
        assertEquals(1, history.size(), "История пустая");
    }

    @Test
    public void testRemoveInBeginning() {
        Task task1 = new Task("test", "test disc", Status.IN_PROGRESS,
                LocalDateTime.of(2023, 10, 9, 10, 0), 90L);
        final long taskId1 = taskManager.addNewTask(task1);
        Task task2 = new Task("test", "test disc", Status.IN_PROGRESS,
                LocalDateTime.of(2024, 10, 9, 10, 0), 90L);
        final long taskId2 = taskManager.addNewTask(task2);
        Task task3 = new Task("test", "test disc", Status.IN_PROGRESS,
                LocalDateTime.of(2025, 10, 9, 10, 0), 90L);
        final long taskId3 = taskManager.addNewTask(task3);
        List<Task> tasks = List.of(task2, task3);
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);
        historyManager.remove(taskId1);
        assertEquals(tasks, historyManager.getHistory());
    }

    @Test
    public void testRemoveInMiddle() {
        Task task1 = new Task("test", "test disc", Status.IN_PROGRESS,
                LocalDateTime.of(2023, 10, 9, 10, 0), 90L);
        final long taskId1 = taskManager.addNewTask(task1);
        Task task2 = new Task("test", "test disc", Status.IN_PROGRESS,
                LocalDateTime.of(2024, 10, 9, 10, 0), 90L);
        final long taskId2 = taskManager.addNewTask(task2);
        Task task3 = new Task("test", "test disc", Status.IN_PROGRESS,
                LocalDateTime.of(2025, 10, 9, 10, 0), 90L);
        final long taskId3 = taskManager.addNewTask(task3);
        List<Task> tasks = List.of(task1, task3);
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);
        historyManager.remove(taskId2);
        assertEquals(tasks, historyManager.getHistory());
    }

    @Test
    public void testRemoveInEnd() {
        Task task1 = new Task("test", "test disc", Status.IN_PROGRESS,
                LocalDateTime.of(2023, 10, 9, 10, 0), 90L);
        final long taskId1 = taskManager.addNewTask(task1);
        Task task2 = new Task("test", "test disc", Status.IN_PROGRESS,
                LocalDateTime.of(2024, 10, 9, 10, 0), 90L);
        final long taskId2 = taskManager.addNewTask(task2);
        Task task3 = new Task("test", "test disc", Status.IN_PROGRESS,
                LocalDateTime.of(2025, 10, 9, 10, 0), 90L);
        final long taskId3 = taskManager.addNewTask(task3);
        List<Task> tasks = List.of(task1, task2);
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);
        historyManager.remove(taskId3);
        assertEquals(tasks, historyManager.getHistory());
    }

    @Test
    public void testGetHistory() {
        Task task1 = new Task("test", "test disc", Status.IN_PROGRESS);
        final long taskId1 = taskManager.addNewTask(task1);
        historyManager.addTask(task1);
        List<Task> tasks = List.of(task1);
        assertEquals(tasks, historyManager.getHistory());
    }
}
