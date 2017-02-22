package ru.sbrf.docedit.dao;

import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.FieldValueHolder;
import ru.sbrf.docedit.model.field.value.FieldValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface for DAO of {@code FieldMeta} class.
 */
public interface FieldDao {
    /**
     * Creates new {@code FieldMeta}.
     */
    long createFieldMeta(FieldMeta fieldMeta);

    /**
     * Lists all fields for template with given {@code templateId}.
     * <p>
     * This method will return empty list if there is no template with given id.
     *
     * @param templateId id of template for which list all fields
     * @return list of fields for given {@code templateId}
     */
    List<FieldMeta> getTemplateFields(long templateId);

    /**
     * Removes field meta with given id.
     *
     * @param fieldId id of field meta to remove
     * @return {@code true} if given {@code fieldId} was actually removed
     */
    boolean removeFieldMeta(long fieldId);

    /**
     * Updates field meta with new value.
     * <p>
     * Note that this method will set all fields according to {@code newValue}.
     *
     * @param fieldId  id of the field to update
     * @param newValue new value of field with field id
     * @return {@code true} if field meta was successfully updated
     */
    boolean updateFieldMeta(long fieldId, FieldMeta newValue);

    /**
     * Returns field with given {@code fieldId}.
     */
    Optional<FieldMeta> getFieldMeta(long fieldId);

    /**
     * Returns fields ordinals for given template.
     *
     * @return {code Optional.empty()} if there is no template with given {@code templateId}
     */
    Optional<List<Long>> getOrdinals(long templateId);


    /**
     * Sets up field value in document.
     *
     * @param documentId id of the document
     * @param fieldId    id of the field (in template)
     * @param newValue   new value
     * @return {@code true} if update was successful
     */
    boolean setFieldValue(long documentId, long fieldId, FieldValue newValue);

    /**
     * Returns field value for given document and field ids.
     *
     * @param documentId id of the document
     * @param fieldId    id of the field (in template)
     * @return {@code Optional.empty()} if there is no such document
     * OR field is not belongs to template that this document using
     */
    Optional<FieldValueHolder> getFieldValue(long documentId, long fieldId);

    /**
     * Returns list of non empty (set) fields for document with given id.
     *
     * @param documentId id of the document to retrieve fields
     * @return mapping: fieldId -> field value
     */
    Map<Long, FieldValue> getDocumentNonEmptyFields(long documentId);
}
