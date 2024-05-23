package com.fzemi.sscmaster.task.config;

import com.fzemi.sscmaster.task.entity.Task;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Task validation event listener
 * Checks if task is valid before saving to database
 */

@Component
public class TaskValidationEventListener extends AbstractMongoEventListener<Task> {
    private final Validator validator;

    public TaskValidationEventListener(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void onBeforeSave(BeforeSaveEvent<Task> event) {
        Task task = event.getSource();

        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
