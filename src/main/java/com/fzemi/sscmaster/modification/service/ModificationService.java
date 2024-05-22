package com.fzemi.sscmaster.modification.service;

import com.fzemi.sscmaster.modification.entity.Modification;

public interface ModificationService {

    /**
     * Create a new modification object for creation action
     * @return new modification object
     */
    Modification createModification();

    /**
     * Create a new modification object for deletion action
     * @return new modification object
     */
    Modification deleteModification();

    /**
     * Create a new modification object for update action
     * @param obj1 existing object in db
     * @param obj2 new object
     * @return new modification object
     * @param <T> type of the object
     */
    <T> Modification updateModification(T obj1, T obj2);
}
