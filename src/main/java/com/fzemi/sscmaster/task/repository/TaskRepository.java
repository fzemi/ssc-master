package com.fzemi.sscmaster.task.repository;

import com.fzemi.sscmaster.task.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task, String>{
    Optional<Task> findByIsDeletedFalseAndID(String id);
    List<Task> findAllByIsDeletedFalse();
    List<Task> findAllByIsDeletedTrue();
}
