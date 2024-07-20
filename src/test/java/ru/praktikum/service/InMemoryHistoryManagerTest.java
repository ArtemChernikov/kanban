package ru.praktikum.service;

import org.junit.jupiter.api.Test;
import ru.praktikum.model.Task;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void whenAddTask() {
        Task task = Task.builder()
                .id(1L)
                .name("Task1")
                .build();
        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertThat(task).isEqualTo(history.get(0));
    }

    @Test
    void whenAddCopyTask() {
        Task task = Task.builder()
                .id(1L)
                .name("Task1")
                .build();
        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(task);
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertThat(history.size()).isEqualTo(1);
        assertThat(task).isEqualTo(history.get(0));
    }

    @Test
    void whenAddUpdatedTask() {
        Task task = Task.builder()
                .id(1L)
                .name("Task1")
                .build();
        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(task);
        task.setName("NewTask");
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertThat(history.size()).isEqualTo(1);
        assertThat(task).isEqualTo(history.get(0));
        assertThat(task.getName()).isEqualTo(history.get(0).getName());
    }

    @Test
    void whenRemove() {
        Task task1 = Task.builder()
                .id(1L)
                .name("Task1")
                .build();
        Task task2 = Task.builder()
                .id(2L)
                .name("Task2")
                .build();
        Task task3 = Task.builder()
                .id(3L)
                .name("Task3")
                .build();
        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2.getId());
        List<Task> history = historyManager.getHistory();
        assertThat(history.size()).isEqualTo(2);
        assertThat(task1).isEqualTo(history.get(0));
        assertThat(task3).isEqualTo(history.get(1));
    }

}