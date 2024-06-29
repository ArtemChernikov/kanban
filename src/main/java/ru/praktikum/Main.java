package ru.praktikum;

import ru.praktikum.model.EpicTask;
import ru.praktikum.model.SubTask;
import ru.praktikum.model.Task;
import ru.praktikum.model.enums.TaskStatus;
import ru.praktikum.model.enums.TaskType;
import ru.praktikum.service.HistoryManager;
import ru.praktikum.service.InMemoryTaskManager;
import ru.praktikum.service.TaskManager;
import ru.praktikum.util.Managers;

public class Main {
    public static void main(String[] args) {
        HistoryManager defaultHistory = Managers.getDefaultHistory();
        TaskManager inMemoryTaskManager = new InMemoryTaskManager(defaultHistory);
        EpicTask epicTask = new EpicTask("a", "s", TaskStatus.IN_PROGRESS, TaskType.EPIC_TASK);
        inMemoryTaskManager.addNewTask(epicTask);


        SubTask subTask = new SubTask("s", "s", TaskStatus.DONE, TaskType.SUBTASK, epicTask.getId());
        inMemoryTaskManager.addNewTask(subTask);


        SubTask subTask1 = new SubTask("s", "s", TaskStatus.DONE, TaskType.SUBTASK, epicTask.getId());
        inMemoryTaskManager.addNewTask(subTask1);

        Task task = new Task("s", "s", TaskStatus.DONE, TaskType.TASK);
        inMemoryTaskManager.addNewTask(task);

        System.out.println(inMemoryTaskManager.getTaskByIdAndType(epicTask.getId(), TaskType.EPIC_TASK));
        System.out.println(inMemoryTaskManager.getTaskByIdAndType(subTask1.getId(), TaskType.SUBTASK));
        System.out.println(inMemoryTaskManager.getTaskByIdAndType(subTask.getId(), TaskType.EPIC_TASK));
        System.out.println(inMemoryTaskManager.getTaskByIdAndType(999L, TaskType.SUBTASK));
        System.out.println(inMemoryTaskManager.getTaskByIdAndType(epicTask.getId(), TaskType.EPIC_TASK));
        System.out.println(inMemoryTaskManager.getTaskByIdAndType(subTask1.getId(), TaskType.SUBTASK));
        System.out.println(inMemoryTaskManager.getTaskByIdAndType(epicTask.getId(), TaskType.EPIC_TASK));
        System.out.println(inMemoryTaskManager.getTaskByIdAndType(subTask1.getId(), TaskType.SUBTASK));
        System.out.println(inMemoryTaskManager.getTaskByIdAndType(epicTask.getId(), TaskType.EPIC_TASK));
        System.out.println(inMemoryTaskManager.getTaskByIdAndType(subTask1.getId(), TaskType.SUBTASK));
        System.out.println(inMemoryTaskManager.getTaskByIdAndType(epicTask.getId(), TaskType.EPIC_TASK));
        System.out.println(inMemoryTaskManager.getTaskByIdAndType(subTask1.getId(), TaskType.SUBTASK));
        System.out.println(inMemoryTaskManager.getTaskByIdAndType(epicTask.getId(), TaskType.EPIC_TASK));
        System.out.println(inMemoryTaskManager.getTaskByIdAndType(subTask1.getId(), TaskType.SUBTASK));

        inMemoryTaskManager.getAllTasksByType(TaskType.EPIC_TASK).forEach(System.out::println);
        System.out.println(inMemoryTaskManager.getHistory().size());
        inMemoryTaskManager.deleteAllTasksByType(TaskType.SUBTASK);
        inMemoryTaskManager.getAllTasksByType(TaskType.EPIC_TASK).forEach(System.out::println);
    }
}