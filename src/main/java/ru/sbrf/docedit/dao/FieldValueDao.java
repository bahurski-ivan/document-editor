package ru.sbrf.docedit.dao;

import ru.sbrf.docedit.model.field.Field;

import java.util.List;
import java.util.Optional;

/**
 * Interface for DAO of {@code Field} classes.
 */
public interface FieldValueDao {
    /**
     * Creates new field value.
     *
     * @param field new field
     * @return {@code true} if field value was successfully created
     */
    boolean createFieldValue(Field field);

    /**
     * Updates field value.
     *
     * @param field value to update
     * @return {@code true} if field value was successfully updated
     */
    boolean updateFieldValue(Field field);

    /**
     * Removes field value.
     *
     * @param field field to remove
     * @return {@code true} if field value was successfully updated
     */
    boolean removeFieldValue(Field field);

    /**
     * Lists all document fields.
     *
     * @param documentId id of the document
     * @return list of all fields inside document
     */
    List<Field> listDocumentFields(long documentId);

    /**
     * Returns field for given {@code documentId} and {@code fieldId}.
     */
    Optional<Field> getField(long documentId, long fieldId);

    /**
     * Lists field values by field id.
     */
    List<Field> listFieldValuesByFieldId(long fieldId);
}