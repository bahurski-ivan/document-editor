package ru.sbrf.docedit.dao;

import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.template.TemplateFull;
import ru.sbrf.docedit.model.template.TemplateMeta;

import java.util.List;

/**
 * DAO for {@code TemplateFull} class.
 */
public interface TemplateDao {
    /**
     * Lists all saved templates.
     *
     * @return list of all saved templates
     */
    List<TemplateFull> listAll();

    /**
     * Creates new template according to values in {@code templateFull} argument.
     *
     * @param templateFull new template
     * @return id of new template
     */
    long createTemplate(TemplateFull templateFull);

    /**
     * Removes template by it's id.
     *
     * @param templateId id of template to remove
     * @return {@code true} if template was successfully removed
     */
    boolean remoteTemplateById(long templateId);

    /**
     * Updates template meta information.
     *
     * @param meta template meta information
     * @return {@code true} if template was successfully updated
     */
    boolean updateTemplateMeta(TemplateMeta meta);

    /**
     * Updates template field.
     *
     * @param meta field meta information
     * @return {@code true} if field was successfully updated
     */
    boolean updateTemplateField(FieldMeta meta);

    /**
     * Adds new fields to template.
     *
     * @param templateId id of target template
     * @param fields     fields to add
     * @return {@code true} if addiction end up successfully
     */
    boolean addFieldsToTemplate(long templateId, List<FieldMeta> fields);

    /**
     * Removes fields from template.
     *
     * @param templateId    id of target template
     * @param fieldMetaList list of fields to remove
     * @return {@code true} if field was successfully removed from template
     */
    boolean removeFieldsFromTemplate(long templateId, List<FieldMeta> fieldMetaList);

    /**
     * Lists all fields inside template.
     *
     * @param templateId id of template to query
     * @return list of all fields that belongs to template with {@code templateId}
     */
    List<FieldMeta> listTemplateFields(long templateId);
}
