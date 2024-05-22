package com.fzemi.sscmaster.modification.service.impl;

import com.fzemi.sscmaster.enums.ActionTypeEnum;
import com.fzemi.sscmaster.modification.entity.Modification;
import com.fzemi.sscmaster.modification.service.ModificationService;
import com.fzemi.sscmaster.utils.ObjectComparator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ModificationServiceImpl implements ModificationService {

    @Override
    public Modification createModification() {
        return Modification.builder()
                .timestamp(new Date())
                .actionType(ActionTypeEnum.CREATE)
                .build();
    }

    @Override
    public Modification deleteModification() {
        return Modification.builder()
                .timestamp(new Date())
                .actionType(ActionTypeEnum.DELETE)
                .modifiedFields(List.of("isDeleted: false -> true"))
                .build();
    }

    @Override
    public <T> Modification updateModification(T obj1, T obj2) {
        try {
            Map<String, String[]> modifiedFields = ObjectComparator.compareObjects(obj1, obj2);
            List<String> modifiedFieldsList = new ArrayList<>();

            modifiedFields.forEach((k, v) -> {
                modifiedFieldsList.add(k + ": " + v[0] + " -> " + v[1]);
            });

            return Modification.builder()
                    .timestamp(new Date())
                    .actionType(ActionTypeEnum.UPDATE)
                    .modifiedFields(modifiedFieldsList)
                    .build();
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error while comparing objects" + e.getMessage());
        }
    }
}
