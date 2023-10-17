package ru.practicum.tracker.manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.practicum.tracker.tasks.models.Status;
import ru.practicum.tracker.tasks.Task;

import java.time.LocalDateTime;

public class InMemoryTaskManagerTest extends TaskManagerTest {
    @Test
    public void testGenerateId() {
        Task task = new Task("test", "test disc", Status.IN_PROGRESS,
                LocalDateTime.of(2023, 10, 9, 10, 0), 90L);
        final long taskId = taskManager.addNewTask(task);
        assertEquals(0, taskId);
        Task task1 = new Task("test", "test disc", Status.IN_PROGRESS,
                LocalDateTime.of(2023, 10, 9, 20, 0), 90L);
        final long taskId1 = taskManager.addNewTask(task1);
        assertEquals(1, taskId1);
    }

}
