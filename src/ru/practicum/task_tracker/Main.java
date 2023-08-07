package ru.practicum.task_tracker;

import ru.practicum.task_tracker.manager.TaskManager;
import ru.practicum.task_tracker.tasks.Epic;
import ru.practicum.task_tracker.tasks.Subtask;
import ru.practicum.task_tracker.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();


        Epic epic1 = new Epic("Переезд", "Нужно сделать все необходимое, для переезда");
        Long epicId1 = taskManager.addNewEpic(epic1);

        Epic epic2 = new Epic("Убраться в квартитре", "В доме слишком пыльно и все вещи разбросаны");
        Long epicId2 = taskManager.addNewEpic(epic2);


        Subtask subtask1 = new Subtask("Собрать коробки", "Положить в них все вещи для переезда ",
                "IN_PROGRESS", epicId1);
        Long subtask1Id = taskManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Упаковать кошку", "Положить ее в переноску", "NEW", epicId1);
        Long subtask2Id = taskManager.addNewSubtask(subtask2);

        Subtask subtask3 = new Subtask("Сказать слова на прощание", "Попрощатсья с соседями", "NEW", epicId1);
        Long subtask3Id = taskManager.addNewSubtask(subtask3);

        Subtask subtask4 = new Subtask("Помыть полы", "У нас же вроде был белый ламинат...",
                "DONE", epicId2);
        Long subtask4Id = taskManager.addNewSubtask(subtask4);
        Subtask subtask5 = new Subtask("Убрать вещи в шкаф", "А шкаф то полностью пустой...",
                "DONE", epicId2);
        Long subtask5Id = taskManager.addNewSubtask(subtask5);


        Task task1 = new Task("погулять", "заплывешь жиром", "NEW");
        Long task1Id = taskManager.addNewTask(task1);

        Task task2 = new Task("спать", "нужен сон", "NEW");
        Long task2Id = taskManager.addNewTask(task2);


        System.out.println("Вывод всех задач");
        taskManager.printAllTasks();
        System.out.println();

        System.out.println("Проверка поиска  по id");
        taskManager.getTaskById(task2Id);
        System.out.println();
        taskManager.getSubtaskById(subtask4Id);
        System.out.println();
        taskManager.getEpicById(epicId2);
        System.out.println("-------------------------------");

        System.out.println("Проверка на обновление");
        taskManager.updateTask(new Task("Покушать", "Ты очень голодный", "NEW"), task1Id);
        taskManager.updateSubtask(new Subtask("Помыть окна", "Даже свет не попадает тебе в комнату",
                "NEW", epicId2), subtask5Id);
        taskManager.updateEpic(new Epic("Эпик", "Описание"), epicId2);

        taskManager.printAllTasks();
        System.out.println();

        System.out.println("Получение подзадач определенного эпика");
        taskManager.getAllSubtasksOfEpic(epicId1);
        System.out.println();

        System.out.println("Удаление по индетификатору");
        taskManager.removeTaskById(task1Id);
        taskManager.removeSubtaskById(subtask2Id);
        taskManager.removeEpicById(epicId2);
        taskManager.printAllTasks();
        System.out.println();

        System.out.println("Полное удаление");
        taskManager.removeTasks();
        taskManager.printAllTasks();
        System.out.println();
        taskManager.removeSubtasks();
        taskManager.printAllTasks();
        System.out.println();
        taskManager.removeEpics();
        taskManager.printAllTasks();
        System.out.println();

    }
}
