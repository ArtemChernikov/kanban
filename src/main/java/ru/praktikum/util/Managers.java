package ru.praktikum.util;

import ru.praktikum.service.FileBackedTaskManager;
import ru.praktikum.service.HistoryManager;
import ru.praktikum.service.InMemoryHistoryManager;
import ru.praktikum.service.TaskManager;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 09.02.2024
 */
public class Managers {
    private Managers() {
        throw new IllegalStateException("Utility class");
    }

    public static TaskManager getDefault(HistoryManager historyManager, String path) {
        return new FileBackedTaskManager(historyManager, path);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
