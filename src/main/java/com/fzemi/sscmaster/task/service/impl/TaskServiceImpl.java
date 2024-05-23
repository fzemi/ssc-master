package com.fzemi.sscmaster.task.service.impl;

import com.fzemi.sscmaster.modification.entity.Modification;
import com.fzemi.sscmaster.modification.service.ModificationService;
import com.fzemi.sscmaster.task.entity.Task;
import com.fzemi.sscmaster.task.exception.TaskNotFoundException;
import com.fzemi.sscmaster.task.repository.TaskRepository;
import com.fzemi.sscmaster.task.service.TaskService;
import com.fzemi.sscmaster.user.entity.User;
import com.fzemi.sscmaster.user.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ModificationService modificationService;
    private final UserService userService;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            ModificationService modificationService,
            @Lazy UserService userService
    ) {
        this.taskRepository = taskRepository;
        this.modificationService = modificationService;
        this.userService = userService;
    }

    @Override
    public Task getTask(String id) {
        return taskRepository.findByIsDeletedFalseAndID(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id: " + id + " not found"));
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAllByIsDeletedFalse();
    }

    @Override
    public List<Task> getAllDeletedTasks() {
        return taskRepository.findAllByIsDeletedTrue();
    }

    @Override
    @Transactional
    public Task createTask(Task task) {
        Modification modification = modificationService.createModification();
        task.getModificationHistory().add(modification);

        Task createdTask = taskRepository.insert(task);
        userService.assignTaskToUsers(createdTask);

        return createdTask;
    }

    @Override
    @Transactional
    public Task updateTask(Task task) {
        Task existingTask = taskRepository.findByIsDeletedFalseAndID(task.getID())
                .orElseThrow(() -> new TaskNotFoundException("Task with id: " + task.getID() + " not found"));

        Modification modification = modificationService.updateModification(existingTask, task);

        task.setModificationHistory(existingTask.getModificationHistory());
        task.getModificationHistory().add(modification);

        // prevent from overriding
        task.setCreationDate(existingTask.getCreationDate());

        Task updatedTask = taskRepository.save(task);
        userService.assignTaskToUsers(updatedTask);

        return updatedTask;
    }

    @Override
    @Transactional
    public void deleteTask(String id) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id: " + id + " not found"));

        Modification modification = modificationService.deleteModification();

        existingTask.getModificationHistory().add(modification);
        existingTask.setIsDeleted(true);

        taskRepository.save(existingTask);
    }

    @Override
    public List<Task> getQualifiedTasks(User user) {
        List<Task> allTasks = taskRepository.findAllByIsDeletedFalse();
        List<Task> qualifiedTasks = new ArrayList<>();

        for (Task task : allTasks) {
            if (isTaskQualified(task, user)) {
                qualifiedTasks.add(task);
            }
        }

        return qualifiedTasks;
    }

    @Override
    public boolean isTaskQualified(Task task, User user) {
        Date minUserBirthDate = userService.calculateMinUserBirthDate(task.getMaximumAgeInYears());
        if (task.getAllConditionsMustBeSatisfied()) {
            return isAllConditionsSatisfied(task, user, minUserBirthDate);
        } else {
            return isAnyConditionSatisfied(task, user, minUserBirthDate);
        }
    }

    @Override
    public boolean isAllConditionsSatisfied(Task task, User user, Date minUserBirthDate) {
        return isOrganizationUnitValid(task, user.getOrganizationUnit())
                && isTeamValid(task, user.getTeam())
                && isExperienceLevelValid(task, user.getExperienceLevel())
                && isBirthDateValid(task, user.getDateOfBirth(), minUserBirthDate);
    }

    @Override
    public boolean isAnyConditionSatisfied(Task task, User user, Date minUserBirthDate) {
        return isOrganizationUnitValid(task, user.getOrganizationUnit())
                || isTeamValid(task, user.getTeam())
                || isExperienceLevelValid(task, user.getExperienceLevel())
                || isBirthDateValid(task, user.getDateOfBirth(), minUserBirthDate);
    }

    @Override
    public boolean isOrganizationUnitValid(Task task, String organizationUnit) {
        return organizationUnit == null || organizationUnit.equals(task.getOrganizationUnit());
    }

    @Override
    public boolean isTeamValid(Task task, String team) {
        return team == null || team.equals(task.getTeam());
    }

    @Override
    public boolean isExperienceLevelValid(Task task, Integer minimumExperienceLevel) {
        return minimumExperienceLevel == null || minimumExperienceLevel >= task.getMinimumExperienceLevel();
    }

    @Override
    public boolean isBirthDateValid(Task task, Date userBirthDate, Date minUserBirthDate) {
        return userBirthDate == null || userBirthDate.after(minUserBirthDate);
    }
}
