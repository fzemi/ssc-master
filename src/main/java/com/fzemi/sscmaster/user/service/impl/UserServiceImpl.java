package com.fzemi.sscmaster.user.service.impl;

import com.fzemi.sscmaster.modification.entity.Modification;
import com.fzemi.sscmaster.modification.service.ModificationService;
import com.fzemi.sscmaster.user.entity.User;
import com.fzemi.sscmaster.user.exception.UserNotFoundException;
import com.fzemi.sscmaster.user.repository.UserRepository;
import com.fzemi.sscmaster.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModificationService modificationService;

    public UserServiceImpl(
            UserRepository userRepository,
            ModificationService modificationService
    ) {
        this.userRepository = userRepository;
        this.modificationService = modificationService;
    }

    @Override
    public User getUser(String id) {
        return userRepository.findByIsDeletedFalseAndID(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllByIsDeletedFalse();
    }

    @Override
    public List<User> getAllDeletedUsers() {
        return userRepository.findAllByIsDeletedTrue();
    }

    @Override
    public User createUser(User user) {
        Modification modification = modificationService.createModification();
        user.getModificationHistory().add(modification);

        return userRepository.insert(user);
    }

    @Override
    public User updateUser(User user) {
        User existingUser = userRepository.findByIsDeletedFalseAndID(user.getID())
                .orElseThrow(() -> new UserNotFoundException("User with id: " + user.getID() + " not found"));

        Modification modification = modificationService.updateModification(existingUser, user);

        user.getModificationHistory().addAll(existingUser.getModificationHistory());
        user.getModificationHistory().add(modification);

        // prevent from overriding
        user.setCreationDate(existingUser.getCreationDate());

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String id) {
        User exisingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));

        Modification modification = modificationService.deleteModification();

        exisingUser.getModificationHistory().add(modification);
        exisingUser.setIsDeleted(true);

        userRepository.save(exisingUser);
    }
}
