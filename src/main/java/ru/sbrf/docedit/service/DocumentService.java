package ru.sbrf.docedit.service;

import ru.sbrf.docedit.model.document.DocumentFull;
import ru.sbrf.docedit.model.document.DocumentMeta;
import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.model.pagination.Page;

import java.util.Optional;

/**
 * Created by SBT-Bakhurskiy-IA on 10.02.2017.
 */
public interface DocumentService {
    /**
     * Creates new document.
     */
    DocumentMeta create(long templateId, String documentName);

    /**
     * Removes document.
     */
    void remove(long documentId);

    /**
     * Updates document meta information.
     */
    DocumentMeta update(long documentId, String documentName);

    /**
     * Returns document meta information by it's it.
     */
    Optional<DocumentMeta> get(long documentId);

    /**
     * Lists documents using pagination.
     * <p>
     * Note that result is ordered by {@code DocumentMeta.documentName} field.
     */
    Page<DocumentMeta> list(int pageNo, int pageSize, Order order);

    /**
     * Returns full document (including full information about fields) by it's id.
     * <p>
     * Note that fields returns sorted by it's ordinals.
     */
    Optional<DocumentFull> getFull(long documentId);

    /**
     * Lists documents for given template id.
     */
    Page<DocumentMeta> listForTemplate(int templateId, int pageNo, int pageSize, Order order);
}
