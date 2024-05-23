package com.fzemi.sscmaster.task;

import com.fzemi.sscmaster.task.entity.Task;
import com.fzemi.sscmaster.task.repository.TaskRepository;
import com.fzemi.sscmaster.utils.TestDataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class TaskRepositoryIntegrationTests {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskRepositoryIntegrationTests(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Test
    public void testThatTaskCanBeCreatedAndRecalled() {
        // Given:
        Task task = TestDataUtils.createTestTaskA();

        // When:
        taskRepository.insert(task);
        Optional<Task> result = taskRepository.findById(task.getID());

        // Then:
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(task);
    }

    @Test
    public void testThatTasksCanBeFoundByIsDeletedFalse() {
        Task taskA = TestDataUtils.createTestTaskA();
        Task taskB = TestDataUtils.createTestTaskB(); // isDeleted = true

        taskRepository.insert(List.of(taskA, taskB));
        List<Task> result = taskRepository.findAllByIsDeletedFalse();

        assertThat(result)
                .isNotEmpty()
                .contains(taskA)
                .doesNotContain(taskB);
    }

    @Test
    public void testThatTasksCanBeFoundByIsDeletedTrue() {
        Task taskA = TestDataUtils.createTestTaskA();
        Task taskB = TestDataUtils.createTestTaskB(); // isDeleted = true

        taskRepository.insert(List.of(taskA, taskB));
        List<Task> result = taskRepository.findAllByIsDeletedTrue();

        assertThat(result)
                .isNotEmpty()
                .contains(taskB)
                .doesNotContain(taskA);
    }
}
