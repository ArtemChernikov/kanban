package ru.praktikum.service;

import ru.praktikum.model.EpicTask;
import ru.praktikum.model.SubTask;
import ru.praktikum.model.Task;
import ru.praktikum.model.enums.TaskStatus;
import ru.praktikum.model.enums.TaskType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.praktikum.model.enums.TaskStatus.*;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 04.02.2024
 */
public class InMemoryTaskManager implements TaskManager {
    private final Map<Long, Task> tasks = new HashMap<>();
    private final Map<Long, SubTask> subTasks = new HashMap<>();
    private final Map<Long, EpicTask> epicTasks = new HashMap<>();
    private Long id = 1L;

    @Override
    public Optional<? extends Task> addNewTask(Task task) {
        TaskType type = task.getType();
        switch (type) {
            case TASK -> {
                return addTask(task);
            }
            case SUBTASK -> {
                return addSubTask((SubTask) task);
            }
            case EPIC_TASK -> {
                return addEpicTask((EpicTask) task);
            }
            default -> {
                System.out.println("Укажите корректный тип задачи");
                return Optional.empty();
            }
        }
    }

    @Override
    public Task getTaskByIdAndType(Long id, TaskType type) {
        return null;
    }

    @Override
    public Task updateTask(Task task) {
        return null;
    }

    @Override
    public Task deleteTaskByIdAndType(Long id, TaskType type) {
        return null;
    }

    @Override
    public void deleteAllTasksByType(TaskType type) {

    }

    @Override
    public List<Task> getAllTasksByType(TaskType type) {
        return null;
    }

    @Override
    public List<SubTask> getAllSubTasksByEpicId(Long id) {
        return null;
    }

    private Optional<Task> addTask(Task task) {
        task.setId(id++);
        tasks.put(task.getId(), task);
        return Optional.of(task);
    }

    private Optional<SubTask> addSubTask(SubTask subTask) {
        Long epicId = subTask.getEpicId();
        if (epicTasks.containsKey(epicId)) {
            EpicTask epicTask = epicTasks.get(epicId);
            subTask.setId(id++);
            epicTask.addSubTaskId(subTask.getId());
            subTasks.put(subTask.getId(), subTask);
            updateEpicTaskStatus(epicId);
            return Optional.of(subTask);
        }
        System.out.println("Укажите корректный идентификатор EpicTask у SubTask");
        return Optional.empty();
    }

    private Optional<EpicTask> addEpicTask(EpicTask epicTask) {
        epicTask.setId(id++);
        epicTasks.put(epicTask.getId(), epicTask);
        return Optional.of(epicTask);
    }

    private void updateEpicTaskStatus(Long epicId) {
        EpicTask epicTask = epicTasks.get(epicId);
        List<Long> subTasksIds = epicTask.getSubTasksIds();
        epicTask.setStatus(getEpicTaskStatusBySubTasksStatuses(subTasksIds));
    }

    private TaskStatus getEpicTaskStatusBySubTasksStatuses(List<Long> subTasksIds) {
        if (subTasksIds.isEmpty()) {
            return NEW;
        }
        boolean isNew = true;
        boolean isDone = true;
        for (Long subTaskId : subTasksIds) {
            TaskStatus oldSubTaskStatus = subTasks.get(subTaskId).getStatus();
            if (!NEW.equals(oldSubTaskStatus)) {
                isNew = false;
            }
            if (!DONE.equals(oldSubTaskStatus)) {
                isDone = false;
            }
        }
        if (isNew) {
            return NEW;
        }
        if (isDone) {
            return DONE;
        }
        return IN_PROGRESS;
    }
}
