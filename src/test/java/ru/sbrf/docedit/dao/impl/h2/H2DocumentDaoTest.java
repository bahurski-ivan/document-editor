package ru.sbrf.docedit.dao.impl.h2;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import junit.framework.AssertionFailedError;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sbrf.docedit.AbstractDbTest;
import ru.sbrf.docedit.model.document.DocumentMeta;
import ru.sbrf.docedit.model.pagination.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by SBT-Bakhurskiy-IA on 14.02.2017.
 */
@DatabaseSetup("classpath:dataset/DocumentMetaDaoDataSet.xml")
@DatabaseTearDown("classpath:dataset/DocumentMetaDaoDataSet.xml")
public class H2DocumentDaoTest extends AbstractDbTest {
    private static final List<DocumentMeta> ALL_DOCUMENTS = new ArrayList<>();

    static {
        ALL_DOCUMENTS.add(new DocumentMeta(1, 1, "document#2"));
        ALL_DOCUMENTS.add(new DocumentMeta(2, 1, "document#3"));
        ALL_DOCUMENTS.add(new DocumentMeta(3, 1, "document#4"));
    }

    @Autowired
    private H2DocumentDao documentMetaDao;

    @Test
    public void createDocument() throws Exception {
        final DocumentMeta meta = new DocumentMeta(0, 0, "document#1");
        final long id = documentMetaDao.createDocument(meta);
        final Optional<DocumentMeta> saved = documentMetaDao.getDocumentMeta(id);

        assertEquals(id, 0);
        assertTrue(saved.isPresent());
        assertEquals(saved.get(), meta);
    }

    @Test
    public void updateDocument() throws Exception {
        final DocumentMeta meta = new DocumentMeta(1, 1, "document#20");
        assertTrue(documentMetaDao.updateDocument(meta));
        assertEquals(meta, documentMetaDao.getDocumentMeta(1).orElseThrow(AssertionFailedError::new));
    }

    @Test
    public void listAllDocuments() throws Exception {
        assertEquals(documentMetaDao.listAllDocuments(), ALL_DOCUMENTS);
    }

    @Test
    public void removeDocument() throws Exception {
        final List<DocumentMeta> newV = new ArrayList<>(ALL_DOCUMENTS);
        newV.remove(0);
        assertTrue(documentMetaDao.removeDocument(1));
        assertEquals(newV, documentMetaDao.listAllDocuments());
    }

    @Test
    public void get() throws Exception {
        final Optional<DocumentMeta> dd = documentMetaDao.getDocumentMeta(1);
        assertEquals(dd.orElse(null), ALL_DOCUMENTS.get(0));
    }

    @Test
    public void listPaged() throws Exception {
        final List<DocumentMeta> newV = new ArrayList<>(ALL_DOCUMENTS);
        newV.remove(0);
        assertTrue(documentMetaDao.listPaged(0, 2, Order.DESC).getItems().containsAll(newV));
        assertTrue(newV.containsAll(documentMetaDao.listPaged(0, 2, Order.DESC).getItems()));
    }

    @Test
    public void listForTemplate() throws Exception {
        assertEquals(documentMetaDao.listForTemplate(1, 0, Integer.MAX_VALUE, Order.ASC).getItems(), ALL_DOCUMENTS);
        assertEquals(documentMetaDao.listForTemplate(0, 0, Integer.MAX_VALUE, Order.ASC).getItems(), Collections.emptyList());
    }
}