package com.fzemi.sscmaster.user.service.impl;

import com.fzemi.sscmaster.modification.entity.Modification;
import com.fzemi.sscmaster.modification.service.ModificationService;
import com.fzemi.sscmaster.task.entity.Task;
import com.fzemi.sscmaster.task.service.TaskService;
import com.fzemi.sscmaster.user.entity.User;
import com.fzemi.sscmaster.user.exception.UserNotFoundException;
import com.fzemi.sscmaster.user.repository.UserRepository;
import com.fzemi.sscmaster.user.service.UserService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModificationService modificationService;
    private final MongoTemplate mongoTemplate;
    private final TaskService taskService;

    public UserServiceImpl(
            UserRepository userRepository,
            ModificationService modificationService,
            MongoTemplate mongoTemplate,
            TaskService taskService
    ) {
        this.userRepository = userRepository;
        this.modificationService = modificationService;
        this.mongoTemplate = mongoTemplate;
        this.taskService = taskService;
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
    @Transactional
    public User updateUser(User user) {
        User existingUser = userRepository.findByIsDeletedFalseAndID(user.getID())
                .orElseThrow(() -> new UserNotFoundException("User with id: " + user.getID() + " not found"));

        setUserUpdateModifications(existingUser, user);

        // prevent from overriding
        user.setCreationDate(existingUser.getCreationDate());
        User updatedUser = userRepository.save(user);
        assignTasksToUser(updatedUser);

        return updatedUser;
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        User exisingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));

        Modification modification = modificationService.deleteModification();

        exisingUser.getModificationHistory().add(modification);
        exisingUser.setIsDeleted(true);

        userRepository.save(exisingUser);
    }

    @Override
    public Date calculateMinUserBirthDate(Integer maximumAgeInYears) {
        LocalDate currentDate = LocalDate.now();

        Date minUserBirthDate = Date.from(currentDate.minusYears(maximumAgeInYears)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());

        return minUserBirthDate;
    }

    @Override
    public List<User> getQualifiedUsers(
            String organizationUnit,
            String team,
            Integer minimumExperienceLevel,
            Integer maximumAgeInYears,
            Boolean allConditionsMustBeSatisfied
    ) {
        List<Criteria> criteria = new ArrayList<>();
        criteria.add(Criteria.where("isDeleted").is(false));

        if (organizationUnit != null) {
            criteria.add(Criteria.where("organizationUnit").is(organizationUnit));
        }

        if (team != null) {
            criteria.add(Criteria.where("team").is(team));
        }

        if (minimumExperienceLevel != null) {
            criteria.add(Criteria.where("experienceLevel").gte(minimumExperienceLevel));
        }

        if(maximumAgeInYears != null) {
            Date minUserBirthDate = calculateMinUserBirthDate(maximumAgeInYears);
            // user has to be born after minUserBirthDate
            criteria.add(Criteria.where("dateOfBirth").gte(minUserBirthDate));
        }

        Query query;

        if(allConditionsMustBeSatisfied)
            query = new Query(new Criteria().andOperator(criteria));
        else
            query = new Query(new Criteria().orOperator(criteria));

        List<User> qualifiedUsers = mongoTemplate.find(query, User.class);

        return qualifiedUsers;
    }

    @Override
    @Transactional
    public void assignTaskToUsers(Task task) {
        List<User> qualifiedUsers = getQualifiedUsers(
                task.getOrganizationUnit(),
                task.getTeam(),
                task.getMinimumExperienceLevel(),
                task.getMaximumAgeInYears(),
                task.getAllConditionsMustBeSatisfied()
        );

        // remove task from users that are not qualified after task or user update
        removeTaskFromNotQualifiedUsers(task, qualifiedUsers);

        qualifiedUsers.forEach(user -> {
            User existingUser = getUser(user.getID());
            if (!user.getTasks().contains(task)) {
                user.getTasks().add(task);
            }
            sortTasksByPriority(user);
            setUserUpdateModifications(existingUser, user);
            userRepository.save(user);
        });
    }

    @Override
    @Transactional
    public void removeTaskFromNotQualifiedUsers(Task task, List<User> qualifiedUsers) {
        List<User> usersWithTask = userRepository.findAllByTaskTasksID(task.getID());

        // remove task from users that are not qualified
        usersWithTask.removeAll(qualifiedUsers);

        usersWithTask.forEach(user -> {
            User existingUser = getUser(user.getID());
            user.getTasks().stream()
                    .filter(t -> t.getID().equals(task.getID()))
                    .findFirst()
                    .ifPresent(taskToRemove -> user.getTasks().remove(taskToRemove));

            sortTasksByPriority(user);
            setUserUpdateModifications(existingUser, user);
            userRepository.save(user);
        });
    }

    @Override
    public void sortTasksByPriority(User user) {
        user.getTasks().sort(Comparator.comparing(Task::getPriority).reversed());
    }

    @Override
    @Transactional
    public void assignTasksToUser(User user) {
        List<Task> allTasks = taskService.getAllTasks();

        User existingUser = getUser(user.getID());

        for (Task task : allTasks) {
            if (taskService.isTaskQualified(task, user)) {
                if (!user.getTasks().contains(task)) {
                    user.getTasks().add(task);
                }
            } else {
                user.getTasks().removeIf(t -> t.getID().equals(task.getID()));
            }
        }
        sortTasksByPriority(user);
        setUserUpdateModifications(existingUser, user);
        userRepository.save(user);
    }

    @Override
    public void setUserUpdateModifications(User user, User updatedUser) {
        Modification modification = modificationService.updateModification(user, updatedUser);

        user.setModificationHistory(updatedUser.getModificationHistory());
        user.getModificationHistory().add(modification);
    }
}
