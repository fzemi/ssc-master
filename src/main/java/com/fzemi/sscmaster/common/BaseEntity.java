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

    @Builder.Default
    private Boolean isDeleted = false;

    @Builder.Default
    List<Modification> modificationHistory = new ArrayList<>();

    @CreatedDate
    private Date creationDate;
}
