package com.fzemi.sscmaster.user.repository;

import com.fzemi.sscmaster.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByIsDeletedFalseAndID(String id);
    List<User> findAllByIsDeletedFalse();
    List<User> findAllByIsDeletedTrue();

    @Query("{ 'tasks.ID' : ?0 }")
    List<User> findAllByTaskTasksID(String taskID);
}
