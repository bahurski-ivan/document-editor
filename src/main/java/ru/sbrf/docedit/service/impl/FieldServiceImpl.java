package ru.sbrf.docedit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sbrf.docedit.dao.FieldMetaDao;
import ru.sbrf.docedit.dao.FieldOrdinalsDao;
import ru.sbrf.docedit.dao.FieldValueDao;
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
    public FieldFull updateFieldValue(long documentId, long fieldId, FieldValue value) {
        final Field field = new Field(fieldId, documentId, value);
        fieldValueDao.updateFieldValue(field);
        final Optional<FieldFull> ff = getDocumentField(documentId, fieldId);
        assert ff.isPresent();
        return ff.get();
    }

    @Override
    public Optional<FieldFull> getDocumentField(long documentId, long fieldId) {
        final Optional<Field> ff = fieldValueDao.getField(documentId, fieldId);

        if (ff.isPresent()) {
            final Field field = ff.get();
            Optional<FieldMeta> ffm = fieldMetaDao.get(fieldId);

            assert ffm.isPresent();

            return Optional.of(new FieldFull(ffm.get(), field.getValue()));
        }

        return Optional.empty();
    }

    @Override
    public List<FieldFull> getAllDocumentFields(long documentId) {
        return fieldValueDao.listDocumentFields(documentId).stream()
                .map(f -> getDocumentField(f.getDocumentId(), f.getFieldId()))
                .map(f -> {
                    assert f.isPresent();
                    return f.get();
                })
                .collect(Collectors.toList());
    }


    @Override
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
    public FieldMeta update(long fieldId, String technicalName, String displayName, FieldType type, int ordinal) {
        final Optional<FieldMeta> m = fieldMetaDao.get(fieldId);
        if (m.isPresent()) {
            final FieldMeta meta = m.get();
            final List<Long> fieldsIds = ordinalsDao.getOrderedFields(meta.getTemplateId());
            final FieldMeta result = new FieldMeta(fieldId, meta.getTemplateId(), technicalName, displayName, type);
            fieldMetaDao.updateFieldMeta(result);

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

        return null;
    }

    @Override
    public void remove(long fieldId) {
        final Optional<FieldMeta> m = fieldMetaDao.get(fieldId);

        if (m.isPresent()) {
            final FieldMeta meta = m.get();
            final List<Long> fieldsIds = ordinalsDao.getOrderedFields(meta.getTemplateId());
            final int index = fieldsIds.indexOf(fieldId);
            fieldsIds.remove((int) index);
            ordinalsDao.updateBatch(meta.getTemplateId(), fieldsIds);
            fieldMetaDao.removeFieldMeta(meta.getFieldId());
        }
    }

    @Override
    public Optional<FieldMeta> getOne(long fieldId) {
        return fieldMetaDao.get(fieldId);
    }

    @Override
    public List<FieldMeta> getAll(long templateId) {
        return fieldMetaDao.listFields(templateId);
    }
}
