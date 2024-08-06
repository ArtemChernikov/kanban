package ru.praktikum.service;

import ru.praktikum.exception.TaskManagerLoadException;
import ru.praktikum.exception.TaskManagerSaveException;
import ru.praktikum.exception.ValidationException;
import ru.praktikum.model.EpicTask;
import ru.praktikum.model.SubTask;
import ru.praktikum.model.Task;
import ru.praktikum.model.enums.TaskStatus;
import ru.praktikum.model.enums.TaskType;
import ru.praktikum.util.Managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.praktikum.util.Constants.DATA_LOAD_ERROR_MESSAGE;
import static ru.praktikum.util.Constants.DATA_SAVE_ERROR_MESSAGE;
import static ru.praktikum.util.Constants.INCORRECT_TASK_TYPE_MESSAGE;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 20.07.2024
 */
public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTaskManager(HistoryManager historyManager, String path) {
        super(historyManager);
        file = new File(path);
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager dataFromFile = new FileBackedTaskManager(Managers.getDefaultHistory(), file.getPath());
        StringBuilder stringFile = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                stringFile.append(reader.readLine());
                stringFile.append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new TaskManagerLoadException(DATA_LOAD_ERROR_MESSAGE + e.getMessage());
        }
        putDataFromFile(dataFromFile, stringFile);
        return dataFromFile;
    }

    @Override
    public Optional<Task> addNewTask(Task task) {
        Optional<Task> newTask = super.addNewTask(task);
        save();
        return newTask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskByIdAndType(Long id, TaskType taskType) {
        super.deleteTaskByIdAndType(id, taskType);
        save();
    }

    @Override
    public void deleteAllTasksByType(TaskType taskType) {
        super.deleteAllTasksByType(taskType);
        save();
    }

    private File getFile() {
        return file;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();

            for (Task task : getTasks().values()) {
                writer.write(taskToString(task));
                writer.newLine();
            }
            for (SubTask subTask : getSubTasks().values()) {
                writer.write(taskToString(subTask));
                writer.newLine();
            }
            for (EpicTask epicTask : getEpicTasks().values()) {
                writer.write(taskToString(epicTask));
                writer.newLine();
            }
            writer.newLine();
        } catch (IOException e) {
            throw new TaskManagerSaveException(DATA_SAVE_ERROR_MESSAGE);
        }
    }

    private String taskToString(Task task) {
        return task.toCSV();
    }

    private Task fromString(String str) {
        try {
            String[] element = str.split(",");
            long id = Integer.parseInt(element[0]);
            TaskType taskType = TaskType.valueOf(element[1]);
            String name = element[2];
            TaskStatus taskStatus = TaskStatus.valueOf(element[3]);
            String desc = element[4];
            switch (taskType) {
                case TASK -> {
                    return new Task(id, name, desc, taskStatus, taskType);
                }
                case SUBTASK -> {
                    long epicId = Integer.parseInt(element[5]);
                    return new SubTask(id, name, desc, taskStatus, taskType, new EpicTask(epicId));
                }
                case EPIC_TASK -> {
                    return new EpicTask(id, name, desc, taskStatus, taskType);
                }
                default -> throw new ValidationException(INCORRECT_TASK_TYPE_MESSAGE);
            }
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private static void putDataFromFile(FileBackedTaskManager dataFromFile, StringBuilder stringFile) {
        List<Task> tasks = getAllTasksFromFile(dataFromFile, stringFile);
        long nextId = 0;

        for (Task task : tasks) {
            Long taskId = task.getId();
            if (taskId > nextId) {
                nextId = taskId;
            }
            switch (task.getType()) {
                case TASK -> dataFromFile.getTasks().put(task.getId(), task);
                case SUBTASK -> dataFromFile.getSubTasks().put(task.getId(), (SubTask) task);
                case EPIC_TASK -> dataFromFile.getEpicTasks().put(task.getId(), (EpicTask) task);
            }
        }

        dataFromFile.setNextId(nextId + 1);

        dataFromFile.getSubTasks().values().forEach(subTask -> {
            long epicId = subTask.getEpicTask().getId();
            EpicTask epicTask = dataFromFile.getEpicTasks().get(epicId);
            epicTask.addSubTask(subTask);
            subTask.setEpicTask(epicTask);
        });
    }

    private static List<Task> getAllTasksFromFile(FileBackedTaskManager dataFromFile, StringBuilder stringFile) {
        String[] lines = stringFile.toString().split(System.lineSeparator());
        List<Task> tasks = new ArrayList<>();
        for (int i = 1; i < lines.length; i++) {
            tasks.add(dataFromFile.fromString(lines[i]));
        }
        return tasks;
    }
}
