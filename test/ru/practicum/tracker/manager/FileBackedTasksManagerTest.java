package ru.practicum.tracker.manager;

import org.junit.jupiter.api.Test;

import ru.practicum.tracker.exception.ManagerSaveException;
import ru.practicum.tracker.exception.TaskValidateException;
import ru.practicum.tracker.tasks.Epic;
import ru.practicum.tracker.tasks.models.Status;
import ru.practicum.tracker.tasks.Subtask;
import ru.practicum.tracker.tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File("tasks.txt"));

    @Test
    public void testSave() {
        Task task = new Task("test", "test disc", Status.NEW,
                LocalDateTime.of(2023, 10, 8, 10, 0), 180L);
        final long taskId = fileBackedTasksManager.addNewTask(task);
        fileBackedTasksManager.getTaskById(taskId);
        List<String> lines = readLinesFromFile(new File("tasks.txt"));
        List<String> expectedLines = Arrays.asList("id,type,name,status,description,startTime,duration,epic",
                "0,TASK,test,NEW,test disc,2023-10-08T10:00,180,",
                "",
                "0,");
        assertEquals(expectedLines, lines);
    }

    @Test
    public void testLoadFromFile() {
        Epic epic1 = new Epic("Переезд", "Нужно сделать все необходимое для переезда");
            long epicId1 = fileBackedTasksManager.addNewEpic(epic1);

        Subtask subtask1 = new Subtask("Собрать коробки", "Положить в них все вещи для переезда ",
                Status.IN_PROGRESS, epicId1,
                LocalDateTime.of(2024, 10, 9, 10, 0), 90L);
        Long subtask1Id = fileBackedTasksManager.addNewSubtask(subtask1);

        Task task1 = new Task("погулять", "заплывешь жиром", Status.NEW,
                LocalDateTime.of(2023, 10, 8, 10, 0), 180L);
        Long task1Id = fileBackedTasksManager.addNewTask(task1);

        Task task2 = new Task("погулять", "заплывешь жиром", Status.NEW,
                LocalDateTime.of(2023, 10, 8, 10, 10), 180L);
        TaskValidateException exception = assertThrows(
                TaskValidateException.class,
                () -> fileBackedTasksManager.addNewTask(task2)
        );
        assertEquals("Задача пересекается",exception.getMessage());


        fileBackedTasksManager.getEpicById(epicId1);
        fileBackedTasksManager.getSubtaskById(subtask1Id);
        fileBackedTasksManager.getTaskById(task1Id);


        FileBackedTasksManager loadedManager = FileBackedTasksManager.loadFromFile(new File("tasks.txt"));


        assertEquals(fileBackedTasksManager.getTasks(), loadedManager.getTasks(), "Список задач не совпадает");
        assertEquals(fileBackedTasksManager.getSubtasks(), loadedManager.getSubtasks(), "Список задач не совпадает");
        assertEquals(fileBackedTasksManager.getEpics(), loadedManager.getEpics(), "Список задач не совпадает");
        assertEquals(fileBackedTasksManager.getHistory(), loadedManager.getHistory(), "Список истории не совпадает");
    }

    @Test
    public void testLoadFromFileWithEmptyHistory() {
        Task task = new Task("test", "test disc", Status.IN_PROGRESS,
                LocalDateTime.of(2023, 10, 8, 10, 0), 180L);
        Long taskId = fileBackedTasksManager.addNewTask(task);
        FileBackedTasksManager loadedManager = FileBackedTasksManager.loadFromFile(new File("tasks.txt"));
        assertEquals(fileBackedTasksManager.getHistory(), loadedManager.getHistory());

    }

    @Test
    public void testLoadFromFileWithEmptyTasks() {
        fileBackedTasksManager.save();
        FileBackedTasksManager loadedManager = FileBackedTasksManager.loadFromFile(new File("tasks.txt"));
        assertEquals(fileBackedTasksManager.getTasks(), loadedManager.getTasks());
    }

    @Test
    public void testLoadFromFileWithEpicWithoutSubtasks() {
        Epic epic = new Epic("test", "test disc");
        final long epicId = fileBackedTasksManager.addNewEpic(epic);
        taskManager.calculateStartTimeForEpic(epicId);
        FileBackedTasksManager loadedManager = FileBackedTasksManager.loadFromFile(new File("tasks.txt"));
        assertEquals(fileBackedTasksManager.getEpics() , loadedManager.getEpics());
    }


    private List<String> readLinesFromFile(File file) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла", e);
        }
        return lines;
    }

}
