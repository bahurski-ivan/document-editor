package ru.sbrf.docedit.model.document;


import ru.sbrf.docedit.model.field.FieldFull;

import java.util.List;
import java.util.Objects;

/**
 * Represents a document.
 */
public class DocumentFull {
    private final long documentId;
    private final long templateId;
    private final String documentName;
    private final List<FieldFull> fields;

    public DocumentFull(long documentId, long templateId, String documentName, List<FieldFull> fields) {
        Objects.requireNonNull(documentName);
        Objects.requireNonNull(fields);

        this.documentId = documentId;
        this.templateId = templateId;
        this.documentName = documentName;
        this.fields = fields;
    }

    public long getTemplateId() {
        return templateId;
    }

    public long getDocumentId() {
        return documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public List<FieldFull> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return "DocumentFull{" +
                "documentId=" + documentId +
                ", templateId=" + templateId +
                ", documentName='" + documentName + '\'' +
                ", fields=" + fields +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocumentFull that = (DocumentFull) o;

        if (documentId != that.documentId) return false;
        if (templateId != that.templateId) return false;
        if (!documentName.equals(that.documentName)) return false;
        return fields.equals(that.fields);

    }

    @Override
    public int hashCode() {
        int result = (int) (documentId ^ (documentId >>> 32));
        result = 31 * result + (int) (templateId ^ (templateId >>> 32));
        result = 31 * result + documentName.hashCode();
        result = 31 * result + fields.hashCode();
        return result;
    }
}
