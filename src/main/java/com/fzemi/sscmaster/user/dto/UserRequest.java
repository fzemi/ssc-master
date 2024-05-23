package com.fzemi.sscmaster.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Builder
public record UserRequest(
        @NotBlank
        @Pattern(
                regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]*$",
                message = "First name must start with an uppercase letter, no spaces allowed and no special characters allowed"
        )
        String firstName,

        @NotBlank
        @Pattern(
                regexp = "^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]*$",
                message = "Last name must start with an uppercase letter, no spaces allowed and no special characters allowed"
        )
        String lastName,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date dateOfBirth,

        @Pattern(regexp = "organization_A|organization_B|organization_C")
        String organizationUnit,

        @NotBlank
        @Pattern(
                regexp = "^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ ]*$",
                message = "Team name must contain only letters and spaces"
        )
        String team,

        @NotNull
        @Range(
                min = 1,
                max = 3,
                message = "Experience level must be between 1 and 3"
        )
        Integer experienceLevel
) {
}
