package ru.sbrf.docedit.dao;

import java.util.List;

/**
 * Created by SBT-Bakhurskiy-IA on 13.02.2017.
 */
public interface FieldOrdinalsDao {
    /**
     * Creates ordinal record for {@code fieldId} in template with {@code templateId}.
     *
     * @param templateId id of field holder template
     * @param fieldId    id of field to add
     * @param ordinal    position inside template
     */
    void create(long templateId, long fieldId, int ordinal);

    /**
     * Updates field ordinal.
     *
     * @param templateId id of field holder template
     * @param fieldId    id field to modify
     * @param ordinal    new position inside template
     */
    void update(long templateId, long fieldId, int ordinal);

    /**
     * Updates all fields order inside template.
     *
     * @param templateId       id of field holder template
     * @param orderedFieldsIds ordered fields ids
     */
    void updateBatch(long templateId, List<Long> orderedFieldsIds);

    /**
     * Returns ids of fields inside template, ordered by ordinal.
     *
     * @param templateId id of field holder template
     * @return list of fields ids
     */
    List<Long> getOrderedFields(long templateId);

    /**
     * Returns ordinal for field with id equals to {@code fieldId}.
     *
     * @param templateId id of field holder template
     * @param fieldId    id of field to retrieve
     * @return ordinal of field with id equal to {@code fieldId}
     */
    int getOrdinal(long templateId, long fieldId);
}
