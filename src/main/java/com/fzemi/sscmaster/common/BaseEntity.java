package com.fzemi.sscmaster.common;

import com.fzemi.sscmaster.modification.entity.Modification;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class BaseEntity {

    @Id
    private String ID;

    private Boolean isDeleted = false;

    List<Modification> modificationHistory = new ArrayList<>();

    @CreatedDate
    private Date creationDate;
}
