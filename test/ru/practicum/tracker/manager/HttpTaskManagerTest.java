package ru.practicum.tracker.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.tracker.server.KVServer;
import ru.practicum.tracker.tasks.Epic;
import ru.practicum.tracker.tasks.Subtask;
import ru.practicum.tracker.tasks.Task;
import ru.practicum.tracker.tasks.models.Status;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    KVServer server;

    @BeforeEach
    public void start() throws IOException {
        server = new KVServer();
        server.start();
    }

    @AfterEach
    public void stop() {
        server.stop();
    }


    @Test
    public void testLoadFromHttpServer() {
        HttpTaskManager httpTaskManager = new HttpTaskManager();

        Epic epic1 = new Epic("Переезд", "Нужно сделать все необходимое для переезда");
        long epicId1 = httpTaskManager.addNewEpic(epic1);

        Subtask subtask1 = new Subtask("Собрать коробки", "Положить в них все вещи для переезда ",
                Status.IN_PROGRESS, epicId1,
                LocalDateTime.of(2024, 10, 9, 10, 0), 90L);
        Long subtask1Id = httpTaskManager.addNewSubtask(subtask1);

        Task task1 = new Task("погулять", "заплывешь жиром", Status.NEW,
                LocalDateTime.of(2023, 10, 8, 10, 0), 180L);
        Long task1Id = httpTaskManager.addNewTask(task1);

        httpTaskManager.getTaskById(task1Id);
        httpTaskManager.getEpicById(epicId1);
        httpTaskManager.getSubtaskById(subtask1Id);


        HttpTaskManager uploadedManager = new HttpTaskManager();
        Task uploadedTask1 = uploadedManager.getTaskById(task1Id);
        Epic uploadedEpic1 = uploadedManager.getEpicById(epicId1);
        Subtask uploadedSubtask1 = uploadedManager.getSubtaskById(subtask1Id);

        assertEquals(task1, uploadedTask1);
        assertEquals(epic1, uploadedEpic1);
        assertEquals(subtask1, uploadedSubtask1);

        assertEquals(httpTaskManager.getHistory()
                , uploadedManager.getHistory());

        assertEquals(httpTaskManager.getTasks(), uploadedManager.getTasks());
        assertEquals(httpTaskManager.getEpics(), uploadedManager.getEpics());
        assertEquals(httpTaskManager.getSubtasks(), uploadedManager.getSubtasks());
    }
}
