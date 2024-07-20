package ru.praktikum.service;

import ru.praktikum.exception.NotFoundException;
import ru.praktikum.exception.ValidationException;
import ru.praktikum.model.EpicTask;
import ru.praktikum.model.SubTask;
import ru.praktikum.model.Task;
import ru.praktikum.model.enums.TaskType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.praktikum.util.Constants.INCORRECT_TASK_TYPE_MESSAGE;
import static ru.praktikum.util.Constants.TASK_NOT_FOUND_MESSAGE;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 04.02.2024
 */
public class InMemoryTaskManager implements TaskManager {
    private final Map<Long, Task> tasks = new HashMap<>();
    private final Map<Long, SubTask> subTasks = new HashMap<>();
    private final Map<Long, EpicTask> epicTasks = new HashMap<>();
    private final HistoryManager historyManager;
    private Long id = 1L;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
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
            default -> throw new ValidationException(INCORRECT_TASK_TYPE_MESSAGE);
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
                historyManager.add(task);
                return Optional.of(task);
            }
            case SUBTASK -> {
                if (!subTasks.containsKey(id)) {
                    return Optional.empty();
                }
                SubTask subTask = subTasks.get(id);
                historyManager.add(subTask);
                return Optional.of(subTask);
            }
            case EPIC_TASK -> {
                if (!epicTasks.containsKey(id)) {
                    return Optional.empty();
                }
                EpicTask epicTask = epicTasks.get(id);
                historyManager.add(epicTask);
                return Optional.of(epicTask);
            }
            default -> throw new NotFoundException(TASK_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    public void updateTask(Task task) {
        TaskType type = task.getType();
        switch (type) {
            case TASK -> tasks.computeIfPresent(task.getId(), (key, value) -> task);
            case SUBTASK -> updateSubTask((SubTask) task);
            case EPIC_TASK -> updateEpicTask((EpicTask) task);
            default -> throw new ValidationException(INCORRECT_TASK_TYPE_MESSAGE);
        }
    }

    @Override
    public void deleteTaskByIdAndType(Long id, TaskType type) {
        switch (type) {
            case TASK -> {
                tasks.remove(id);
                historyManager.remove(id);
            }
            case SUBTASK -> {
                deleteSubTaskById(id);
                historyManager.remove(id);
            }
            case EPIC_TASK -> {
                deleteEpicTaskById(id);
                historyManager.remove(id);
            }
            default -> throw new NotFoundException(TASK_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    public void deleteAllTasksByType(TaskType type) {
        switch (type) {
            case TASK -> deleteAllTasks();
            case SUBTASK -> deleteAllSubtasks();
            case EPIC_TASK -> deleteAllEpicTasks();
            default -> throw new ValidationException(INCORRECT_TASK_TYPE_MESSAGE);
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
            default -> throw new ValidationException(INCORRECT_TASK_TYPE_MESSAGE);
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private Optional<Task> addTask(Task task) {
        task.setId(id++);
        tasks.put(task.getId(), task);
        return Optional.of(task);
    }

    private Optional<Task> addSubTask(Task task) {
        SubTask subTask = (SubTask) task;
        EpicTask epicTask = subTask.getEpicTask();
        if (epicTasks.containsKey(epicTask.getId())) {
            subTask.setId(id++);
            epicTask.addSubTask(subTask);
            subTasks.put(subTask.getId(), subTask);
            epicTask.updateStatus();
            return Optional.of(subTask);
        }
        return Optional.empty();
    }

    private Optional<Task> addEpicTask(Task task) {
        EpicTask epicTask = (EpicTask) task;
        epicTask.setId(id++);
        epicTasks.put(epicTask.getId(), epicTask);
        epicTask.updateStatus();
        return Optional.of(epicTask);
    }

    private void updateSubTask(SubTask subTask) {
        Long subTaskId = subTask.getId();
        subTasks.computeIfPresent(subTaskId, (key, value) -> {
            EpicTask epicTask = subTask.getEpicTask();
            epicTask.updateStatus();
            return subTask;
        });
    }

    private void updateEpicTask(EpicTask epicTask) {
        Long epicTaskId = epicTask.getId();
        epicTasks.computeIfPresent(epicTaskId, (key, value) -> {
            epicTask.updateStatus();
            return epicTask;
        });
    }

    private void deleteAllTasks() {
        tasks.keySet().forEach(historyManager::remove);
        tasks.clear();
    }

    private void deleteAllSubtasks() {
        subTasks.keySet().forEach(historyManager::remove);
        subTasks.clear();
        epicTasks.values().forEach(EpicTask::updateStatus);
    }

    private void deleteAllEpicTasks() {
        Iterator<Long> subTasksIterator = subTasks.keySet().iterator();
        while (subTasksIterator.hasNext()) {
            Long subTaskId = subTasksIterator.next();
            historyManager.remove(subTaskId);
            subTasksIterator.remove();
        }

        Iterator<Long> epicTasksIterator = epicTasks.keySet().iterator();
        while (epicTasksIterator.hasNext()) {
            Long epicTaskId = epicTasksIterator.next();
            historyManager.remove(epicTaskId);
            epicTasksIterator.remove();
        }
    }

    private void deleteSubTaskById(Long id) {
        if (subTasks.containsKey(id)) {
            SubTask removedSubTask = subTasks.remove(id);
            EpicTask epicTask = removedSubTask.getEpicTask();
            epicTask.deleteSubTask(removedSubTask);
            epicTask.updateStatus();
        }
    }

    private void deleteEpicTaskById(Long id) {
        if (epicTasks.containsKey(id)) {
            EpicTask removedEpicTask = epicTasks.remove(id);
            removedEpicTask.getSubTasksIds().forEach(subTasks::remove);
        }
    }

    public Map<Long, Task> getTasks() {
        return tasks;
    }

    public Map<Long, SubTask> getSubTasks() {
        return subTasks;
    }

    public Map<Long, EpicTask> getEpicTasks() {
        return epicTasks;
    }

    public void setNextId(long nextId) {
        this.id = nextId;
    }

}
