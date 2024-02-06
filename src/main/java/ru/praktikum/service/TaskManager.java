package ru.praktikum.service;

import ru.praktikum.model.SubTask;
import ru.praktikum.model.Task;
import ru.praktikum.model.enums.TaskType;

import java.util.List;
import java.util.Optional;

public interface TaskManager {

    Optional<? extends Task> addNewTask(Task task);

    Task getTaskByIdAndType(Long id, TaskType type);

    Task updateTask(Task task);

    Task deleteTaskByIdAndType(Long id, TaskType type);

    void deleteAllTasksByType(TaskType type);

    List<Task> getAllTasksByType(TaskType type);

    List<SubTask> getAllSubTasksByEpicId(Long id);

}
