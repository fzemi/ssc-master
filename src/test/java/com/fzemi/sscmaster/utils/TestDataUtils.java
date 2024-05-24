package com.fzemi.sscmaster.utils;

import com.fzemi.sscmaster.enums.ActionTypeEnum;
import com.fzemi.sscmaster.modification.entity.Modification;
import com.fzemi.sscmaster.task.entity.Task;
import com.fzemi.sscmaster.user.entity.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public final class TestDataUtils {
    private TestDataUtils() {
    }

    private static Date parseDate(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Task createTestTaskA() {
        return Task.builder()
                .title("Test Task A")
                .numberOfSubtasks(0)
                .priority(1)
                .allConditionsMustBeSatisfied(true)
                .organizationUnit("organization_A")
                .team("Recruitment")
                .minimumExperienceLevel(3)
                .maximumAgeInYears(30)
                .build();
    }

    public static Task createTestTaskAWithModificationHistory() {
        return Task.builder()
                .title("Test Task A")
                .numberOfSubtasks(0)
                .priority(1)
                .allConditionsMustBeSatisfied(true)
                .organizationUnit("organization_A")
                .team("Recruitment")
                .minimumExperienceLevel(3)
                .maximumAgeInYears(30)
                .modificationHistory(List.of(getCreateModification()))
                .build();
    }

    public static Task createTestTaskAWithModificationHistory(List<String> modifiedFields) {
        return Task.builder()
                .title("Test Task A")
                .numberOfSubtasks(0)
                .priority(1)
                .allConditionsMustBeSatisfied(true)
                .organizationUnit("organization_A")
                .team("Recruitment")
                .minimumExperienceLevel(3)
                .maximumAgeInYears(30)
                .modificationHistory(List.of(
                            getCreateModification(),
                            getUpdateModification(modifiedFields)
                        ))
                .build();
    }

    public static Task createTestTaskB() {
        return Task.builder()
                .title("Test Task B")
                .numberOfSubtasks(0)
                .priority(2)
                .allConditionsMustBeSatisfied(false)
                .organizationUnit("organization_B")
                .team("Development")
                .minimumExperienceLevel(3)
                .maximumAgeInYears(40)
                .isDeleted(true)
                .build();
    }

    public static Task createTestTaskC() {
        return Task.builder()
                .title("Test Task C")
                .numberOfSubtasks(0)
                .priority(3)
                .allConditionsMustBeSatisfied(true)
                .organizationUnit("organization_C")
                .team("Recruitment")
                .minimumExperienceLevel(3)
                .maximumAgeInYears(30)
                .build();
    }

    private static Modification getCreateModification() {
        return Modification.builder()
                .actionType(ActionTypeEnum.CREATE)
                .build();
    }

    private static Modification getUpdateModification(List<String> modifiedFields) {
        return Modification.builder()
                .actionType(ActionTypeEnum.UPDATE)
                .modifiedFields(modifiedFields)
                .build();
    }

    public static User createTestUserA() {
        return User.builder()
                .firstName("John")
                .lastName("Wick")
                .dateOfBirth(parseDate("2020-01-05"))
                .organizationUnit("organization_A")
                .team("Recruitment")
                .experienceLevel(1)
                .tasks(new ArrayList<>())
                .build();
    }

    public static User createTestUserAWithModificationHistory() {
        return User.builder()
                .firstName("John")
                .lastName("Wick")
                .dateOfBirth(parseDate("2020-01-05"))
                .organizationUnit("organization_A")
                .team("Recruitment")
                .experienceLevel(1)
                .tasks(new ArrayList<>())
                .modificationHistory(List.of(getCreateModification()))
                .build();
    }

    public static User createTestUserAWithModificationHistory(List<String> modifiedFields) {
        return User.builder()
                .firstName("John")
                .lastName("Wick")
                .dateOfBirth(parseDate("2020-01-05"))
                .organizationUnit("organization_A")
                .team("Recruitment")
                .experienceLevel(1)
                .tasks(new ArrayList<>())
                .modificationHistory(List.of(
                        getCreateModification(),
                        getUpdateModification(modifiedFields)
                ))
                .build();
    }

    public static User createTestUserB() {
        return User.builder()
                .firstName("John")
                .lastName("Smith")
                .dateOfBirth(parseDate("2020-01-05"))
                .organizationUnit("organization_B")
                .team("Development")
                .experienceLevel(3)
                .tasks(new ArrayList<>())
                .isDeleted(true)
                .build();
    }

    public static User createTestUserC() {
        return User.builder()
                .firstName("John")
                .lastName("Travolta")
                .dateOfBirth(parseDate("2020-01-05"))
                .organizationUnit("organization_C")
                .team("Development")
                .experienceLevel(3)
                .tasks(new ArrayList<>())
                .build();
    }
}
