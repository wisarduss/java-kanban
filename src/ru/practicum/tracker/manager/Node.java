package ru.practicum.tracker.manager;

import ru.practicum.tracker.tasks.Task;

public class Node {
    Task value;
    Node prev;
    Node next;

    public Node(Task value, Node prev, Node next) {
        this.value = value;
        this.prev = prev;
        this.next = next;
    }
}
