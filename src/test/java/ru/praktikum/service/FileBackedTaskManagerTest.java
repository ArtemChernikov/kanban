package ru.praktikum.service;

import org.junit.jupiter.api.Test;
import ru.praktikum.model.EpicTask;
import ru.praktikum.model.SubTask;
import ru.praktikum.model.Task;
import ru.praktikum.model.enums.TaskStatus;
import ru.praktikum.model.enums.TaskType;
import ru.praktikum.util.Managers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class FileBackedTaskManagerTest {

    @Test
    void whenLoadEmptyFileThenEmptyTasks() throws IOException {
        File tempFile = File.createTempFile("temp", "csv");
        TaskManager taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        List<Task> tasks = taskManager.getAllTasksByType(TaskType.TASK);
        List<Task> subTasks = taskManager.getAllTasksByType(TaskType.SUBTASK);
        List<Task> epicTasks = taskManager.getAllTasksByType(TaskType.EPIC_TASK);
        assertThat(tasks).isEmpty();
        assertThat(subTasks).isEmpty();
        assertThat(epicTasks).isEmpty();
    }

    @Test
    void whenSaveAndLoadTasksThenNotEmptyTasks() throws IOException {
        File tempFile = File.createTempFile("temp", ".csv");
        TaskManager taskManager = new FileBackedTaskManager(Managers.getDefaultHistory(), tempFile.getPath());
        taskManager.addNewTask(new Task("Уборка", "Протереть стол", TaskStatus.NEW, TaskType.TASK));
        taskManager.addNewTask(new Task("Закалка", "Удариться мизинцем об тумбу", TaskStatus.NEW, TaskType.TASK));
        EpicTask epic1 = (EpicTask) taskManager.addNewTask(new EpicTask("Эпик_1",
                "Сделать кофе", TaskStatus.NEW, TaskType.EPIC_TASK)).get();
        EpicTask epic2 = (EpicTask) taskManager.addNewTask(new EpicTask("Эпик_2",
                "Выпить кофе", TaskStatus.NEW, TaskType.EPIC_TASK)).get();
        taskManager.addNewTask(new SubTask("Включить чайник", "Налить кофе", TaskStatus.NEW, TaskType.SUBTASK, epic1));
        taskManager.addNewTask(new SubTask("Взять молоко", "Налить молоко", TaskStatus.NEW, TaskType.SUBTASK, epic1));
        taskManager.addNewTask(new SubTask("Взять чашку", "Выпить кофе", TaskStatus.NEW, TaskType.SUBTASK, epic1));

        TaskManager newTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        List<Task> tasks = newTaskManager.getAllTasksByType(TaskType.TASK);
        List<Task> subTasks = newTaskManager.getAllTasksByType(TaskType.SUBTASK);
        List<Task> epicTasks = newTaskManager.getAllTasksByType(TaskType.EPIC_TASK);

        assertThat(tasks.size()).isEqualTo(2);
        assertThat(subTasks.size()).isEqualTo(3);
        assertThat(epicTasks.size()).isEqualTo(2);
    }

}