package ru.praktikum.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.praktikum.model.enums.TaskStatus;
import ru.praktikum.model.enums.TaskType;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 29.01.2024
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class SubTask extends Task {
    private EpicTask epicTask;

    public SubTask(String name, String description, TaskStatus status, TaskType type, EpicTask epicTask) {
        super(name, description, status, type);
        this.epicTask = epicTask;
    }

    public SubTask(String name, String description, TaskStatus status, TaskType type) {
        super(name, description, status, type);
    }

    public SubTask(Long id, String name, String description, TaskStatus status, TaskType type, EpicTask epicTask) {
        super(id, name, description, status, type);
        this.epicTask = epicTask;
    }

    @Override
    public String toCSV() {
        return id + "," + type + "," + name + "," + status + "," + description + "," + epicTask.getId();
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicTask.getId() +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
