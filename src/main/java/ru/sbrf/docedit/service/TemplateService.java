package ru.sbrf.docedit.service;

import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.model.pagination.Page;
import ru.sbrf.docedit.model.template.TemplateFull;
import ru.sbrf.docedit.model.template.TemplateMeta;

import java.util.Optional;

/**
 * Created by SBT-Bakhurskiy-IA on 13.02.2017.
 */
public interface TemplateService {
    /**
     * Creates new template.
     */
    TemplateMeta create(String templateName);

    /**
     * Updates template.
     */
    TemplateMeta update(long templateId, String templateName);

    /**
     * Removes template.
     */
    void remove(long templateId);

    /**
     * Returns template meta information by it's id.
     */
    Optional<TemplateMeta> get(long templateId);

    /**
     * Lists templates.
     * <p>
     * Note that result sorted by {@code TemplateMeta.templateName} field in {@code order} order.
     */
    Page<TemplateMeta> list(int pageNo, int pageSize, Order order);

    /**
     * Returns full template by it's id.
     * <p>
     * Note that fields returns sorted by it's ordinals.
     */
    Optional<TemplateFull> getFull(long templateId);
}
