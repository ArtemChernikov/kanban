package ru.praktikum.service;

import ru.praktikum.model.SubTask;
import ru.praktikum.model.Task;
import ru.praktikum.model.enums.TaskType;

import java.util.List;

public interface TaskManager {

    void addNewTask(Task task);

    Task getTaskByIdAndType(Long id, TaskType type);

    void updateTask(Task task);

    void deleteTaskByIdAndType(Long id, TaskType type);

    void deleteAllTasksByType(TaskType type);

    List<Task> getAllTasksByType(TaskType type);

    List<SubTask> getAllSubTasksByEpicId(Long id);

}
