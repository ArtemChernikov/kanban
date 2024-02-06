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
        System.out.println(inMemoryTaskManager.getTaskByIdAndType(epicTask.getId(), TaskType.EPIC_TASK));

        SubTask subTask = new SubTask("s", "s", TaskStatus.DONE, TaskType.SUBTASK, epicTask.getId());
        inMemoryTaskManager.addNewTask(subTask);
        System.out.println(inMemoryTaskManager.getTaskByIdAndType(epicTask.getId(), TaskType.EPIC_TASK));

        SubTask subTask1 = new SubTask("s", "s", TaskStatus.IN_PROGRESS, TaskType.SUBTASK, epicTask.getId());
        inMemoryTaskManager.addNewTask(subTask1);
        System.out.println(inMemoryTaskManager.getTaskByIdAndType(epicTask.getId(), TaskType.EPIC_TASK));

        //inMemoryTaskManager.deleteTaskByIdAndType(epicTask.getId(), TaskType.EPIC_TASK);
        System.out.println(inMemoryTaskManager.getAllTasksByType(TaskType.EPIC_TASK));
        System.out.println(inMemoryTaskManager.getAllTasksByType(TaskType.SUBTASK));
        System.out.println(inMemoryTaskManager.getAllSubTasksByEpicId(epicTask.getId()));
    }
}