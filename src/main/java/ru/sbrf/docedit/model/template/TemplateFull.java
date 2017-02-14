package ru.sbrf.docedit.model.template;

import ru.sbrf.docedit.model.field.FieldMeta;

import java.util.List;
import java.util.Objects;

/**
 * Represents document meta information (document template).
 */
public class TemplateFull {
    private final long templateId;
    private final String templateName;
    private final List<FieldMeta> fields;

    public TemplateFull(long templateId, String templateName, List<FieldMeta> fields) {
        Objects.requireNonNull(templateName);
        Objects.requireNonNull(fields);

        this.templateId = templateId;
        this.templateName = templateName;
        this.fields = fields;
    }

    public long getTemplateId() {
        return templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public List<FieldMeta> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return "TemplateFull{" +
                "templateId=" + templateId +
                ", templateName='" + templateName + '\'' +
                ", fields=" + fields +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TemplateFull that = (TemplateFull) o;

        if (templateId != that.templateId) return false;
        if (!templateName.equals(that.templateName)) return false;
        return fields.equals(that.fields);

    }

    @Override
    public int hashCode() {
        int result = (int) (templateId ^ (templateId >>> 32));
        result = 31 * result + templateName.hashCode();
        result = 31 * result + fields.hashCode();
        return result;
    }
}
