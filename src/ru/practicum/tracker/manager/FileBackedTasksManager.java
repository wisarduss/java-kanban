package ru.practicum.tracker.manager;

import ru.practicum.tracker.exception.ManagerSaveException;
import ru.practicum.tracker.tasks.Epic;
import ru.practicum.tracker.tasks.Subtask;
import ru.practicum.tracker.tasks.Task;
import ru.practicum.tracker.tasks.TaskType;
import ru.practicum.tracker.tasks.Status;


import java.io.*;

import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static final String PATH = "tasks.txt";
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager newTaskManager = new FileBackedTasksManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null && br.ready() && !line.isBlank()) {
                Task task = SCVFormatterUtils.fromString(line);
                if (task.getType().equals(TaskType.TASK)) {
                    newTaskManager.addNewTask(task);
                }
                if (task.getType().equals(TaskType.EPIC)) {
                    newTaskManager.addNewEpic((Epic) task);
                }
                if (task.getType().equals(TaskType.SUBTASK)) {
                    newTaskManager.addNewSubtask((Subtask) task);
                }
            }
            line = br.readLine();
            final List<Integer> history = SCVFormatterUtils.historyFromString(line);
            for (Integer id : history) {
                for (Task task : newTaskManager.getTasks()) {
                    if (task.getId() == id) {
                        newTaskManager.getTaskById(id);
                    }
                }
                for (Epic epic : newTaskManager.getEpics()) {
                    if (epic.getId() == id) {
                        newTaskManager.getEpicById(id);
                    }
                }
                for (Subtask subtask : newTaskManager.getSubtasks()) {
                    if (subtask.getId() == id) {
                        newTaskManager.getSubtaskById(id);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла в методе loadFromFile");
        }
        return newTaskManager;
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            if (!getEpics().isEmpty()) {
                for (Epic epic : getEpics()) {
                    writer.write(SCVFormatterUtils.toString(epic) + "\n");
                }
            }
            if (!getSubtasks().isEmpty()) {
                for (Subtask subtask : getSubtasks()) {
                    writer.write(SCVFormatterUtils.toString(subtask) + "\n");
                }
            }
            if (!getTasks().isEmpty()) {
                for (Task task : getTasks()) {
                    writer.write(SCVFormatterUtils.toString(task) + "\n");
                }
            }


            writer.write(System.lineSeparator());
            if (getHistoryManager().getHistory() != null) {
                writer.write(SCVFormatterUtils.historyToString(super.getHistoryManager()) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла в методе loadFromFile");
        }
    }

    @Override
    public long addNewEpic(Epic epic) {
        long id = super.addNewEpic(epic);
        save();
        return id;
    }

    @Override
    public Long addNewSubtask(Subtask subtask) {
        long id = super.addNewSubtask(subtask);
        save();
        return id;
    }

    @Override
    public Long addNewTask(Task task) {
        long id = super.addNewTask(task);
        save();
        return id;
    }

    @Override
    public Task getTaskById(long taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public Subtask getSubtaskById(long subtaskId) {
        Subtask subtask = super.getSubtaskById(subtaskId);
        save();
        return subtask;
    }

    @Override
    public Epic getEpicById(long epicId) {
        Epic epic = super.getEpicById(epicId);
        save();
        return epic;
    }

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File(PATH));
        Epic epic1 = new Epic("Переезд", "Нужно сделать все необходимое для переезда");
        Long epicId1 = fileBackedTasksManager.addNewEpic(epic1);

        Epic epic2 = new Epic("Убраться в квартитре", "В доме слишком пыльно и все вещи разбросаны");
        Long epicId2 = fileBackedTasksManager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Собрать коробки", "Положить в них все вещи для переезда ",
                Status.IN_PROGRESS, epicId1);
        Long subtask1Id = fileBackedTasksManager.addNewSubtask(subtask1);

        Subtask subtask2 = new Subtask("Упаковать кошку", "Положить ее в переноску", Status.NEW,
                epicId1);
        Long subtask2Id = fileBackedTasksManager.addNewSubtask(subtask2);

        Subtask subtask3 = new Subtask("Сказать слова на прощание", "Попрощатсья с соседями",
                Status.NEW, epicId1);
        Long subtask3Id = fileBackedTasksManager.addNewSubtask(subtask3);

        Subtask subtask4 = new Subtask("Помыть полы", "У нас же вроде был белый ламинат...",
                Status.DONE, epicId2);
        Long subtask4Id = fileBackedTasksManager.addNewSubtask(subtask4);

        Subtask subtask5 = new Subtask("Убрать вещи в шкаф", "А шкаф то полностью пустой...",
                Status.DONE, epicId2);
        Long subtask5Id = fileBackedTasksManager.addNewSubtask(subtask5);

        Task task1 = new Task("погулять", "заплывешь жиром", Status.NEW);
        Long task1Id = fileBackedTasksManager.addNewTask(task1);

        Task task2 = new Task("спать", "нужен сон", Status.NEW);
        Long task2Id = fileBackedTasksManager.addNewTask(task2);
        fileBackedTasksManager.getEpicById(epicId1);
        fileBackedTasksManager.getSubtaskById(subtask1Id);
        fileBackedTasksManager.getSubtaskById(subtask2Id);
        fileBackedTasksManager.getSubtaskById(subtask3Id);
        fileBackedTasksManager.getTaskById(task1Id);
        fileBackedTasksManager.getTaskById(task2Id);

        FileBackedTasksManager testRestoredOne = loadFromFile(new File(PATH));
        System.out.println("\nТест работы: история просмотра \n");
        for (Task task : testRestoredOne.getHistoryManager().getHistory()) {
            System.out.println(task);
        }
        System.out.println("\nТест работы: список тасок \n");
        for (Task task : testRestoredOne.getTasks()) {
            System.out.println(task);
        }
        System.out.println("\nТест работы: список эпиков \n");
        for (Epic epic : testRestoredOne.getEpics()) {
            System.out.println(epic);
        }
        System.out.println("\nТест работы: список сабтасок \n");
        for (Subtask subtask : testRestoredOne.getSubtasks()) {
            System.out.println(subtask);
        }

    }

}
