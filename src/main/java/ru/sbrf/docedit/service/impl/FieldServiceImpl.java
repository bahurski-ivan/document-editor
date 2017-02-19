package ru.sbrf.docedit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.sbrf.docedit.dao.DocumentDao;
import ru.sbrf.docedit.dao.FieldDao;
import ru.sbrf.docedit.exception.NoSuchEntityException;
import ru.sbrf.docedit.model.document.DocumentMeta;
import ru.sbrf.docedit.model.field.FieldFull;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.FieldValueHolder;
import ru.sbrf.docedit.model.field.value.FieldType;
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
@Transactional
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
        if (!fieldDao.setFieldValue(documentId, fieldId, value))
            throw new NoSuchEntityException();
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

        throw new NoSuchEntityException();
    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public FieldMeta create(long templateId, String technicalName, String displayName, FieldType type) {
        final long id = fieldDao.createFieldMeta(new FieldMeta(-1L, templateId, technicalName, displayName, type, Integer.MAX_VALUE));
        return fieldDao.getFieldMeta(id).orElseThrow(NoSuchEntityException::new);
    }

    @Override
    public void update(long fieldId, FieldMeta.Update update) {
        if (!fieldDao.updateFieldMeta(fieldId, update))
            throw new NoSuchEntityException();
    }

    @Override
    public void remove(long fieldId) {
        if (!fieldDao.removeFieldMeta(fieldId))
            throw new NoSuchEntityException();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FieldMeta> getOne(long fieldId) {
        return fieldDao.getFieldMeta(fieldId);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    public List<FieldMeta> getAll(long templateId) {
        return fieldDao.getTemplateFields(templateId);
    }
}
