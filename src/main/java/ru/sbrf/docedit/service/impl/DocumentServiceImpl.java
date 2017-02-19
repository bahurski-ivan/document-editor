package ru.sbrf.docedit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.sbrf.docedit.dao.DocumentDao;
import ru.sbrf.docedit.exception.NoSuchEntityException;
import ru.sbrf.docedit.model.document.DocumentFull;
import ru.sbrf.docedit.model.document.DocumentMeta;
import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.model.pagination.Page;
import ru.sbrf.docedit.service.DocumentService;

import java.util.Optional;

/**
 * Created by SBT-Bakhurskiy-IA on 13.02.2017.
 */
@Component
@Transactional(isolation = Isolation.READ_COMMITTED)
public class DocumentServiceImpl implements DocumentService {
    private final DocumentDao documentDao;

    @Autowired
    public DocumentServiceImpl(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }

    @Override
    public DocumentMeta create(long templateId, String documentName) {
        final long id = documentDao.createDocument(new DocumentMeta(-1, templateId, documentName));
        return new DocumentMeta(id, templateId, documentName);
    }

    @Override
    public void remove(long documentId) {
        documentDao.removeDocument(documentId);
    }

    @Override
    public void update(long documentId, DocumentMeta.Update update) {
        final DocumentMeta meta = documentDao.getDocumentMeta(documentId).orElseThrow(NoSuchEntityException::new);
        documentDao.updateDocument(documentId, update);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentMeta> get(long documentId) {
        return documentDao.getDocumentMeta(documentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentMeta> list(int pageNo, int pageSize, Order order) {
        return documentDao.listPaged(pageNo, pageSize, order);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    public Optional<DocumentFull> getFull(long documentId) {
        return documentDao.getFullDocument(documentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentMeta> listForTemplate(int templateId, int pageNo, int pageSize, Order order) {
        return documentDao.listForTemplate(templateId, pageNo, pageSize, order);
    }
}
