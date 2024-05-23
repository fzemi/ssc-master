package com.fzemi.sscmaster.task.service;

import com.fzemi.sscmaster.task.entity.Task;
import com.fzemi.sscmaster.user.entity.User;

import java.util.Date;
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

    /**
     * Get qualified tasks for user
     * @param user user
     * @return list of qualified tasks for user
     */
    List<Task> getQualifiedTasks(User user);

    boolean isTaskQualified(Task task, User user);
    boolean isAllConditionsSatisfied(Task task, User user, Date minUserBirthDate);
    boolean isAnyConditionSatisfied(Task task, User user, Date minUserBirthDate);
    boolean isOrganizationUnitValid(Task task, String organizationUnit);
    boolean isTeamValid(Task task, String team);
    boolean isExperienceLevelValid(Task task, Integer minimumExperienceLevel);
    boolean isBirthDateValid(Task task, Date userBirthDate, Date minUserBirthDate);
}
