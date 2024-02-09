package ru.praktikum.util;

import ru.praktikum.service.InMemoryTaskManager;
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

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
