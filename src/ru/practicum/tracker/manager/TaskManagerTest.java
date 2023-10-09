package ru.practicum.tracker.manager;

import org.junit.jupiter.api.Test;
import ru.practicum.tracker.tasks.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class TaskManagerTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    public void shouldReturnNewStatusEpicWithoutSubtask() {
        Epic epic = new Epic("Test Epic", "Test Epic disc");
        final long epicId = taskManager.addNewEpic(epic);
        assertEquals("NEW", epic.getStatus().toString(), "Неверный статус Epic");
    }

    @Test
    public void shouldReturnNewStatusEpicWithStatusSubtaskNew() {
        Epic epic = new Epic("Test Epic", "Test Epic disc");
        final long epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Test subtask", "Test subtask disc", Status.NEW, epicId,
                LocalDateTime.of(2024, 10, 9, 10, 0), 90L);
        final long subtaskId1 = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test subtask", "Test subtask disc", Status.NEW, epicId,
                LocalDateTime.of(2024, 10, 3, 10, 0), 90L);
        final long subtaskId2 = taskManager.addNewSubtask(subtask2);
        assertEquals("NEW", epic.getStatus().toString(), "Неверный статус Epic");
        assertEquals(epicId, subtask1.getEpicId());
        assertEquals(epicId, subtask2.getEpicId());
    }

    @Test
    public void shouldReturnDoneStatusEpicWithStatusSubtaskDone() {
        Epic epic = new Epic("Test Epic", "Test Epic disc");
        final long epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Test subtask", "Test subtask disc", Status.DONE, epicId,
                LocalDateTime.of(2024, 10, 9, 10, 0), 90L);
        final long subtaskId1 = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test subtask", "Test subtask disc", Status.DONE, epicId,
                LocalDateTime.of(2024, 10, 9, 11, 31), 90L);
        final long subtaskId2 = taskManager.addNewSubtask(subtask2);
        assertEquals("DONE", epic.getStatus().toString(), "Неверный статус Epic");
        assertEquals(epicId, subtask1.getEpicId());
        assertEquals(epicId, subtask2.getEpicId());
        assertEquals("2024-10-09T10:00", epic.getStartTime().toString());
    }

    @Test
    public void shouldReturnInProgressStatusEpicWithStatusSubtaskNewAndDone() {
        Epic epic = new Epic("Test Epic", "Test Epic disc");
        final long epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Test subtask", "Test subtask disc", Status.DONE, epicId,
                LocalDateTime.of(2024, 10, 9, 10, 0), 90L);
        final long subtaskId1 = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test subtask", "Test subtask disc", Status.NEW, epicId,
                LocalDateTime.of(2024, 10, 9, 22, 0), 90L);
        final long subtaskId2 = taskManager.addNewSubtask(subtask2);
        assertEquals("IN_PROGRESS", epic.getStatus().toString(), "Неверный статус Epic");
        assertEquals(epicId, subtask1.getEpicId());
        assertEquals(epicId, subtask2.getEpicId());
    }

    @Test
    public void shouldReturnInProgressStatusEpicWithStatusSubtaskInProgress() {
        Epic epic = new Epic("Test Epic", "Test Epic disc");
        final long epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Test subtask", "Test subtask disc", Status.IN_PROGRESS, epicId,
                LocalDateTime.of(2024, 10, 9, 10, 0), 90L);
        final long subtaskId1 = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test subtask", "Test subtask disc", Status.IN_PROGRESS, epicId,
                LocalDateTime.of(2024, 10, 10, 10, 0), 90L);
        final long subtaskId2 = taskManager.addNewSubtask(subtask2);
        assertEquals("IN_PROGRESS", epic.getStatus().toString(), "Неверный статус Epic");
        assertEquals(epicId, subtask1.getEpicId());
        assertEquals(epicId, subtask2.getEpicId());
    }

    @Test
    public void testAddNewEpic() {
        Epic epic = new Epic("Test Epic", "Test Epic disc");
        final long epicId = taskManager.addNewEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epicId);
        assertNotNull(savedEpic, "Задача не найдена");
        assertEquals(epic, savedEpic, "Задачи не свопадают");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаюстя");
        assertEquals(1, epics.size(), "Неверное количество задач");
        assertEquals(epic, epics.get(0), "Задачи не свопадают");
    }

    @Test
    public void testAddNewTask() {
        Task task = new Task("Test task", "Test task disc", Status.NEW);
        final long taskId = taskManager.addNewTask(task);
        final Task savedTask = taskManager.getTaskById(taskId);
        assertNotNull(savedTask, "Здача не найдена");
        assertEquals(task, savedTask, "Задачи не свопадают");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertEquals(task, tasks.get(0), "Задачи не свопдают");
    }

    @Test
    public void testAddNewSubtask() {
        Epic epic = new Epic("Test Epic", "Test Epic disc");
        final long epicId = taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Test subtask", "Test subtask disc", Status.NEW, epicId);
        final long subtaskId = taskManager.addNewSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);
        assertNotNull(savedSubtask, "Задача не найдена");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают");

        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Задачи не возвращаются");
        assertEquals(1, subtasks.size(), "Неверное количество задач");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают");
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task("Test task", "Test task disc", Status.NEW,
                LocalDateTime.of(2024, 10, 9, 10, 0), 90L);
        final long taskId = taskManager.addNewTask(task);
        Task taskToUpdate = new Task("new Test", "new Test disc", taskId, Status.IN_PROGRESS,
                LocalDateTime.of(2025, 10, 9, 10, 0), 90L);
        taskManager.updateTask(taskToUpdate);
        final Task savedTask = taskManager.getTaskById(taskId);
        assertEquals(taskToUpdate, savedTask, "Задачи не совпадают");
    }

    @Test
    public void testUpdateSubtask() {
        Epic epic = new Epic("Test Epic", "Test Epic disc");
        final long epicId = taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Test subtask", "Test subtask disc", Status.NEW, epicId,
                LocalDateTime.of(2024, 10, 9, 10, 0), 90L);
        final long subtaskId = taskManager.addNewSubtask(subtask);
        Subtask subtaskToUpdate = new Subtask("new Test subtask",
                "new Test subtask disc", subtaskId, Status.IN_PROGRESS,
                epicId, LocalDateTime.of(2024, 10, 9, 20, 0), 90L);
        taskManager.updateSubtask(subtaskToUpdate);
        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);
        assertEquals(subtaskToUpdate, savedSubtask, "Подзадачи не совпадают");
    }

    @Test
    public void testUpdateEpic() {
        Epic epic = new Epic("Test Epic", "Test Epic disc");
        final long epicId = taskManager.addNewEpic(epic);
        final Epic epicToUpdate = new Epic("new test epic", "new test disc", epicId);
        taskManager.updateEpic(epicToUpdate);
        final Epic savedEpic = taskManager.getEpicById(epicId);
        assertEquals(epicToUpdate, savedEpic, "Задачи не совпадают");
    }

    @Test
    public void testRemoveTasksWithTask() {
        Task task = new Task("Test task", "Test task disc", Status.NEW);
        final long taskId = taskManager.addNewTask(task);
        taskManager.removeTasks();
        List<Task> tasks = new ArrayList<>();
        assertEquals(tasks, taskManager.getTasks(), "Список задач не совпадает");
    }

    @Test
    public void testRemoveTasksWithEmptyId() {
        Task task = new Task("Test task", "Test task disc", Status.NEW);
        taskManager.removeTasks();
        List<Task> tasks = new ArrayList<>();
        assertEquals(tasks, taskManager.getTasks(), "Список задач не совпадает");
    }

    @Test
    public void testRemoveTasksWithoutTask() {
        List<Task> tasks = new ArrayList<>();
        taskManager.removeTasks();
        assertEquals(tasks, taskManager.getTasks(), "Список задач не совпадает");
    }

    @Test
    public void testRemoveSubtasksWithSubtasks() {
        Epic epic = new Epic("Test epic", "test epic disc");
        final long epicId = taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Test task", "Test task disc", Status.NEW, epicId);
        final long subtaskId = taskManager.addNewSubtask(subtask);
        taskManager.removeEpics();
        List<Subtask> subtasks = new ArrayList<>();
        assertEquals(subtasks, taskManager.getSubtasks(), "Список задач не совпадает");
    }

    @Test
    public void testRemoveSubtasksWithEmptyId() {
        Epic epic = new Epic("Test epic", "test epic disc");
        final long epicId = taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Test task", "Test task disc", Status.NEW, epicId);
        taskManager.removeSubtasks();
        List<Subtask> subtasks = new ArrayList<>();
        assertEquals(subtasks, taskManager.getSubtasks(), "Список задач не совпадает");
    }

    @Test
    public void testRemoveSubtasksWithoutSubtasks() {
        List<Subtask> subtasks = new ArrayList<>();
        taskManager.removeSubtasks();
        assertEquals(subtasks, taskManager.getSubtasks(), "Список задач не совпадает");
    }

    @Test
    public void testRemoveEpicWithEpic() {
        Epic epic = new Epic("Test epic", "test epic disc");
        final long epicId = taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("new test subtask", "new test subtask disc", Status.IN_PROGRESS, epicId);
        final long subtaskId = taskManager.addNewSubtask(subtask);
        taskManager.removeEpics();
        List<Epic> epics = new ArrayList<>();
        List<Subtask> subtasks = new ArrayList<>();
        assertEquals(epics, taskManager.getEpics(), "Список задач не совпадает");
        assertEquals(subtasks, taskManager.getSubtasks());
    }

    @Test
    public void testRemoveEpicWithoutEpic() {
        taskManager.removeEpics();
        List<Epic> epics = new ArrayList<>();
        assertEquals(epics, taskManager.getEpics());
    }

    @Test
    public void testRemoveEpicWithEmptyId() {
        Epic epic = new Epic("Test epic", "test epic disc");
        taskManager.removeEpics();
        List<Epic> epics = new ArrayList<>();
        assertEquals(epics, taskManager.getEpics());
    }

    @Test
    public void testGetTaskByIdWithNormalBehaviour() {
        Task task = new Task("Test task", "Test task disc", Status.NEW);
        final long taskId = taskManager.addNewTask(task);
        final Task savedTask = taskManager.getTaskById(taskId);
        assertEquals(task, savedTask);
    }

    @Test
    public void testGetTaskByIdWithEmptyTasks() {
        List<Task> tasks = taskManager.getTasks();
        assertEquals(0, tasks.size());
    }

    @Test
    public void testGetTaskByIdWithIncorrectId() {
        Task task = new Task("Test task", "Test task disc", Status.NEW);
        final long taskId = taskManager.addNewTask(task);
        assertNull(taskManager.getTaskById(2));
    }

    @Test
    public void testGetSubtaskByIdWithNormalBehaviour() {
        Epic epic = new Epic("Test epic", "test epic disc");
        final long epicId = taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Test task", "Test task disc", Status.NEW, epicId);
        final long subtaskId = taskManager.addNewSubtask(subtask);
        assertEquals(subtask, taskManager.getSubtaskById(subtaskId));
    }

    @Test
    public void testGetSubtaskByIdWithEmptySubtasks() {
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(0, subtasks.size());
    }

    @Test
    public void testGetSubtaskByIdWithIncorrectId() {
        Epic epic = new Epic("Test epic", "test epic disc");
        final long epicId = taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("Test task", "Test task disc", Status.NEW, epicId);
        final long subtaskId = taskManager.addNewSubtask(subtask);
        assertNull(taskManager.getSubtaskById(123));
    }

    @Test
    public void testGetEpicByIdWithNormalBehaviour() {
        Epic epic = new Epic("test epic", "test epic disc");
        final long epicId = taskManager.addNewEpic(epic);
        assertEquals(epic, taskManager.getEpicById(epicId));
    }

    @Test
    public void testGetEpicByIdWithEmptyEpics() {
        List<Epic> epics = taskManager.getEpics();
        assertEquals(0, epics.size());
    }

    @Test
    public void testGetEpicByIdWithIncorrectId() {
        Epic epic = new Epic("test epic", "test epic disc");
        final long epicId = taskManager.addNewEpic(epic);
        assertNull(taskManager.getEpicById(544));
    }

    @Test
    public void testRemoveTaskByIdWithNormalBehaviour() {
        Task task1 = new Task("Test task", "test task disc", Status.IN_PROGRESS,
                LocalDateTime.of(2023, 10, 8, 10, 0), 180L);
        final long taskId1 = taskManager.addNewTask(task1);
        Task task2 = new Task("213213", "1231231", Status.IN_PROGRESS,
                LocalDateTime.of(2024, 10, 8, 10, 0), 180L);
        final long taskId2 = taskManager.addNewTask(task2);
        taskManager.removeTaskById(taskId1);
        List<Task> tasks = taskManager.getTasks();
        assertEquals("[" + task2 + "]", tasks.toString());
    }

    @Test
    public void testRemoveTaskByIdWithEmptyTask() {
        List<Task> tasks = taskManager.getTasks();
        assertEquals(0, tasks.size());
    }

    @Test
    public void testRemoveTaskByIdWithIncorrectId() {
        Task task1 = new Task("Test task", "test task disc", Status.IN_PROGRESS);
        final long taskId1 = taskManager.addNewTask(task1);
        taskManager.removeTaskById(1231);
        assertEquals("[" + task1 + "]", taskManager.getTasks().toString());
    }

    @Test
    public void testRemoveSubtaskByIdWithNormalBehaviour() {
        Epic epic = new Epic("test", "test disc");
        final long epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("test ", "test disc", Status.IN_PROGRESS, epicId,
                LocalDateTime.of(2024, 10, 9, 10, 0), 90L);
        final long subtaskId1 = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("test ", "test disc", Status.IN_PROGRESS, epicId,
                LocalDateTime.of(2024, 10, 9, 20, 0), 90L);
        final long subtaskId2 = taskManager.addNewSubtask(subtask2);
        taskManager.removeSubtaskById(subtaskId1);
        assertEquals("[" + subtask2 + "]", taskManager.getSubtasks().toString());
    }

    @Test
    public void testRemoveSubtaskByIdWithEmptySubtasks() {
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(0, subtasks.size());
    }

    @Test
    public void testRemoveSubtaskByIdWithIncorrectId() {
        Epic epic = new Epic("test", "test disc");
        final long epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("test ", "test disc", Status.IN_PROGRESS, epicId);
        final long subtaskId1 = taskManager.addNewSubtask(subtask1);
        taskManager.removeSubtaskById(1231);
        assertEquals("[" + subtask1 + "]", taskManager.getSubtasks().toString());
    }

    @Test
    public void testRemoveEpicByIdWithNormalBehaviour() {
        Epic epic1 = new Epic("test", "test disc");
        final long epicId1 = taskManager.addNewEpic(epic1);
        Epic epic2 = new Epic("test", "test disc");
        final long epicId2 = taskManager.addNewEpic(epic2);
        taskManager.removeEpicById(epicId1);
        assertEquals("[" + epic2 + "]", taskManager.getEpics().toString());
    }

    @Test
    public void testRemoveEpicByIdWithEmptyEpic() {
        List<Epic> epics = taskManager.getEpics();
        assertEquals(0, epics.size());
    }

    @Test
    public void testRemoveEpicByIdWithIncorrectId() {
        Epic epic = new Epic("Test", "test disc");
        final long epicId = taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("test", "test disc", Status.IN_PROGRESS, epicId);
        final long subtaskId = taskManager.addNewSubtask(subtask);
        taskManager.removeEpicById(12321);
        assertEquals("[" + epic + "]", taskManager.getEpics().toString());
    }

    @Test
    public void testGetAllSubtaskOfEpicWithNormalBehaviour() {
        Epic epic = new Epic("Test", "test disc");
        final long epicId = taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("test", "test disc", Status.IN_PROGRESS, epicId);
        final long subtaskId = taskManager.addNewSubtask(subtask);
        assertEquals("[" + subtask + "]", taskManager.getAllSubtasksOfEpic(epicId).toString());
        assertEquals(1, taskManager.getAllSubtasksOfEpic(epicId).size());
    }

    @Test
    public void testGetAllSubtaskOfEpicWithEmptySubtask() {
        Epic epic = new Epic("Test", "test disc");
        final long epicId = taskManager.addNewEpic(epic);
        assertEquals("[]", taskManager.getAllSubtasksOfEpic(epicId).toString());
    }

    @Test
    public void testGetAllSubtaskOfEpicWithEmptyEpicAndSubtask() {
        assertEquals("[]", taskManager.getAllSubtasksOfEpic(123).toString());
    }

    @Test
    public void testGetAllSubtaskOfEpicWithIncorrectId() {
        Epic epic = new Epic("Test", "test disc");
        final long epicId = taskManager.addNewEpic(epic);
        Subtask subtask = new Subtask("test", "test disc", Status.IN_PROGRESS, epicId);
        final long subtaskId = taskManager.addNewSubtask(subtask);
        assertEquals("[]", taskManager.getAllSubtasksOfEpic(12).toString());
    }

    @Test
    public void testGetHistoryWithNormalBehaviour() {
        Epic epic = new Epic("test", "test disc");
        final long epicId = taskManager.addNewEpic(epic);
        taskManager.getEpicById(epicId);
        assertEquals(1, taskManager.getHistory().size());
        assertEquals("[" + epic + "]", taskManager.getHistory().toString());
    }

    @Test
    public void testGetHistoryWithEmptyHistory() {
        assertEquals(0, taskManager.getHistory().size());
        assertEquals("[]", taskManager.getHistory().toString());
    }

    @Test
    public void testGetTasksWithNormalBehaviour() {
        Task task1 = new Task("test", "test disc", Status.IN_PROGRESS,
                LocalDateTime.of(2023, 10, 9, 10, 0), 90L);
        final long taskId1 = taskManager.addNewTask(task1);
        Task task2 = new Task("test", "test disc", Status.IN_PROGRESS,
                LocalDateTime.of(2024, 10, 10, 10, 0), 90L);
        final long taskId2 = taskManager.addNewTask(task2);
        Task task3 = new Task("test", "test disc", Status.IN_PROGRESS,
                LocalDateTime.of(2024, 10, 11, 10, 0), 90L);
        final long taskId3 = taskManager.addNewTask(task3);
        List<Task> tasks = List.of(task1, task2, task3);
        assertEquals(3, taskManager.getTasks().size());
        assertEquals(tasks, taskManager.getTasks());
    }

    @Test
    public void testGetTasksWithEmptyTaskList() {
        List<Task> tasks = new ArrayList<>();
        assertEquals(tasks, taskManager.getTasks());
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    public void testGetSubtasksWithNormalBehaviour() {
        Epic epic = new Epic("test", "test disc");
        final long epicId = taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("test", "test disc", Status.IN_PROGRESS, epicId,
                LocalDateTime.of(2024, 10, 9, 10, 0), 90L);
        final long subtaskId1 = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("test", "test disc", Status.IN_PROGRESS, epicId,
                LocalDateTime.of(2024, 10, 9, 15, 0), 90L);
        final long subtaskId2 = taskManager.addNewSubtask(subtask2);
        Subtask subtask3 = new Subtask("test", "test disc", Status.IN_PROGRESS, epicId,
                LocalDateTime.of(2024, 10, 9, 20, 0), 90L);
        final long subtaskId3 = taskManager.addNewSubtask(subtask3);
        List<Subtask> subtasks = List.of(subtask1, subtask2, subtask3);
        assertEquals(subtasks, taskManager.getSubtasks());
        assertEquals(3, taskManager.getSubtasks().size());
    }

    @Test
    public void testGetSubtaskWithEmptySubtaskList() {
        List<Subtask> subtasks = new ArrayList<>();
        assertEquals(subtasks, taskManager.getSubtasks());
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    public void testGetEpicsWithNormalBehaviour() {
        Epic epic1 = new Epic("test", "test disc");
        final long epicId1 = taskManager.addNewEpic(epic1);
        Epic epic2 = new Epic("test", "test disc");
        final long epicId2 = taskManager.addNewEpic(epic2);
        Epic epic3 = new Epic("test", "test disc");
        final long epicId3 = taskManager.addNewEpic(epic3);
        List<Epic> epics = List.of(epic1, epic2, epic3);
        assertEquals(epics, taskManager.getEpics());
        assertEquals(3, taskManager.getEpics().size());
    }

    @Test
    public void testGetEpicsWithEmptyEpicsList() {
        List<Epic> epics = new ArrayList<>();
        assertEquals(epics, taskManager.getEpics());
        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    public void testGetHistoryManager() {
        assertNotNull(taskManager.getHistoryManager());
    }
}
