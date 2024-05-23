package com.fzemi.sscmaster.task.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;

@Builder
public record TaskRequest(
        @NotBlank
        @Pattern(
                regexp = "^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ ]*$",
                message = "Title must contain only letters and spaces"
        )
        String title,

        @NotNull
        @Min(
                value = 0,
                message = "Number of subtasks must be greater or equal than 0"
        )
        Integer numberOfSubtasks,

        @NotNull
        @Range(
                min = 0,
                max = 10,
                message = "Priority must be between 0 and 10"
        )
        Integer priority,

        @NotNull
        Boolean allConditionsMustBeSatisfied,

        @Pattern(regexp = "organization_A|organization_B|organization_C")
        String organizationUnit,

        @Pattern(
                regexp = "^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ ]*$",
                message = "Team name must contain only letters and spaces or null"
        )
        String team,

        @Range(
                min = 1,
                max = 3,
                message = "Minimum experience level must be between 1 and 3"
        )
        Integer minimumExperienceLevel,

        @Min(
                value = 0,
                message = "Minimum age in years must be greater or equal than 0"
        )
        Integer maximumAgeInYears
) {
}
