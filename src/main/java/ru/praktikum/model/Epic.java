package ru.praktikum.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.praktikum.model.enums.TaskStatus;
import ru.praktikum.model.enums.TaskType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 29.01.2024
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Epic extends Task {
    private List<Long> subTasksIds;

    public Epic(Long id, String name, String description, TaskStatus status, TaskType type) {
        super(id, name, description, status, type);
        this.subTasksIds = new ArrayList<>();
    }
}
