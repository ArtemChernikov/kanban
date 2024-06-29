package ru.praktikum.service;

import ru.praktikum.model.Node;
import ru.praktikum.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 09.02.2024
 */
public class InMemoryHistoryManager implements HistoryManager {
    private final CustomLinkedList<Task> history = new CustomLinkedList<>();
    private final Map<Long, Node<Task>> historyTaskMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (historyTaskMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        historyTaskMap.put(task.getId(), history.listLast(task));
    }

    @Override
    public void remove(long id) {
        history.removeNode(historyTaskMap.get(id));
    }

    @Override
    public List<Task> getHistory() {

        return history.getTasks();
    }
}

class CustomLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size = 0;

    public Node<T> listLast(T element) {
        Node<T> newNode = new Node<>(null, element, null);
        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
        }
        tail = newNode;
        size++;
        return newNode;
    }

    public List<T> getTasks() {
        List<T> allTasks = new ArrayList<>();
        Node<T> currentNode = head;
        while (currentNode != null) {
            allTasks.add(currentNode.data);
            currentNode = currentNode.next;
        }
        return allTasks;
    }

    public void removeNode(Node<T> task) {
        Node<T> prev = task.prev;
        Node<T> next = task.next;

        if (task == head && next != null) {
            next.prev = null;
            head = next;
        } else if (task == tail && prev != null) {
            prev.next = null;
            tail = prev;
        } else if (next != null && prev != null) {
            prev.next = next;
            next.prev = prev;
        } else {
            head = null;
            tail = null;
        }
        size--;
    }
}
