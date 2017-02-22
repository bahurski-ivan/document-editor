package ru.sbrf.docedit.dao;

import ru.sbrf.docedit.model.document.DocumentFull;
import ru.sbrf.docedit.model.document.DocumentMeta;
import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.model.pagination.Page;

import java.util.List;
import java.util.Optional;

/**
 * Interface for DAO of {@code DocumentMeta} class.
 */
public interface DocumentDao {
    /**
     * Creates new document.
     *
     * @param documentMeta meta information about document
     * @return id of created document
     */
    long createDocument(DocumentMeta documentMeta);

    /**
     * Updates document by its id.
     * <p>
     * Note that this method will fill all document information
     * will be set according to {@code newValue}.
     *
     * @param documentId id of document to update
     * @param newValue   new state of document with given id
     * @return {@code true} if document was successfully updated
     */
    boolean updateDocument(long documentId, DocumentMeta newValue);

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
    Optional<DocumentMeta> getDocumentMeta(long documentId);

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


    /**
     * Returns full document.
     * <p>
     * Note that fields (returned by {@code DocumentFull.getFields()} are ordered by ordinal.
     *
     * @param documentId id of the document
     * @return {@code Optional.empty()} if there is no such document
     */
    Optional<DocumentFull> getFullDocument(long documentId);
}
