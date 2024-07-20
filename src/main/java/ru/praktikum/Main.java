package ru.praktikum;

import ru.praktikum.model.EpicTask;
import ru.praktikum.model.SubTask;
import ru.praktikum.model.Task;
import ru.praktikum.model.enums.TaskStatus;
import ru.praktikum.model.enums.TaskType;
import ru.praktikum.service.FileBackedTaskManager;
import ru.praktikum.service.TaskManager;
import ru.praktikum.util.Managers;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new FileBackedTaskManager(Managers.getDefaultHistory(), "./src/main/resources/data.csv");

        taskManager.addNewTask(new Task("Уборка", "Протереть стол", TaskStatus.NEW, TaskType.TASK));
        taskManager.addNewTask(new Task("Закалка", "Удариться мизинцем об тумбу", TaskStatus.NEW, TaskType.TASK));

        EpicTask epic1 = (EpicTask) taskManager.addNewTask(new EpicTask("Эпик_1", "Сделать кофе",
                TaskStatus.NEW, TaskType.EPIC_TASK)).get();
        EpicTask epic2 = (EpicTask) taskManager.addNewTask(new EpicTask("Эпик_2", "Выпить кофе",
                TaskStatus.NEW, TaskType.EPIC_TASK)).get();

        taskManager.addNewTask(new SubTask("Включить чайник", "Налить кофе", TaskStatus.NEW, TaskType.SUBTASK, epic1));
        taskManager.addNewTask(new SubTask("Взять молоко", "Налить молоко", TaskStatus.NEW, TaskType.SUBTASK, epic1));
        taskManager.addNewTask(new SubTask("Взять чашку", "Выпить кофе", TaskStatus.NEW, TaskType.SUBTASK, epic2));

        System.out.println("_________________________________________________________");
        System.out.println(taskManager.getAllTasksByType(TaskType.TASK));
        System.out.println(taskManager.getAllTasksByType(TaskType.EPIC_TASK));
        System.out.println(taskManager.getAllTasksByType(TaskType.SUBTASK));

        System.out.println("__________________________________________________________");
        TaskManager taskManager1 = FileBackedTaskManager.loadFromFile(new File("./src/main/resources/data.csv"));
        System.out.println(taskManager1.getAllTasksByType(TaskType.TASK));
        System.out.println(taskManager1.getAllTasksByType(TaskType.EPIC_TASK));
        System.out.println(taskManager1.getAllTasksByType(TaskType.SUBTASK));

        System.out.println("__________________________________________________________");
        taskManager1.addNewTask(new Task("Уборка", "Протереть стол", TaskStatus.NEW, TaskType.TASK));
        System.out.println(taskManager1.getAllTasksByType(TaskType.TASK));
    }
}