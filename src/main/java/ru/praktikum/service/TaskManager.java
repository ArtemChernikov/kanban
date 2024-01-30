package ru.praktikum.service;

import ru.praktikum.model.Epic;
import ru.praktikum.model.SubTask;
import ru.praktikum.model.Task;

import java.util.List;

public interface TaskManager {

    void addNewTask(Task task);

    void addNewEpic(Epic epic);

    void addNewSubTask(SubTask subTask);

    Task getTaskById(Long id);

    Epic getEpicById(Long id);

    SubTask getSubTaskById();

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void update(SubTask subTask);

    void deleteTaskById(Long id);

    void deleteEpicById(Long id);

    void deleteSubTaskById(Long id);

    void deleteAllTasks();

    void deleteAllEpic();

    void deleteAllSubtasks();

    List<Task> getAllTasks();

    List<Epic> getAllEpicTasks();

    List<SubTask> getAllSubtasks();

    List<SubTask> getAllSubTaskByEpicId(Long id);

    void setStatus(Task task);

}
