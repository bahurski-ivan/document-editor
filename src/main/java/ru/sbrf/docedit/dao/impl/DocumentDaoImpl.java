//package ru.sbrf.docedit.dao.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import ru.sbrf.docedit.model.field.Field;
//import ru.sbrf.docedit.dao.DocumentDao;
//import ru.sbrf.docedit.dao.DocumentMetaDao;
//import ru.sbrf.docedit.dao.FieldValueDao;
//import ru.sbrf.docedit.model.document.DocumentFull;
//import ru.sbrf.docedit.model.document.DocumentMeta;
//
///**
// * Created by SBT-Bakhurskiy-IA on 10.02.2017.
// */
//@Component
//public class DocumentDaoImpl implements DocumentDao {
//    private final FieldValueDao fieldValueDao;
//    private final DocumentMetaDao documentMetaDao;
//
//    @Autowired
//    public DocumentDaoImpl(FieldValueDao fieldValueDao, DocumentMetaDao documentMetaDao) {
//        this.fieldValueDao = fieldValueDao;
//        this.documentMetaDao = documentMetaDao;
//    }
//
//    @Override
//    public long createDocument(DocumentFull documentFull) {
//        documentFull.getFields().forEach(fieldValueDao::createFieldValue);
//        return documentMetaDao.createDocument(new DocumentMeta(documentFull.getDocumentId(), documentFull.getTemplateId(), documentFull.getDocumentName()));
//    }
//
//    @Override
//    public boolean removeDocument(long documentId) {
//        return documentMetaDao.removeDocument(documentId);
//    }
//
//    @Override
//    public boolean updateDocumentMeta(DocumentMeta documentMeta) {
//        return documentMetaDao.updateDocument(documentMeta);
//    }
//
//    @Override
//    public boolean updateFieldValue(Field field) {
//        return fieldValueDao.updateFieldValue(field);
//    }
//}
