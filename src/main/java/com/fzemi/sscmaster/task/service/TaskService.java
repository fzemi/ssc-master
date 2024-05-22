package com.fzemi.sscmaster.task.service;

import com.fzemi.sscmaster.task.entity.Task;

import java.util.List;

public interface TaskService {
    /**
     * Get task by id
     * @param id task id
     * @return task
     * @throws com.fzemi.sscmaster.task.exception.TaskNotFoundException if task not found
     */
    Task getTask(String id);

    /**
     * Get all tasks
     * @return list of tasks
     */
    List<Task> getAllTasks();

    /**
     * Get all deleted tasks
     * @return list of deleted tasks
     */
    List<Task> getAllDeletedTasks();

    /**
     * Create task
     * @param task task entity
     * @return created task
     */
    Task createTask(Task task);

    /**
     * Fully update task
     * @param task task entity
     * @return updated task
     * @throws com.fzemi.sscmaster.task.exception.TaskNotFoundException if task not found
     */
    Task updateTask(Task task);

    /**
     * Delete task, set's <code>isDeleted</code> property to <code>true</code> in database instead of removing it
     * @param id task id
     * @throws com.fzemi.sscmaster.task.exception.TaskNotFoundException if task not found
     */
    void deleteTask(String id);
}
