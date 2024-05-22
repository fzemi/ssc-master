package com.fzemi.sscmaster.user.service;

import com.fzemi.sscmaster.user.entity.User;

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
}
