package com.fzemi.sscmaster.user.service;

import com.fzemi.sscmaster.task.entity.Task;
import com.fzemi.sscmaster.user.entity.User;

import java.util.Date;
import java.util.List;

public interface UserService {

    /**
     * Get user by id
     * @param id user id
     * @return user
     * @throws com.fzemi.sscmaster.user.exception.UserNotFoundException if user not found
     */
    User getUser(String id);

    /**
     * Get all users
     * @return list of users
     */
    List<User> getAllUsers();

    /**
     * Get all deleted users
     * @return list of deleted users
     */
    List<User> getAllDeletedUsers();

    /**
     * Create user
     * @param user user entity
     * @return created user
     */
    User createUser(User user);

    /**
     * Fully update user
     * @param user user entity
     * @return updated user
     * @throws com.fzemi.sscmaster.user.exception.UserNotFoundException if user not found
     */
    User updateUser(User user);

    /**
     * Delete user, set's <code>isDeleted</code> property to <code>true</code> in database instead of removing it
     * @param id user id
     * @throws com.fzemi.sscmaster.user.exception.UserNotFoundException if user not found
     */
    void deleteUser(String id);

    /**
     * Calculate minimum user birthdate, so that user will be able to work on new task
     * @param maximumAgeInYears task's maximum age in years
     * @return minimum user birth date
     */
    Date calculateMinUserBirthDate(Integer maximumAgeInYears);

    /**
     * Get qualified users for new or updated task
     * @param organizationUnit task's organization unit
     * @param team task's team
     * @param minimumExperienceLevel task's minimum experience level
     * @param maximumAgeInYears task's maximum age in years
     * @param allConditionsMustBeSatisfied task's boolean, if <code>true</code> all conditions must be satisfied, otherwise any
     * @return list of qualified users for new or updated task
     */
    List<User> getQualifiedUsers(
            String organizationUnit,
            String team,
            Integer minimumExperienceLevel,
            Integer maximumAgeInYears,
            Boolean allConditionsMustBeSatisfied
    );

    /**
     * Assign updated task to users
     * @param task updated task entity
     */
    void assignTaskToUsers(Task task);

    /**
     * Assign tasks to updated user
     * @param user updated user entity
     */
    void assignTasksToUser(User user);

    /**
     * Remove task from not qualified users
     * @param task task entity
     * @param qualifiedUsers list of qualified users
     */
    void removeTaskFromNotQualifiedUsers(Task task, List<User> qualifiedUsers);

    /**
     * Sort user's tasks by priority in descending order
     * @param user user entity
     */
    void sortTasksByPriority(User user);

    /**
     * Set user's modification history when user is updated
     * @param user user entity
     * @param updatedUser updated user entity
     */
    void setUserUpdateModifications(User user, User updatedUser);
}
