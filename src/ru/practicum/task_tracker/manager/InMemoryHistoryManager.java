package ru.practicum.task_tracker.manager;

import ru.practicum.task_tracker.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Long, Node> nodeMap = new HashMap<>();

    private Node first;
    private Node last;

    @Override
    public void remove(long id) {
        Node node = nodeMap.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public void addTask(Task task) {
        if (task != null) {
            long id = task.getId();
            remove(id);
            linkLast(task);
            nodeMap.put(id, last);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node node = first;
        while (node != null) {
            tasks.add(node.value);
            node = node.next;
        }
        return tasks;
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
            if (node.next == null) {
                last = node.prev;
            } else {
                node.next.prev = node.prev;
            }
        } else {
            first = node.next;
            if (first == null) {
                last = null;
            } else {
                first.prev = null;
            }
        }
    }

    private void linkLast(Task task) {
        Node node = new Node(task, last, null);
        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
    }
}