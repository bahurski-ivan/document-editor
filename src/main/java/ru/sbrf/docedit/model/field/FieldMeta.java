package ru.sbrf.docedit.model.field;

import ru.sbrf.docedit.model.field.value.FieldType;

import java.util.Objects;

/**
 * Represents meta information about field inside a document.
 */
public class FieldMeta {
    private final long fieldId;
    private final long templateId;
    private final String technicalName;
    private final String displayName;
    private final FieldType type;
    private final int ordinal;

    /**
     * Create field meta with {@code Integer.MAX_VALUE} ordinal.
     */
    public FieldMeta(long fieldId, long templateId, String technicalName, String displayName, FieldType type) {
        this.fieldId = fieldId;
        this.templateId = templateId;
        this.technicalName = technicalName;
        this.displayName = displayName;
        this.type = type;
        this.ordinal = Integer.MAX_VALUE;
    }

    public FieldMeta(long fieldId, long templateId, String technicalName, String displayName, FieldType type, int ordinal) {
        Objects.requireNonNull(technicalName);
        Objects.requireNonNull(displayName);
        Objects.requireNonNull(type);

        this.fieldId = fieldId;
        this.templateId = templateId;
        this.technicalName = technicalName;
        this.displayName = displayName;
        this.type = type;
        this.ordinal = ordinal;
    }

    public FieldType getType() {
        return type;
    }

    public String getTechnicalName() {
        return technicalName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getFieldId() {
        return fieldId;
    }

    public long getTemplateId() {
        return templateId;
    }

    public int getOrdinal() {
        return ordinal;
    }

    @Override
    public String toString() {
        return "FieldMeta{" +
                "fieldId=" + fieldId +
                ", templateId=" + templateId +
                ", technicalName='" + technicalName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", type=" + type +
                ", ordinal=" + ordinal +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldMeta meta = (FieldMeta) o;

        if (fieldId != meta.fieldId) return false;
        if (templateId != meta.templateId) return false;
        if (ordinal != meta.ordinal) return false;
        if (!technicalName.equals(meta.technicalName)) return false;
        if (!displayName.equals(meta.displayName)) return false;
        return type == meta.type;

    }

    @Override
    public int hashCode() {
        int result = (int) (fieldId ^ (fieldId >>> 32));
        result = 31 * result + (int) (templateId ^ (templateId >>> 32));
        result = 31 * result + technicalName.hashCode();
        result = 31 * result + displayName.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + ordinal;
        return result;
    }
}
