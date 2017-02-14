package ru.sbrf.docedit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.sbrf.docedit.dao.DocumentMetaDao;
import ru.sbrf.docedit.dao.FieldMetaDao;
import ru.sbrf.docedit.dao.FieldOrdinalsDao;
import ru.sbrf.docedit.dao.FieldValueDao;
import ru.sbrf.docedit.model.document.DocumentFull;
import ru.sbrf.docedit.model.document.DocumentMeta;
import ru.sbrf.docedit.model.field.Field;
import ru.sbrf.docedit.model.field.FieldFull;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.value.FieldValue;
import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.model.pagination.Page;
import ru.sbrf.docedit.service.DocumentService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by SBT-Bakhurskiy-IA on 13.02.2017.
 */
@Component
@Transactional
public class DocumentServiceImpl implements DocumentService {
    private final DocumentMetaDao documentMetaDao;
    private final FieldValueDao fieldValueDao;
    private final FieldMetaDao fieldMetaDao;
    private final FieldOrdinalsDao ordinalsDao;

    @Autowired
    public DocumentServiceImpl(DocumentMetaDao documentMetaDao, FieldValueDao fieldValueDao, FieldMetaDao fieldMetaDao, FieldOrdinalsDao ordinalsDao) {
        this.documentMetaDao = documentMetaDao;
        this.fieldValueDao = fieldValueDao;
        this.fieldMetaDao = fieldMetaDao;
        this.ordinalsDao = ordinalsDao;
    }

    @Override
    public DocumentMeta create(long templateId, String documentName) {
        final long id = documentMetaDao.createDocument(new DocumentMeta(-1, templateId, documentName));
//        final List<FieldMeta> templateFields = fieldMetaDao.listFields(templateId);
//        templateFields.forEach(m -> fieldValueDao.createFieldValue(new Field(m.getFieldId(), id, null)));
        return new DocumentMeta(id, templateId, documentName);
    }

    @Override
    public void remove(long documentId) {
        documentMetaDao.removeDocument(documentId);
    }

    @Override
    public DocumentMeta update(long documentId, String documentName) {
        final Optional<DocumentMeta> meta = documentMetaDao.get(documentId);

        if (!meta.isPresent())
            return null;

        final DocumentMeta newMeta = new DocumentMeta(documentId, meta.get().getTemplateId(), documentName);
        documentMetaDao.updateDocument(newMeta);
        return newMeta;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentMeta> get(long documentId) {
        return documentMetaDao.get(documentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentMeta> list(int pageNo, int pageSize, Order order) {
        return documentMetaDao.listPaged(pageNo, pageSize, order);
    }

    @Override
    public Optional<DocumentFull> getFull(long documentId) {
        final Optional<DocumentMeta> dd = documentMetaDao.get(documentId);

        if (dd.isPresent()) {
            final DocumentMeta meta = dd.get();
            final long templateId = meta.getTemplateId();
            final List<Long> orderedIds = ordinalsDao.getOrderedFields(templateId);

            final Map<Long, Integer> fieldIdIndexMap = IntStream
                    .range(0, orderedIds.size()).mapToObj(i -> i)
                    .collect(Collectors.toMap(orderedIds::get, i -> i));
            final Map<Long, FieldValue> fieldValueMap = fieldValueDao
                    .listDocumentFields(meta.getDocumentId()).stream()
                    .collect(Collectors.toMap(Field::getFieldId, Field::getValue));
            final List<FieldMeta> metaList = fieldMetaDao.listFields(templateId);

            assert orderedIds.size() == metaList.size();

            return Optional.of(new DocumentFull(meta.getDocumentId(),
                    meta.getTemplateId(),
                    meta.getDocumentName(),

                    metaList.stream().sorted((f1, f2) -> {
                        final int i1 = fieldIdIndexMap.get(f1.getFieldId());
                        final int i2 = fieldIdIndexMap.get(f2.getFieldId());
                        return Integer.compare(i1, i2);
                    })
                            .map(fm -> new FieldFull(fm, fieldValueMap.get(fm.getFieldId())))
                            .collect(Collectors.toList()))
            );
        }

        return Optional.empty();
    }

    @Override
    public Page<DocumentMeta> listForTemplate(int templateId, int pageNo, int pageSize, Order order) {
        return documentMetaDao.listForTemplate(templateId, pageNo, pageSize, order);
    }
}
