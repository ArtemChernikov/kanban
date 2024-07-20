package ru.praktikum.exception;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 20.07.2024
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
