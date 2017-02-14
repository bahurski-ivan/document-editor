package ru.sbrf.docedit.dao;

import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.model.pagination.Page;
import ru.sbrf.docedit.model.template.TemplateMeta;

import java.util.List;
import java.util.Optional;

/**
 * Interface for {@code TemplateMeta} data access object.
 */
public interface TemplateMetaDao {
    /**
     * Creates new document template.
     *
     * @param templateMeta template meta information
     * @return id of new template
     */
    long createTemplate(TemplateMeta templateMeta);

    /**
     * Updates template by its id.
     * <p>
     * This method will update all fields of {@code TemplateMeta} according to {@code templateMeta} argument.
     *
     * @param templateMeta id of template to update
     * @return {@code true} if template has been updated
     */
    boolean updateTemplateName(TemplateMeta templateMeta);

    /**
     * Lists all saved templates meta information.
     *
     * @return list of all saved templates meta information
     */
    List<TemplateMeta> listAll();

    /**
     * Removes template by it's id.
     *
     * @param templateId id of template to remove
     * @return {@code true} if template was successfully removed
     */
    boolean removeTemplateMeta(long templateId);

    /**
     * Returns template meta with given {@code templateId}
     */
    Optional<TemplateMeta> getTemplate(long templateId);

    /**
     * Lists templates by it's name.
     *
     * @param pageNo   number of page
     * @param pageSize size of page
     * @param order    selection order
     * @return paged result
     */
    Page<TemplateMeta> listPaged(int pageNo, int pageSize, Order order);
}
