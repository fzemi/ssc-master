package com.fzemi.sscmaster.task.dto;

import com.fzemi.sscmaster.common.Mapper;
import com.fzemi.sscmaster.task.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperImpl implements Mapper<Task, TaskRequest> {

    @Override
    public TaskRequest mapToDTO(Task task) {
        return TaskRequest.builder()
                .title(task.getTitle())
                .numberOfSubtasks(task.getNumberOfSubtasks())
                .priority(task.getPriority())
                .allConditionsMustBeSatisfied(task.getAllConditionsMustBeSatisfied())
                .organizationUnit(task.getOrganizationUnit())
                .team(task.getTeam())
                .minimumExperienceLevel(task.getMinimumExperienceLevel())
                .maximumAgeInYears(task.getMaximumAgeInYears())
                .build();
    }

    @Override
    public Task mapFromDTO(TaskRequest taskRequest) {
        return Task.builder()
                .title(taskRequest.title())
                .numberOfSubtasks(taskRequest.numberOfSubtasks())
                .priority(taskRequest.priority())
                .allConditionsMustBeSatisfied(taskRequest.allConditionsMustBeSatisfied())
                .organizationUnit(taskRequest.organizationUnit())
                .team(taskRequest.team())
                .minimumExperienceLevel(taskRequest.minimumExperienceLevel())
                .maximumAgeInYears(taskRequest.maximumAgeInYears())
                .build();
    }
}
