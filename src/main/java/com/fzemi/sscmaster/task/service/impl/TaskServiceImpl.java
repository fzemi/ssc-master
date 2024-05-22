package com.fzemi.sscmaster.task.service.impl;

import com.fzemi.sscmaster.modification.entity.Modification;
import com.fzemi.sscmaster.modification.service.ModificationService;
import com.fzemi.sscmaster.task.entity.Task;
import com.fzemi.sscmaster.task.exception.TaskNotFoundException;
import com.fzemi.sscmaster.task.repository.TaskRepository;
import com.fzemi.sscmaster.task.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ModificationService modificationService;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            ModificationService modificationService
    ) {
        this.taskRepository = taskRepository;
        this.modificationService = modificationService;
    }

    @Override
    public Task getTask(String id) {
        return taskRepository.findByIsDeletedFalseAndID(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id: " + id + " not found"));
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAllByIsDeletedFalse();
    }

    @Override
    public List<Task> getAllDeletedTasks() {
        return taskRepository.findAllByIsDeletedTrue();
    }

    @Override
    public Task createTask(Task task) {
        Modification modification = modificationService.createModification();
        task.getModificationHistory().add(modification);

        return taskRepository.insert(task);
    }

    @Override
    public Task updateTask(Task task) {
        Task existingTask = taskRepository.findByIsDeletedFalseAndID(task.getID())
                .orElseThrow(() -> new TaskNotFoundException("Task with id: " + task.getID() + " not found"));

        Modification modification = modificationService.updateModification(existingTask, task);

        task.getModificationHistory().addAll(existingTask.getModificationHistory());
        task.getModificationHistory().add(modification);

        // prevent from overriding
        task.setCreationDate(existingTask.getCreationDate());

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(String id) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id: " + id + " not found"));

        Modification modification = modificationService.deleteModification();

        existingTask.getModificationHistory().add(modification);
        existingTask.setIsDeleted(true);

        taskRepository.save(existingTask);
    }
}
