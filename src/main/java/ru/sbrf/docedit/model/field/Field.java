package ru.sbrf.docedit.model.field;

import ru.sbrf.docedit.model.field.value.FieldValue;

/**
 * Represents document's field information and it's value.
 */
public class Field {
    private final long fieldId;
    private final long documentId;
    private final FieldValue value;

    public Field(long fieldId, long documentId, FieldValue value) {
        this.fieldId = fieldId;
        this.documentId = documentId;
        this.value = value;
    }

    public long getFieldId() {
        return fieldId;
    }

    public long getDocumentId() {
        return documentId;
    }

    public FieldValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Field{" +
                "fieldId=" + fieldId +
                ", documentId=" + documentId +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (fieldId != field.fieldId) return false;
        if (documentId != field.documentId) return false;
        return value != null ? value.equals(field.value) : field.value == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (fieldId ^ (fieldId >>> 32));
        result = 31 * result + (int) (documentId ^ (documentId >>> 32));
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
