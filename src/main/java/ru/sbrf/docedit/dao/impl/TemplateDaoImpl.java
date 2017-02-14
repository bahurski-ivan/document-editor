//package ru.sbrf.docedit.dao.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import ru.sbrf.docedit.dao.FieldMetaDao;
//import ru.sbrf.docedit.dao.TemplateDao;
//import ru.sbrf.docedit.dao.TemplateMetaDao;
//import ru.sbrf.docedit.model.field.FieldMeta;
//import ru.sbrf.docedit.model.template.TemplateFull;
//import ru.sbrf.docedit.model.template.TemplateMeta;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
///**
// * Created by SBT-Bakhurskiy-IA on 10.02.2017.
// */
//@Component
//public class TemplateDaoImpl implements TemplateDao {
//    private final TemplateMetaDao templateMetaDao;
//    private final FieldMetaDao fieldMetaDao;
//
//    @Autowired
//    public TemplateDaoImpl(TemplateMetaDao templateMetaDao, FieldMetaDao fieldMetaDao) {
//        this.templateMetaDao = templateMetaDao;
//        this.fieldMetaDao = fieldMetaDao;
//    }
//
//    @Override
//    public List<TemplateFull> listAll() {
//        return templateMetaDao.listAll().stream()
//                .map(m -> new TemplateFull(m.getTemplateId(), m.getTemplateName(), fieldMetaDao.listFields(m.getTemplateId())))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public long createTemplate(TemplateFull templateFull) {
//        final List<FieldMeta> fields = templateFull.getFields();
//        final List<Long> ordinals = fields.stream()
//                .peek(f -> {
//                    if (f.getTemplateId() != templateFull.getTemplateId())
//                        throw new IllegalArgumentException();
//                })
//                .map(fieldMetaDao::createFieldMeta)
//                .collect(Collectors.toList());
//        return templateMetaDao.createTemplate(new TemplateMeta(templateFull.getTemplateId(), templateFull.getTemplateName(), ordinals));
//    }
//
//    @Override
//    public boolean remoteTemplateById(long templateId) {
//        return templateMetaDao.removeTemplateMeta(templateId);
//    }
//
//    @Override
//    public boolean updateTemplateMeta(TemplateMeta meta) {
//        return templateMetaDao.updateTemplateName(meta);
//    }
//
//    @Override
//    public boolean updateTemplateField(FieldMeta meta) {
//        return fieldMetaDao.updateFieldMeta(meta);
//    }
//
//    @Override
//    public boolean addFieldsToTemplate(long templateId, List<FieldMeta> fields) {
//        final Optional<TemplateMeta> meta = templateMetaDao.getTemplate(templateId);
//        if (!meta.isPresent())
//            return false;
//        final TemplateMeta m = meta.get();
//        final List<Long> addedOrdinals = fields.stream()
//                .map(fieldMetaDao::createFieldMeta)
//                .collect(Collectors.toList());
//
//        addedOrdinals.addAll(m.getFieldsOrdinal());
//
//        final TemplateMeta newMeta = new TemplateMeta(m.getTemplateId(), m.getTemplateName(), addedOrdinals);
//        return templateMetaDao.updateTemplateName(newMeta);
//    }
//
//    @Override
//    public boolean removeFieldsFromTemplate(long templateId, List<FieldMeta> fields) {
//        final Optional<TemplateMeta> meta = templateMetaDao.getTemplate(templateId);
//        if (!meta.isPresent())
//            return false;
//        final TemplateMeta m = meta.get();
//        final List<Long> removedOrdinals = fields.stream()
//                .map(FieldMeta::getFieldId)
//                .peek(fieldMetaDao::removeFieldMeta)
//                .collect(Collectors.toList());
//
//        final List<Long> ordinals = new ArrayList<>(m.getFieldsOrdinal());
//        ordinals.removeAll(removedOrdinals);
//
//        final TemplateMeta newMeta = new TemplateMeta(m.getTemplateId(), m.getTemplateName(), ordinals);
//        return templateMetaDao.updateTemplateName(newMeta);
//    }
//
//    @Override
//    public List<FieldMeta> listTemplateFields(long templateId) {
//        return fieldMetaDao.listFields(templateId);
//    }
//}
