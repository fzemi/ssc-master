package com.fzemi.sscmaster.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fzemi.sscmaster.common.BaseEntity;
import com.fzemi.sscmaster.task.entity.Task;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "users")
public class User extends BaseEntity {


    /**
     * User's first name
     */
    @NotBlank
    @Pattern(
            regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]*$",
            message = "First name must start with an uppercase letter, no spaces allowed and no special characters allowed"
    )
    private String firstName;

    /**
     * User's last name
     */
    @NotBlank
    @Pattern(
            regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]*$",
            message = "Last name must start with an uppercase letter, no spaces allowed and no special characters allowed"
    )
    private String lastName;

    /**
     * User's date of birth
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    /**
     * User's organization unit
     */
    @Pattern(regexp = "organization_A|organization_B|organization_C")
    private String organizationUnit;

    /**
     * User's team
     */
    @NotBlank
    @Pattern(
            regexp = "^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ ]*$",
            message = "Team name must contain only letters and spaces"
    )
    private String team;

    /**
     * User's experience level
     */
    @NotNull
    @Range(
            min = 1,
            max = 3,
            message = "Experience level must be between 1 and 3"
    )
    private Integer experienceLevel;

    @DBRef
    private List<Task> tasks = new ArrayList<>();

    public void setDateOfBirth(Date dateOfBirth) {
        if (dateOfBirth != null) {
            // Ensure the date is after 1900-01-01
            Calendar calendar = Calendar.getInstance();
            calendar.set(1900, Calendar.JANUARY, 1);
            Date minDate = calendar.getTime();

            if (dateOfBirth.before(minDate) || dateOfBirth.after(minDate)) {
                throw new IllegalArgumentException("Date of birth must be past and after 1900-01-01");
            }
        }

        this.dateOfBirth = dateOfBirth;
    }
}
