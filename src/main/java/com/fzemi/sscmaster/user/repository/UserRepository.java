package com.fzemi.sscmaster.user.repository;

import com.fzemi.sscmaster.user.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByIsDeletedFalseAndID(String id);
    List<User> findAllByIsDeletedFalse();
    List<User> findAllByIsDeletedTrue();
}
