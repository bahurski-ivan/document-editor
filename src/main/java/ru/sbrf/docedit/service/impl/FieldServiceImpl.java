package ru.sbrf.docedit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.sbrf.docedit.dao.FieldMetaDao;
import ru.sbrf.docedit.dao.FieldOrdinalsDao;
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
    private final FieldOrdinalsDao ordinalsDao;

    @Autowired
    public FieldServiceImpl(FieldMetaDao fieldMetaDao, FieldValueDao fieldValueDao, FieldOrdinalsDao ordinalsDao) {
        this.fieldMetaDao = fieldMetaDao;
        this.fieldValueDao = fieldValueDao;
        this.ordinalsDao = ordinalsDao;
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
        final long id = fieldMetaDao.createFieldMeta(new FieldMeta(-1L, templateId, technicalName, displayName, type));
        final List<Long> ordinals = ordinalsDao.getOrderedFields(templateId);
        int ordinal = 0;

        if (!ordinals.isEmpty())
            ordinal = ordinals.size();

        ordinalsDao.update(templateId, id, ordinal);

        return new FieldMeta(id, templateId, technicalName, displayName, type);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public FieldMeta update(long fieldId, String technicalName, String displayName, FieldType type, int ordinal) {
        final Optional<FieldMeta> m = fieldMetaDao.get(fieldId);
        if (m.isPresent()) {
            final FieldMeta meta = m.get();
            final List<Long> fieldsIds = ordinalsDao.getOrderedFields(meta.getTemplateId());
            final FieldMeta result = new FieldMeta(fieldId, meta.getTemplateId(), technicalName, displayName, type);

            if (!fieldMetaDao.updateFieldMeta(result))
                throw new NoSuchEntityException(result.getFieldId());

            // if type was changed -->
            // convert document values that have same field to new type (or null if conversion not defined)
            if (meta.getType() != type)
                fieldValueDao.listFieldValuesByFieldId(meta.getFieldId())
                        .stream()
                        .map(field -> fieldValueDao.getField(field.getDocumentId(), field.getFieldId()).orElse(null))
                        .filter(field -> field != null && field.getValue() != null)
                        .forEach(field -> {
                            final FieldValue oldV = field.getValue();
                            final FieldValue newV = oldV.convertTo(type);
                            fieldValueDao.updateFieldValue(new Field(field.getFieldId(), field.getDocumentId(), newV));
                        });

            if (ordinal < 0)
                ordinal = 0;
            if (ordinal > fieldsIds.size())
                ordinal = fieldsIds.size();

            ordinalsDao.update(meta.getTemplateId(), fieldId, ordinal);

            return result;
        }

        throw new NoSuchEntityException();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void remove(long fieldId) {
        final Optional<FieldMeta> m = fieldMetaDao.get(fieldId);

        if (m.isPresent()) {
            final FieldMeta meta = m.get();
            final List<Long> fieldsIds = ordinalsDao.getOrderedFields(meta.getTemplateId());
            final int index = fieldsIds.indexOf(fieldId);

            if (index != -1) {
                fieldsIds.remove((int) index);
                ordinalsDao.updateBatch(meta.getTemplateId(), fieldsIds);
                fieldMetaDao.removeFieldMeta(meta.getFieldId());
                return;
            }
        }

        throw new NoSuchEntityException();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Optional<FieldMeta> getOne(long fieldId) {
        return fieldMetaDao.get(fieldId);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<FieldMeta> getAll(long templateId) {
        return fieldMetaDao.listFields(templateId);
    }
}
