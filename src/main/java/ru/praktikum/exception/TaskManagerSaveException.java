package ru.praktikum.exception;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 20.07.2024
 */
public class TaskManagerSaveException extends RuntimeException {
    public TaskManagerSaveException(String message) {
        super(message);
    }
}
