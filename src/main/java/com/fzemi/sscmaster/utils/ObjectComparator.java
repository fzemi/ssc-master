package com.fzemi.sscmaster.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class to compare two objects of the same class and return the differences between its fields.
 */
public class ObjectComparator {

    public static <T> Map<String, String[]> compareObjects(T obj1, T obj2) throws IllegalAccessException {
        Map<String, String[]> differences = new HashMap<>();

        // Ensure both objects are not null and are of the same class
        if (obj1 == null || obj2 == null) {
            throw new IllegalArgumentException("Both objects must be non-null.");
        }
        if (!obj1.getClass().equals(obj2.getClass())) {
            throw new IllegalArgumentException("Objects must be of the same class.");
        }

        // Get all declared fields of the class
        Field[] fields = obj1.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            // Get field values from both objects
            Object value1 = field.get(obj1);
            Object value2 = field.get(obj2);

            // Check if values are different
            if (!Objects.equals(value1, value2)) {
                differences.put(field.getName(), new String[]{String.valueOf(value1), String.valueOf(value2)});
            }
        }

        return differences;
    }
}