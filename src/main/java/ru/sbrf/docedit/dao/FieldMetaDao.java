package ru.sbrf.docedit.dao;

import ru.sbrf.docedit.model.field.FieldMeta;

import java.util.List;
import java.util.Optional;

/**
 * Interface for DAO of {@code FieldMeta} class.
 */
public interface FieldMetaDao {
    /**
     * Creates new {@code FieldMeta}.
     */
    long createFieldMeta(FieldMeta fieldMeta);

    /**
     * Lists all fields for template with given {@code templateId}.
     *
     * @param templateId id of template for which list all fields
     * @return list of fields for given {@code templateId}
     */
    List<FieldMeta> listFields(long templateId);

    /**
     * Removes field meta with given id.
     *
     * @param fieldId id of field meta to remove
     * @return {@code true} if given {@code fieldId} was actually removed
     */
    boolean removeFieldMeta(long fieldId);

    /**
     * Updates field meta with new value.
     *
     * @param fieldMeta value to update
     * @return {@code true} if field meta was successfully updated
     */
    boolean updateFieldMeta(FieldMeta fieldMeta);

    Optional<FieldMeta> get(long fieldId);
}
