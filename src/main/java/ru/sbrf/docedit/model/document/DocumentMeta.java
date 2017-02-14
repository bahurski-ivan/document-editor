package ru.sbrf.docedit.model.document;

import java.util.Objects;

/**
 * Represents document's meta information.
 */
public class DocumentMeta {
    private final long documentId;
    private final long templateId;
    private final String documentName;

    public DocumentMeta(long documentId, long templateId, String documentName) {
        Objects.requireNonNull(documentName);

        this.documentId = documentId;
        this.templateId = templateId;
        this.documentName = documentName;
    }

    public long getDocumentId() {
        return documentId;
    }

    public long getTemplateId() {
        return templateId;
    }

    public String getDocumentName() {
        return documentName;
    }

    @Override
    public String toString() {
        return "DocumentMeta{" +
                "documentId=" + documentId +
                ", templateId=" + templateId +
                ", documentName='" + documentName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocumentMeta meta = (DocumentMeta) o;

        if (documentId != meta.documentId) return false;
        if (templateId != meta.templateId) return false;
        return documentName.equals(meta.documentName);

    }

    @Override
    public int hashCode() {
        int result = (int) (documentId ^ (documentId >>> 32));
        result = 31 * result + (int) (templateId ^ (templateId >>> 32));
        result = 31 * result + documentName.hashCode();
        return result;
    }
}
