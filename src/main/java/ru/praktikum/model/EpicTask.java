package ru.praktikum.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.praktikum.model.enums.TaskStatus;
import ru.praktikum.model.enums.TaskType;

import java.util.ArrayList;
import java.util.List;

import static ru.praktikum.model.enums.TaskStatus.DONE;
import static ru.praktikum.model.enums.TaskStatus.IN_PROGRESS;
import static ru.praktikum.model.enums.TaskStatus.NEW;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 29.01.2024
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EpicTask extends Task {
    private List<SubTask> subTasks = new ArrayList<>();

    public EpicTask(String name, String description, TaskStatus status, TaskType type) {
        super(name, description, status, type);
    }

    public EpicTask(Long id, String name, String description, TaskStatus status, TaskType type) {
        super(id, name, description, status, type);
    }

    public void updateStatus() {
        TaskStatus taskStatus = IN_PROGRESS;
        if (subTasks.isEmpty()) {
            this.status = NEW;
            return;
        }
        boolean isNew = true;
        boolean isDone = true;
        for (SubTask subTask : subTasks) {
            TaskStatus subTaskStatus = subTask.getStatus();
            if (!NEW.equals(subTaskStatus)) {
                isNew = false;
            }
            if (!DONE.equals(subTaskStatus)) {
                isDone = false;
            }
        }
        if (isNew) {
            taskStatus = NEW;
        }
        if (isDone) {
            taskStatus = DONE;
        }
        this.status = taskStatus;
    }

    public List<Long> getSubTasksIds() {
        return subTasks.stream()
                .map(SubTask::getId)
                .toList();
    }

    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks);
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    public void deleteSubTask(SubTask subTask) {
        subTasks.remove(subTask);
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "subTasksSize=" + subTasks.size() +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
