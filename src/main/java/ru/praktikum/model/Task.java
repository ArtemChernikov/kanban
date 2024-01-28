package ru.praktikum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.praktikum.model.enums.TaskStatus;
import ru.praktikum.model.enums.TaskType;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 29.01.2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Task {
    private Long id;
    private String name;
    private String description;
    private TaskStatus status;
    private TaskType type;
}
