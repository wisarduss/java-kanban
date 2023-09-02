package ru.practicum.task_tracker;


import ru.practicum.task_tracker.manager.Managers;
import ru.practicum.task_tracker.manager.TaskManager;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Status;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Epic epic1 = new Epic("Переезд", "Нужно сделать все необходимое, для переезда");
        Long epicId1 = taskManager.addNewEpic(epic1);

        Epic epic2 = new Epic("Убраться в квартитре", "В доме слишком пыльно и все вещи разбросаны");
        Long epicId2 = taskManager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Собрать коробки", "Положить в них все вещи для переезда ",
                Status.IN_PROGRESS, epicId1);
        Long subtask1Id = taskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Упаковать кошку", "Положить ее в переноску", Status.NEW, epicId1);
        Long subtask2Id = taskManager.addNewSubtask(subtask2);

        Subtask subtask3 = new Subtask("Сказать слова на прощание", "Попрощатсья с соседями", Status.NEW, epicId1);
        Long subtask3Id = taskManager.addNewSubtask(subtask3);

        Subtask subtask4 = new Subtask("Помыть полы", "У нас же вроде был белый ламинат...",
                Status.DONE, epicId2);
        Long subtask4Id = taskManager.addNewSubtask(subtask4);
        Subtask subtask5 = new Subtask("Убрать вещи в шкаф", "А шкаф то полностью пустой...",
                Status.DONE, epicId2);
        Long subtask5Id = taskManager.addNewSubtask(subtask5);

        Task task1 = new Task("погулять", "заплывешь жиром", Status.NEW);
        Long task1Id = taskManager.addNewTask(task1);

        Task task2 = new Task("спать", "нужен сон", Status.NEW);
        Long task2Id = taskManager.addNewTask(task2);

        System.out.println("Вывод всех задач");
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println(taskManager.getAllTasks());
        System.out.println("-------------------------------");

        System.out.println("Проверка поиска  по id");
        System.out.println(taskManager.getTaskById(task2Id));
        System.out.println(taskManager.getSubtaskById(subtask4Id));
        System.out.println(taskManager.getEpicById(epicId2));
        System.out.println("-------------------------------");
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getTaskById(task2Id));
        System.out.println(taskManager.getHistory());
        System.out.println();

        System.out.println("Проверка на обновление");
        taskManager.updateTask(new Task("Позвонить маме", "У нее день рождение", task1Id, Status.NEW));
        System.out.println(taskManager.getAllTasks());
        taskManager.updateSubtask(new Subtask("Помыть окна", "Даже свет не попадает тебе в комнату",
                subtask4Id, Status.NEW, epicId2));
        System.out.println(taskManager.getAllSubtasks());
        taskManager.updateEpic(new Epic("Эпик", "Описание", epicId2));
        System.out.println(taskManager.getAllEpics());
        System.out.println("-------------------------------");

        System.out.println("Получение подзадач определенного эпика");
        System.out.println(taskManager.getAllSubtasksOfEpic(epicId2));
        System.out.println();
        System.out.println("-------------------------------");

        System.out.println("Удаление по индетификатору");
        taskManager.removeTaskById(task1Id);
        System.out.println(taskManager.getAllTasks());
        taskManager.removeSubtaskById(subtask1Id);
        System.out.println(taskManager.getAllSubtasks());
        taskManager.removeEpicById(epicId2);
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println("-------------------------------");
        taskManager.removeTaskById(task2Id);
        System.out.println(taskManager.getHistory());

        System.out.println("-------------------------------");
        System.out.println("Полное удаление");
        taskManager.removeTasks();
        System.out.println(taskManager.getAllTasks());
        taskManager.removeSubtasks();
        System.out.println(taskManager.getAllSubtasks());
        taskManager.removeEpics();
        System.out.println(taskManager.getAllEpics());
        System.out.println("-------------------------------");
    }
}
