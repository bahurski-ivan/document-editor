package ru.sbrf.docedit.service;

import ru.sbrf.docedit.model.field.FieldFull;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.value.FieldValue;

import java.util.List;
import java.util.Map;
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
     */
    void updateFieldValue(long documentId, long fieldId, FieldValue value);

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
     * FieldValueHolder is appended to the end of template, so ordinal of this
     * field is equal to previous last field in this template.
     *
     * @param meta new value (id field is irrelevant)
     * @return newly created field
     */
    FieldMeta create(FieldMeta meta);

    /**
     * Updates field meta information with id equals to {@code fieldId}.
     * <p>
     * Note if {@code ordinal < 0} then this method will treat {@code ordinal} as 0
     * if {@code ordinal > fieldsCount} it will be equal to {@code fieldsCount + 1}.
     *
     * @param fieldId id of field to update
     * @param update  update info
     */
    void update(long fieldId, FieldMeta.Update update);

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

    /**
     * Returns map fieldId {@literal ->} ordinal.
     *
     * @param templateId target template
     * @return mapping fieldId {@literal ->} ordinal
     */
    Map<Long, Integer> getOrdinalMap(long templateId);
}
