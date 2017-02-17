package ru.sbrf.docedit.service;

import ru.sbrf.docedit.model.field.FieldFull;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.value.FieldType;
import ru.sbrf.docedit.model.field.value.FieldValue;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface FieldService {

    // Document related methods

    /**
     * Updates document field value.
     *
     * @param documentId id of document where this field is located
     * @param fieldId    id of field
     * @param value      new value
     * @return updated field
     */
    FieldFull updateFieldValue(long documentId, long fieldId, FieldValue value);

    /**
     * Returns document field.
     */
    Optional<FieldFull> getDocumentField(long documentId, long fieldId);

    /**
     * Returns list of document fields.
     */
    List<FieldFull> getAllDocumentFields(long documentId);


    // Template related methods

    /**
     * Creates new field for template with {@code templateId}.
     * <p>
     * Field is appended to the end of template, so ordinal of this
     * field is equal to previous last field in this template.
     *
     * @param templateId    id of template to append this field
     * @param technicalName technical name of the field
     * @param displayName   field name that will be displayed to the user
     * @param type          type of field
     * @return newly created field
     */
    FieldMeta create(long templateId, String technicalName, String displayName, FieldType type);

    /**
     * Updates field meta information with id equals to {@code fieldId}.
     * <p>
     * Note if {@code ordinal < 0} then this method will treat {@code ordinal} as 0
     * if {@code ordinal > fieldsCount} it will be equal to {@code fieldsCount + 1}.
     *
     * @param fieldId       id of field to update
     * @param technicalName technical name of the field
     * @param displayName   field name that will be displayed to the user
     * @param type          type of field
     * @return updated field meta information
     */
    void update(long fieldId, String technicalName, String displayName, FieldType type, int ordinal);

    /**
     * Removes field from template.
     *
     * @param fieldId id of field to remove
     */
    void remove(long fieldId);

    /**
     * Returns field meta information for {@code fieldId}.
     */
    Optional<FieldMeta> getOne(long fieldId);

    /**
     * Returns all fields for template with id equals to {@code templateId}.
     * <p>
     * Note that this method returns fields in there display order.
     */
    List<FieldMeta> getAll(long templateId);
}
