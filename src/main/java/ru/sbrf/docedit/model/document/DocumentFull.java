package ru.sbrf.docedit.model.document;


import ru.sbrf.docedit.model.field.FieldFull;

import java.util.List;
import java.util.Objects;

/**
 * Represents a document.
 * <p>
 * Note that {@code this.getFields()} method will return fields in there ordinal order.
 */
public class DocumentFull {
    private final DocumentMeta documentMeta;
    private final List<FieldFull> fields;


    public DocumentFull(DocumentMeta documentMeta, List<FieldFull> fields) {
        Objects.requireNonNull(documentMeta);
        Objects.requireNonNull(fields);

        this.documentMeta = documentMeta;
        this.fields = fields;
    }


    public DocumentMeta getDocumentMeta() {
        return documentMeta;
    }

    public List<FieldFull> getFields() {
        return fields;
    }


    @Override
    public String toString() {
        return "DocumentFull{" +
                "documentMeta=" + documentMeta +
                ", fields=" + fields +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocumentFull that = (DocumentFull) o;

        if (!documentMeta.equals(that.documentMeta)) return false;
        return fields.equals(that.fields);
    }

    @Override
    public int hashCode() {
        int result = documentMeta.hashCode();
        result = 31 * result + fields.hashCode();
        return result;
    }
}
