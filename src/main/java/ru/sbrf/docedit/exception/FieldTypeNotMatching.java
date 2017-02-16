package ru.sbrf.docedit.exception;

import ru.sbrf.docedit.model.field.value.FieldType;
import ru.sbrf.docedit.model.field.value.FieldValue;

/**
 * Created by SBT-Bakhurskiy-IA on 16.02.2017.
 */
public class FieldTypeNotMatching extends RuntimeException {
    private final FieldValue value;
    private final FieldType type;

    public FieldTypeNotMatching(FieldValue value, FieldType type) {
        this.value = value;
        this.type = type;
    }

    public FieldValue getValue() {
        return value;
    }

    public FieldType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "FieldTypeNotMatching{" +
                "value=" + value +
                ", type=" + type +
                '}';
    }
}
