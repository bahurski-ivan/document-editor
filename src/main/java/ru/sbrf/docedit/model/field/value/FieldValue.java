package ru.sbrf.docedit.model.field.value;

import java.io.Serializable;

/**
 * Marker class for field value classes.
 */
public interface FieldValue extends Serializable {
    /**
     * Converts field value to different type.
     *
     * @param type result type
     * @return new {@code FieldUpdate} instance if conversion exist{@literal ,} otherwise {@code null}
     */
    FieldValue convertTo(FieldType type);
}

