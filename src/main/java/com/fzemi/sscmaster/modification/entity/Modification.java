package com.fzemi.sscmaster.modification.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fzemi.sscmaster.enums.ActionTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Modification {

    private Date timestamp;
    private ActionTypeEnum actionType;
    private List<String> modifiedFields;
}
