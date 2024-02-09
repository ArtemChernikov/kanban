package ru.praktikum.service;

import ru.praktikum.model.EpicTask;
import ru.praktikum.model.SubTask;
import ru.praktikum.model.Task;
import ru.praktikum.model.enums.TaskStatus;
import ru.praktikum.model.enums.TaskType;

import java.util.*;

import static ru.praktikum.model.enums.TaskStatus.*;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 04.02.2024
 */
public class InMemoryTaskManager implements TaskManager {
    private static final int MAX_HISTORY_SIZE = 10;
    private static final int FIRST_HISTORY_ELEMENT = 0;
    private final Map<Long, Task> tasks = new HashMap<>();
    private final Map<Long, SubTask> subTasks = new HashMap<>();
    private final Map<Long, EpicTask> epicTasks = new HashMap<>();
    private final List<Task> history = new ArrayList<>();
    private Long id = 1L;

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }

    private void addTaskToHistory(Task task) {
        if (history.size() == MAX_HISTORY_SIZE) {
            history.remove(FIRST_HISTORY_ELEMENT);
        }
        history.add(task);
    }

    @Override
    public Optional<Task> addNewTask(Task task) {
        TaskType type = task.getType();
        switch (type) {
            case TASK -> {
                return addTask(task);
            }
            case SUBTASK -> {
                return addSubTask(task);
            }
            case EPIC_TASK -> {
                return addEpicTask(task);
            }
            default -> {
                System.out.println("Укажите корректный тип задачи");
                return Optional.empty();
            }
        }
    }

    @Override
    public Optional<Task> getTaskByIdAndType(Long id, TaskType type) {
        if (!tasks.containsKey(id) && !subTasks.containsKey(id) && !epicTasks.containsKey(id)) {
            return Optional.empty();
        }
        switch (type) {
            case TASK -> {
                if (!tasks.containsKey(id)) {
                    return Optional.empty();
                }
                Task task = tasks.get(id);
                addTaskToHistory(task);
                return Optional.of(task);
            }
            case SUBTASK -> {
                if (!subTasks.containsKey(id)) {
                    return Optional.empty();
                }
                SubTask subTask = subTasks.get(id);
                addTaskToHistory(subTask);
                return Optional.of(subTask);
            }
            case EPIC_TASK -> {
                if (!epicTasks.containsKey(id)) {
                    return Optional.empty();
                }
                EpicTask epicTask = epicTasks.get(id);
                addTaskToHistory(epicTask);
                return Optional.of(epicTask);
            }
            default -> {
                System.out.println("Задача не найдена");
                return Optional.empty();
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        TaskType type = task.getType();
        switch (type) {
            case TASK -> tasks.computeIfPresent(task.getId(), (key, value) -> task);
            case SUBTASK -> updateSubTask((SubTask) task);
            case EPIC_TASK -> updateEpicTask((EpicTask) task);
            default -> System.out.println("Указан некорректный TaskType");
        }
    }

    @Override
    public void deleteTaskByIdAndType(Long id, TaskType type) {
        switch (type) {
            case TASK -> tasks.remove(id);
            case SUBTASK -> deleteSubTaskById(id);
            case EPIC_TASK -> deleteEpicTaskById(id);
            default -> System.out.println("Задача не найдена");
        }
    }

    @Override
    public void deleteAllTasksByType(TaskType type) {
        switch (type) {
            case TASK -> tasks.clear();
            case SUBTASK -> subTasks.clear();
            case EPIC_TASK -> epicTasks.clear();
            default -> System.out.println("Тип задачи введен некорректно");
        }
    }

    @Override
    public List<Task> getAllTasksByType(TaskType type) {
        switch (type) {
            case TASK -> {
                return new ArrayList<>(tasks.values());
            }
            case SUBTASK -> {
                return new ArrayList<>(subTasks.values());
            }
            case EPIC_TASK -> {
                return new ArrayList<>(epicTasks.values());
            }
            default -> {
                System.out.println("Тип задачи введен некорректно");
                return new ArrayList<>();
            }
        }
    }

    @Override
    public List<SubTask> getAllSubTasksByEpicId(Long id) {
        if (epicTasks.containsKey(id)) {
            EpicTask epicTask = epicTasks.get(id);
            return epicTask.getSubTasksIds().stream()
                    .map(subTasks::get)
                    .toList();
        }
        return new ArrayList<>();
    }

    private Optional<Task> addTask(Task task) {
        task.setId(id++);
        tasks.put(task.getId(), task);
        return Optional.of(task);
    }

    private Optional<Task> addSubTask(Task task) {
        SubTask subTask = (SubTask) task;
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

    private Optional<Task> addEpicTask(Task task) {
        EpicTask epicTask = (EpicTask) task;
        epicTask.setId(id++);
        epicTasks.put(epicTask.getId(), epicTask);
        updateEpicTaskStatus(epicTask.getId());
        return Optional.of(epicTask);
    }

    private void updateSubTask(SubTask subTask) {
        Long subTaskId = subTask.getId();
        subTasks.computeIfPresent(subTaskId, (key, value) -> {
            Long epicId = subTask.getEpicId();
            updateEpicTaskStatus(epicId);
            return subTask;
        });
    }

    private void updateEpicTask(EpicTask epicTask) {
        Long epicTaskId = epicTask.getId();
        epicTasks.computeIfPresent(epicTaskId, (key, value) -> {
            updateEpicTaskStatus(epicTaskId);
            return epicTask;
        });
    }

    private void deleteSubTaskById(Long id) {
        if (subTasks.containsKey(id)) {
            SubTask removedSubTask = subTasks.remove(id);
            EpicTask epicTask = epicTasks.get(removedSubTask.getEpicId());
            epicTask.getSubTasksIds().remove(id);
            updateEpicTaskStatus(removedSubTask.getEpicId());
        }
    }

    private void deleteEpicTaskById(Long id) {
        if (epicTasks.containsKey(id)) {
            EpicTask removedEpicTask = epicTasks.remove(id);
            removedEpicTask.getSubTasksIds().forEach(subTasks::remove);
        }
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
