package ru.praktikum.service;

import ru.praktikum.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 09.02.2024
 */
public class InMemoryHistoryManager implements HistoryManager {
    private static final int MAX_HISTORY_SIZE = 10;
    private static final int FIRST_HISTORY_ELEMENT = 0;
    private final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() == MAX_HISTORY_SIZE) {
            history.remove(FIRST_HISTORY_ELEMENT);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
