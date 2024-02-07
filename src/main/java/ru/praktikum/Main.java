package ru.praktikum;

import ru.praktikum.model.EpicTask;
import ru.praktikum.model.SubTask;
import ru.praktikum.model.enums.TaskStatus;
import ru.praktikum.model.enums.TaskType;
import ru.praktikum.service.InMemoryTaskManager;
import ru.praktikum.service.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager();
        EpicTask epicTask = new EpicTask("a", "s", TaskStatus.IN_PROGRESS, TaskType.EPIC_TASK);
        inMemoryTaskManager.addNewTask(epicTask);


        SubTask subTask = new SubTask("s", "s", TaskStatus.DONE, TaskType.SUBTASK, epicTask.getId());
        inMemoryTaskManager.addNewTask(subTask);


        SubTask subTask1 = new SubTask("s", "s", TaskStatus.DONE, TaskType.SUBTASK, epicTask.getId());
        inMemoryTaskManager.addNewTask(subTask1);

        subTask.setStatus(TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.updateTask(subTask);
        System.out.println(inMemoryTaskManager.getAllTasksByType(TaskType.SUBTASK));
        System.out.println(inMemoryTaskManager.getAllTasksByType(TaskType.EPIC_TASK));
        System.out.println();
        epicTask.setStatus(TaskStatus.NEW);
        System.out.println(inMemoryTaskManager.getAllTasksByType(TaskType.EPIC_TASK));
        inMemoryTaskManager.updateTask(epicTask);
        System.out.println(inMemoryTaskManager.getAllTasksByType(TaskType.EPIC_TASK));
    }
}