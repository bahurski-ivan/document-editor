package ru.sbrf.docedit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.sbrf.docedit.dao.DocumentDao;
import ru.sbrf.docedit.dao.FieldDao;
import ru.sbrf.docedit.exception.DBOperation;
import ru.sbrf.docedit.exception.EntityType;
import ru.sbrf.docedit.exception.NoSuchEntityException;
import ru.sbrf.docedit.exception.NoSuchEntityInfo;
import ru.sbrf.docedit.model.document.DocumentMeta;
import ru.sbrf.docedit.model.field.FieldFull;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.FieldValueHolder;
import ru.sbrf.docedit.model.field.value.FieldValue;
import ru.sbrf.docedit.service.FieldService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by SBT-Bakhurskiy-IA on 13.02.2017.
 */
@Component
public class FieldServiceImpl implements FieldService {
    private final FieldDao fieldDao;
    private final DocumentDao documentDao;

    @Autowired
    public FieldServiceImpl(FieldDao fieldDao, DocumentDao documentDao) {
        this.fieldDao = fieldDao;
        this.documentDao = documentDao;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateFieldValue(long documentId, long fieldId, FieldValue value) {
        fieldDao.setFieldValue(documentId, fieldId, value);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Optional<FieldFull> getDocumentField(long documentId, long fieldId) {
        final Optional<FieldValueHolder> ff = fieldDao.getFieldValue(documentId, fieldId);

        if (ff.isPresent()) {
            final FieldValueHolder field = ff.get();
            Optional<FieldMeta> ffm = fieldDao.getFieldMeta(fieldId);
            if (ffm.isPresent())
                return Optional.of(new FieldFull(ffm.get(), field.getValue()));
        }

        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<FieldFull> getAllDocumentFields(long documentId) {
        final Optional<DocumentMeta> dd = documentDao.getDocumentMeta(documentId);

        if (dd.isPresent()) {
            final DocumentMeta documentMeta = dd.get();
            final Map<Long, FieldValue> setFieldsMap = fieldDao.getDocumentNonEmptyFields(documentId);
            return fieldDao.getTemplateFields(documentMeta.getTemplateId())
                    .stream()
                    .map(m -> new FieldFull(m, setFieldsMap.get(m.getFieldId())))
                    .collect(Collectors.toList());
        }

        throw NoSuchEntityException.ofSingle(new NoSuchEntityInfo(documentId, EntityType.DOCUMENT, DBOperation.GET));
    }

    @Override
    public FieldMeta create(FieldMeta meta) {
        final long id = fieldDao.createFieldMeta(meta);
        return fieldDao.getFieldMeta(id).orElseThrow(() -> NoSuchEntityException.ofSingle(new NoSuchEntityInfo(meta.getFieldId(), EntityType.FIELD, DBOperation.CREATE)));
    }

    @Override
    @Transactional
    public void update(long fieldId, FieldMeta.Update update) {
        if (!fieldDao.updateFieldMeta(fieldId, update))
            throw NoSuchEntityException.ofSingle(new NoSuchEntityInfo(fieldId, EntityType.FIELD, DBOperation.UPDATE));
    }

    @Override
    @Transactional
    public void remove(long fieldId) {
        if (!fieldDao.removeFieldMeta(fieldId))
            throw NoSuchEntityException.ofSingle(new NoSuchEntityInfo(fieldId, EntityType.FIELD, DBOperation.REMOVE));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FieldMeta> getOne(long fieldId) {
        return fieldDao.getFieldMeta(fieldId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FieldMeta> getAll(long templateId) {
        return fieldDao.getTemplateFields(templateId);
    }
}
