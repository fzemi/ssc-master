package com.fzemi.sscmaster.utils;

import com.fzemi.sscmaster.enums.ActionTypeEnum;
import com.fzemi.sscmaster.modification.entity.Modification;
import com.fzemi.sscmaster.task.entity.Task;

import java.util.List;

public final class TestDataUtils {
    private TestDataUtils() {
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
}
