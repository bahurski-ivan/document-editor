package ru.sbrf.docedit.model.field;

import ru.sbrf.docedit.model.field.value.FieldValue;

import java.util.Objects;

/**
 * Holder for meta and value information of the field inside template.
 */
public class FieldFull {
    private final FieldMeta meta;
    private final FieldValue value;

    public FieldFull(FieldMeta meta, FieldValue value) {
        Objects.requireNonNull(meta);

        this.meta = meta;
        this.value = value;
    }

    public FieldMeta getMeta() {
        return meta;
    }

    public FieldValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "FieldFull{" +
                "meta=" + meta +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldFull fieldFull = (FieldFull) o;

        if (!meta.equals(fieldFull.meta)) return false;
        return value != null ? value.equals(fieldFull.value) : fieldFull.value == null;

    }

    @Override
    public int hashCode() {
        int result = meta.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
