package ru.sbrf.docedit.dao;

import ru.sbrf.docedit.model.document.DocumentMeta;
import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.model.pagination.Page;

import java.util.List;
import java.util.Optional;

/**
 * Interface for DAO of {@code DocumentMeta} class.
 */
public interface DocumentMetaDao {
    /**
     * Creates new document.
     *
     * @param documentMeta meta information about document
     * @return id of created document
     */
    long createDocument(DocumentMeta documentMeta);

    /**
     * Updates document.
     *
     * @param newValue value with which to update document
     * @return {@code true} if document was successfully updated
     */
    boolean updateDocument(DocumentMeta newValue);

    /**
     * Lists all saved documents.
     *
     * @return list of all saved documents
     */
    List<DocumentMeta> listAllDocuments();

    /**
     * Removes document by it's id.
     */
    boolean removeDocument(long documentId);

    /**
     * Returns document by it's id.
     */
    Optional<DocumentMeta> get(long documentId);

    /**
     * Lists saved documents like {@code listAllDocuments} method but with pagination.
     *
     * @param pageNo   number of page
     * @param pageSize size of page
     * @return {@code Page} instance
     */
    Page<DocumentMeta> listPaged(int pageNo, int pageSize, Order order);

    /**
     * Lists documents for given template id.
     * <p>
     * Sorting is performed using document name.
     *
     * @param templateId id of template
     * @param pageNo     number of page to retrieve
     * @param pageSize   size of page
     * @param order      order result order
     */
    Page<DocumentMeta> listForTemplate(long templateId, int pageNo, int pageSize, Order order);
}
