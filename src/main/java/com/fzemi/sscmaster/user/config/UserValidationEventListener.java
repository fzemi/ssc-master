package com.fzemi.sscmaster.user.config;

import com.fzemi.sscmaster.user.entity.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * User validation event listener
 * Checks if user is valid before saving to database
 */

@Component
public class UserValidationEventListener extends AbstractMongoEventListener<User> {
    private final Validator validator;

    public UserValidationEventListener(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void onBeforeSave(BeforeSaveEvent<User> event) {
        User user = event.getSource();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
