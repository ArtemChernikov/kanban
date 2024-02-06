package ru.praktikum.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
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
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class EpicTask extends Task {
    private List<Long> subTasksIds = new ArrayList<>();

    public EpicTask(String name, String description, TaskStatus status, TaskType type) {
        super(name, description, status, type);
    }

    public void addSubTaskId(Long subTaskId) {
        subTasksIds.add(subTaskId);
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "subTasksIds=" + subTasksIds +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
