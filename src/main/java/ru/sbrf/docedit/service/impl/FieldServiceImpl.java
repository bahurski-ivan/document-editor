package ru.sbrf.docedit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.sbrf.docedit.dao.FieldMetaDao;
import ru.sbrf.docedit.dao.FieldValueDao;
import ru.sbrf.docedit.exception.NoSuchEntityException;
import ru.sbrf.docedit.model.field.Field;
import ru.sbrf.docedit.model.field.FieldFull;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.value.FieldType;
import ru.sbrf.docedit.model.field.value.FieldValue;
import ru.sbrf.docedit.service.FieldService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by SBT-Bakhurskiy-IA on 13.02.2017.
 */
@Component
@Transactional
public class FieldServiceImpl implements FieldService {
    private final FieldMetaDao fieldMetaDao;
    private final FieldValueDao fieldValueDao;


    @Autowired
    public FieldServiceImpl(FieldMetaDao fieldMetaDao, FieldValueDao fieldValueDao) {
        this.fieldMetaDao = fieldMetaDao;
        this.fieldValueDao = fieldValueDao;
    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public FieldFull updateFieldValue(long documentId, long fieldId, FieldValue value) {
        final Field field = new Field(fieldId, documentId, value);
        if (!fieldValueDao.updateFieldValue(field))
            throw new NoSuchEntityException();
        return getDocumentField(documentId, fieldId).orElseThrow(NoSuchEntityException::new);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Optional<FieldFull> getDocumentField(long documentId, long fieldId) {
        final Optional<Field> ff = fieldValueDao.getField(documentId, fieldId);

        if (ff.isPresent()) {
            final Field field = ff.get();
            Optional<FieldMeta> ffm = fieldMetaDao.get(fieldId);
            if (ffm.isPresent())
                return Optional.of(new FieldFull(ffm.get(), field.getValue()));
        }

        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<FieldFull> getAllDocumentFields(long documentId) {
        return fieldValueDao.listDocumentFields(documentId).stream()
                .map(f -> getDocumentField(f.getDocumentId(), f.getFieldId()).orElse(null))
                .filter(f -> f != null)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public FieldMeta create(long templateId, String technicalName, String displayName, FieldType type) {
        final long id = fieldMetaDao.createFieldMeta(new FieldMeta(-1L, templateId, technicalName, displayName, type, Integer.MAX_VALUE));
        return fieldMetaDao.get(id).orElseThrow(NoSuchEntityException::new);
    }

    @Override
    public void update(long fieldId, String technicalName, String displayName, FieldType type, int ordinal) {
        if (!fieldMetaDao.updateFieldMeta(new FieldMeta(fieldId, -1, technicalName, displayName, type, ordinal)))
            throw new NoSuchEntityException();
    }

    @Override
    public void remove(long fieldId) {
        if (!fieldMetaDao.removeFieldMeta(fieldId))
            throw new NoSuchEntityException();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FieldMeta> getOne(long fieldId) {
        return fieldMetaDao.get(fieldId);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    public List<FieldMeta> getAll(long templateId) {
        return fieldMetaDao.listFields(templateId);
    }
}
