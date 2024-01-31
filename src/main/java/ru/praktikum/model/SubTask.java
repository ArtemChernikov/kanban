package ru.praktikum.model;

import lombok.*;
import ru.praktikum.model.enums.TaskStatus;
import ru.praktikum.model.enums.TaskType;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 29.01.2024
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
public class SubTask extends Task {
    private Long epicId;

    public SubTask(Long id, String name, String description, TaskStatus status, TaskType type, Long epicId) {
        super(id, name, description, status, type);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
