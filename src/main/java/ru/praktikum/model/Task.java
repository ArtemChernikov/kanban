package ru.praktikum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.praktikum.model.enums.TaskStatus;
import ru.praktikum.model.enums.TaskType;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 29.01.2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Task {
    protected Long id;
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected TaskType type;

    public Task(String name, String description, TaskStatus status, TaskType type) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
    }

    public Task(Long id) {
        this.id = id;
    }

    public String toCSV() {
        return id + "," + type + "," + name + "," + status + "," + description;
    }
}
