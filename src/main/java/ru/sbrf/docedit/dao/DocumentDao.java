package ru.sbrf.docedit.dao;

import ru.sbrf.docedit.model.document.DocumentFull;
import ru.sbrf.docedit.model.document.DocumentMeta;
import ru.sbrf.docedit.model.field.Field;

/**
 * DAO for {@code DocumentFull} class.
 */
public interface DocumentDao {
    /**
     * Creates new document.
     *
     * @param documentFull new document
     * @return id of new document
     */
    long createDocument(DocumentFull documentFull);

    /**
     * Removes document.
     *
     * @param documentId id of document to remove
     * @return {@code true} if document was successfully removed
     */
    boolean removeDocument(long documentId);

    /**
     * Updates document meta information.
     *
     * @param documentMeta document meta information
     * @return {@code true} if update was successful
     */
    boolean updateDocumentMeta(DocumentMeta documentMeta);

    /**
     * Updates value field of document.
     *
     * @param field field of document to update
     * @return {@code true} if field was successfully updated
     */
    boolean updateFieldValue(Field field);
}
