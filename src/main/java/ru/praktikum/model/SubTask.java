package ru.praktikum.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.praktikum.model.enums.TaskStatus;
import ru.praktikum.model.enums.TaskType;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 29.01.2024
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubTask extends Task {
    private Long epicId;

    public SubTask(Long id, String name, String description, TaskStatus status, TaskType type, Long epicId) {
        super(id, name, description, status, type);
        this.epicId = epicId;
    }
}
