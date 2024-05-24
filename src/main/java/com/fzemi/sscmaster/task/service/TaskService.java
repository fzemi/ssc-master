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
     * Get qualified tasks for updated user
     * @param user updated user
     * @return list of qualified tasks for updated user
     */
    List<Task> getQualifiedTasks(User user);

    /**
     * Check if task is qualified for user
     * @param task task
     * @param user user
     * @return <code>true</code> if task is qualified for user, <code>false</code> otherwise
     */
    boolean isTaskQualified(Task task, User user);

    /**
     * Check if all conditions are satisfied for user
     * @param task task
     * @param user user
     * @param minUserBirthDate minimum user birth date
     * @return <code>true</code> if all conditions are satisfied, <code>false</code> otherwise
     */
    boolean isAllConditionsSatisfied(Task task, User user, Date minUserBirthDate);

    /**
     * Check if any condition is satisfied for user
     * @param task task
     * @param user user
     * @param minUserBirthDate minimum user birth date
     * @return <code>true</code> if any condition is satisfied, <code>false</code> otherwise
     */
    boolean isAnyConditionSatisfied(Task task, User user, Date minUserBirthDate);

    /**
     * Check if organization unit is valid
     * @param task task
     * @param organizationUnit organization unit
     * @return <code>true</code> if organization unit is valid or null, <code>false</code> otherwise
     */
    boolean isOrganizationUnitValid(Task task, String organizationUnit);

    /**
     * Check if team is valid
     * @param task task
     * @param team team
     * @return <code>true</code> if team is valid or null, <code>false</code> otherwise
     */
    boolean isTeamValid(Task task, String team);

    /**
     * Check if experience level is valid
     * @param task task
     * @param minimumExperienceLevel minimum experience level
     * @return <code>true</code> if experience level is valid or null, <code>false</code> otherwise
     */
    boolean isExperienceLevelValid(Task task, Integer minimumExperienceLevel);

    /**
     * Check if birthdate is valid
     * @param task task
     * @param userBirthDate user birthdate
     * @param minUserBirthDate minimum user birth date
     * @return <code>true</code> if birthdate is valid or task's maximumAgeInYears is null, <code>false</code> otherwise
     */
    boolean isBirthDateValid(Task task, Date userBirthDate, Date minUserBirthDate);
}
