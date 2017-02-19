package ru.sbrf.docedit.model.template;

import ru.sbrf.docedit.model.update.FieldUpdate;

import java.util.Objects;

/**
 * Contains only meta information about document without fields meta information.
 */
public class TemplateMeta {
    private final long templateId;
    private final String templateName;


    public TemplateMeta(long templateId, String templateName) {
        Objects.requireNonNull(templateName);

        this.templateId = templateId;
        this.templateName = templateName;
    }

    public long getTemplateId() {
        return templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    @Override
    public String toString() {
        return "TemplateMeta{" +
                "templateId=" + templateId +
                ", templateName='" + templateName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TemplateMeta meta = (TemplateMeta) o;

        if (templateId != meta.templateId) return false;
        return templateName.equals(meta.templateName);

    }

    @Override
    public int hashCode() {
        int result = (int) (templateId ^ (templateId >>> 32));
        result = 31 * result + templateName.hashCode();
        return result;
    }

    /**
     * Update information holder.
     */
    public static class Update {
        private final FieldUpdate<String> templateName = new FieldUpdate<>();

        public FieldUpdate<String> getTemplateName() {
            return templateName;
        }

        public Update setTemplateName(String templateName) {
            this.templateName.setValue(templateName);
            return this;
        }
    }
}
