package ru.praktikum.service;

import org.junit.jupiter.api.Test;
import ru.praktikum.model.EpicTask;
import ru.praktikum.model.SubTask;
import ru.praktikum.model.Task;
import ru.praktikum.model.enums.TaskStatus;
import ru.praktikum.model.enums.TaskType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest {
    private final HistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    void whenGetAllTasks() {
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        Task task1 = new Task("name1", "description1", TaskStatus.NEW, TaskType.TASK);
        Task task2 = new Task("name2", "description2", TaskStatus.NEW, TaskType.TASK);
        List<Task> expected = List.of(taskManager.addNewTask(task1).get(), taskManager.addNewTask(task2).get());
        List<Task> actual = taskManager.getAllTasksByType(TaskType.TASK);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenGetAllEpic() {
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        EpicTask task1 = new EpicTask("name1", "description1", TaskStatus.NEW, TaskType.EPIC_TASK);
        EpicTask task2 = new EpicTask("name2", "description2", TaskStatus.NEW, TaskType.EPIC_TASK);
        List<Task> expected = List.of(taskManager.addNewTask(task1).get(), taskManager.addNewTask(task2).get());
        List<Task> actual = taskManager.getAllTasksByType(TaskType.EPIC_TASK);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenDeleteAllTasks() {
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        Task task1 = new Task("name1", "description1", TaskStatus.NEW, TaskType.TASK);
        Task task2 = new Task("name2", "description2", TaskStatus.NEW, TaskType.TASK);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.deleteAllTasksByType(TaskType.TASK);
        List<Task> actual = taskManager.getAllTasksByType(TaskType.TASK);
        assertThat(actual).isEmpty();
    }

    @Test
    void whenDeleteAllEpic() {
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        EpicTask task1 = new EpicTask("name1", "description1", TaskStatus.NEW, TaskType.EPIC_TASK);
        EpicTask task2 = new EpicTask("name2", "description2", TaskStatus.NEW, TaskType.EPIC_TASK);
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.deleteAllTasksByType(TaskType.EPIC_TASK);
        List<Task> actual = taskManager.getAllTasksByType(TaskType.EPIC_TASK);
        assertThat(actual).isEmpty();
    }

    @Test
    void whenDeleteAllSubTask() {
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        SubTask task1 = new SubTask("name1", "description1", TaskStatus.NEW, TaskType.SUBTASK);
        SubTask task2 = new SubTask("name2", "description2", TaskStatus.NEW, TaskType.SUBTASK);
        EpicTask epic = new EpicTask("name1", "description1", TaskStatus.NEW, TaskType.EPIC_TASK);
        taskManager.addNewTask(epic);
        epic.addSubTask(task1);
        epic.addSubTask(task2);
        taskManager.deleteAllTasksByType(TaskType.SUBTASK);
        List<Task> actual = taskManager.getAllTasksByType(TaskType.SUBTASK);
        assertThat(actual).isEmpty();
    }

    @Test
    void whenGetTaskByIdAndType() {
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        Task task1 = new Task("name1", "description1", TaskStatus.NEW, TaskType.TASK);
        Task task2 = new Task("name2", "description2", TaskStatus.NEW, TaskType.TASK);
        Task expected1 = taskManager.addNewTask(task1).get();
        Task expected2 = taskManager.addNewTask(task2).get();
        Task actual1 = taskManager.getTaskByIdAndType(expected1.getId(), TaskType.TASK).get();
        Task actual2 = taskManager.getTaskByIdAndType(expected2.getId(), TaskType.TASK).get();
        assertEquals(actual1, expected1);
        assertEquals(actual2, expected2);
        assertThat(actual1).isEqualTo(expected1);
        assertThat(actual2).isEqualTo(expected2);
    }

    @Test
    void whenGetEpicByIdAndType() {
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        EpicTask task1 = new EpicTask("name1", "description1", TaskStatus.NEW, TaskType.EPIC_TASK);
        EpicTask task2 = new EpicTask("name2", "description2", TaskStatus.NEW, TaskType.EPIC_TASK);
        Task expected1 = taskManager.addNewTask(task1).get();
        Task expected2 = taskManager.addNewTask(task2).get();
        Task actual1 = taskManager.getTaskByIdAndType(expected1.getId(), TaskType.EPIC_TASK).get();
        Task actual2 = taskManager.getTaskByIdAndType(expected2.getId(), TaskType.EPIC_TASK).get();
        assertThat(actual1).isEqualTo(expected1);
        assertThat(actual2).isEqualTo(expected2);
    }

    @Test
    void whenDeleteTaskByIdAndType() {
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        Task task1 = new Task("name1", "description1", TaskStatus.NEW, TaskType.TASK);
        Task task2 = new Task("name2", "description2", TaskStatus.NEW, TaskType.TASK);
        Task saved1 = taskManager.addNewTask(task1).get();
        Task saved2 = taskManager.addNewTask(task2).get();
        taskManager.deleteTaskByIdAndType(saved1.getId(), TaskType.TASK);
        taskManager.deleteTaskByIdAndType(saved2.getId(), TaskType.TASK);
        List<Task> actual = taskManager.getAllTasksByType(TaskType.TASK);
        assertThat(actual).isEmpty();
    }

    @Test
    void whenDeleteEpicByIdAndType() {
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        EpicTask task1 = new EpicTask("name1", "description1", TaskStatus.NEW, TaskType.EPIC_TASK);
        EpicTask task2 = new EpicTask("name2", "description2", TaskStatus.NEW, TaskType.EPIC_TASK);
        Task saved1 = taskManager.addNewTask(task1).get();
        Task saved2 = taskManager.addNewTask(task2).get();
        taskManager.deleteTaskByIdAndType(saved1.getId(), TaskType.EPIC_TASK);
        taskManager.deleteTaskByIdAndType(saved2.getId(), TaskType.EPIC_TASK);
        List<Task> actual = taskManager.getAllTasksByType(TaskType.EPIC_TASK);
        assertThat(actual).isEmpty();
    }

    @Test
    void whenUpdateTask() {
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        Task task1 = new Task("name1", "description1", TaskStatus.NEW, TaskType.TASK);
        Task oldTask = taskManager.addNewTask(task1).get();
        Task update = new Task(oldTask.getId(), "name2", "description2", TaskStatus.NEW, TaskType.TASK);
        taskManager.updateTask(update);
        Task updateTask = taskManager.getTaskByIdAndType(oldTask.getId(), TaskType.TASK).get();
        assertEquals(update, updateTask);
    }

    @Test
    void whenUpdateEpic() {
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        EpicTask task1 = new EpicTask("name1", "description1", TaskStatus.NEW, TaskType.EPIC_TASK);
        Task oldTask = taskManager.addNewTask(task1).get();
        EpicTask update = new EpicTask(
                oldTask.getId(),
                "name2",
                "description2",
                TaskStatus.NEW,
                TaskType.EPIC_TASK);
        taskManager.updateTask(update);
        Task updateTask = taskManager.getTaskByIdAndType(oldTask.getId(), TaskType.EPIC_TASK).get();
        assertEquals(update, updateTask);
    }

}