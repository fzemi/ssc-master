package com.fzemi.sscmaster.task.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fzemi.sscmaster.common.BaseEntity;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "tasks")
public class Task extends BaseEntity {

    /**
     * Task's title
     */
    @NotBlank
    @Pattern(
            regexp = "^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ ]*$",
            message = "Title must contain only letters and spaces"
    )
    private String title;

    /**
     * Task's number of subtasks
     */
    @NotNull
    @Min(
            value = 0,
            message = "Number of subtasks must be greater or equal than 0"
    )
    private Integer numberOfSubtasks;

    /**
     * Task's priority
     */
    @NotNull
    @Range(
            min = 0,
            max = 10,
            message = "Priority must be between 0 and 10"
    )
    private Integer priority;

    @JsonIgnore
    private Boolean allConditionsMustBeSatisfied;

    /**
     * Task's organization unit
     */
    // TODO: Change to enum
    @Pattern(regexp = "organization_A|organization_B|organization_C")
    private String organizationUnit;

    /**
     * Task's team
     */
    @Pattern(
            regexp = "^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ ]*$",
            message = "Team name must contain only letters and spaces or null"
    )
    private String team;

    /**
     * Task's minimum experience level
     */
    @Range(
            min = 1,
            max = 3,
            message = "Minimum experience level must be between 1 and 3"
    )
    private Integer minimumExperienceLevel;

    /**
     * Task's maximum age in years
     */
    @Min(
            value = 0,
            message = "Minimum age in years must be greater or equal than 0"
    )
    private Integer maximumAgeInYears;

    @AssertTrue(
            message = "Team or " +
            "minimum experience level or " +
            "organization unit or " +
            "maximum age in years must not be null"
    )
    private boolean isAnyFieldNotNull() {
        return team != null || minimumExperienceLevel != null
                || organizationUnit != null || maximumAgeInYears != null;
    }
}
