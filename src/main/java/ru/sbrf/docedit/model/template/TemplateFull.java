package ru.sbrf.docedit.model.template;

import ru.sbrf.docedit.model.field.FieldMeta;

import java.util.List;
import java.util.Objects;

/**
 * Represents document meta information (document template).
 * <p>
 * Note that {this.getFields()} method return fields in order of there ordinals.
 */
public class TemplateFull {
    private final TemplateMeta templateMeta;
    private final List<FieldMeta> fields;


    public TemplateFull(TemplateMeta templateMeta, List<FieldMeta> fields) {
        Objects.requireNonNull(templateMeta);
        Objects.requireNonNull(fields);

        this.templateMeta = templateMeta;
        this.fields = fields;
    }


    @Override
    public String toString() {
        return "TemplateFull{" +
                "templateMeta=" + templateMeta +
                ", fields=" + fields +
                '}';
    }

    public TemplateMeta getTemplateMeta() {
        return templateMeta;
    }

    public List<FieldMeta> getFields() {
        return fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TemplateFull that = (TemplateFull) o;

        if (!templateMeta.equals(that.templateMeta)) return false;
        return fields.equals(that.fields);
    }

    @Override
    public int hashCode() {
        int result = templateMeta.hashCode();
        result = 31 * result + fields.hashCode();
        return result;
    }
}
