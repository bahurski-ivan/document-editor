package ru.sbrf.docedit.service.impl;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sbrf.docedit.AbstractDbTest;
import ru.sbrf.docedit.exception.EmptyUpdate;
import ru.sbrf.docedit.exception.NoSuchEntityException;
import ru.sbrf.docedit.model.document.DocumentFull;
import ru.sbrf.docedit.model.document.DocumentMeta;
import ru.sbrf.docedit.model.pagination.Order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static ru.sbrf.docedit.service.impl.DataSet.*;

/**
 * Created by SBT-Bakhurskiy-IA on 14.02.2017.
 */
@DatabaseSetup("classpath:dataset/ServicesDataSet.xml")
@DatabaseTearDown("classpath:dataset/ServicesDataSet.xml")
public class DocumentServiceImplTest extends AbstractDbTest {
    @Autowired
    private DocumentServiceImpl documentService;

    @Test
    public void create() throws Exception {
        final DocumentMeta meta = documentService.create(1, "document#0");
        assertEquals(documentService.get(0).orElse(null), meta);
    }

    @Test
    public void remove() throws Exception {
        final List<DocumentMeta> newV = new ArrayList<>(ALL_DOCUMENTS);
        newV.remove(ALL_DOCUMENTS.get(0));
        documentService.remove(ALL_DOCUMENTS.get(0).getDocumentId());
        assertEquals(documentService.list(0, Integer.MAX_VALUE, Order.ASC).getItems(), newV);
    }

    @Test(expected = NoSuchEntityException.class)
    public void removeNonExistent() throws Exception {
        documentService.remove(1000000000);
    }

    @Test
    public void updateName() throws Exception {
        final DocumentMeta old = ALL_DOCUMENTS.get(0);
        final DocumentMeta expected = new DocumentMeta(
                old.getDocumentId(), old.getTemplateId(),
                old.getDocumentName() + "_new");
        documentService.update(old.getDocumentId(),
                new DocumentMeta.Update().setDocumentName(expected.getDocumentName()));
        final DocumentMeta saved = documentService.get(old.getDocumentId()).orElse(null);
        assertEquals(expected, saved);
    }

    @Test
    public void updateDocumentTemplate() throws Exception {
        final DocumentMeta old = DOCUMENT_WITH_FIELDS.getDocumentMeta();
        final DocumentMeta expected = new DocumentMeta(old.getDocumentId(),
                TEMPLATE_WITH_NO_FIELDS.getTemplateId(), old.getDocumentName());

        documentService.update(old.getDocumentId(),
                new DocumentMeta.Update().setTemplateId(expected.getTemplateId()));

        final DocumentMeta saved = documentService.get(old.getDocumentId()).orElse(null);

        assertEquals(expected, saved);
        assertEquals(0, documentService.getFull(old.getDocumentId())
                .orElseThrow(AssertionError::new).getFields().size());
    }

    @Test
    public void updateAll() throws Exception {
        final DocumentMeta old = DOCUMENT_WITH_FIELDS.getDocumentMeta();
        final DocumentMeta expected = new DocumentMeta(old.getDocumentId(),
                TEMPLATE_WITH_NO_FIELDS.getTemplateId(),
                old.getDocumentName() + "_new");

        documentService.update(old.getDocumentId(), new DocumentMeta.Update()
                .setTemplateId(expected.getTemplateId())
                .setDocumentName(expected.getDocumentName()));

        final DocumentMeta saved = documentService.get(old.getDocumentId()).orElse(null);

        assertEquals(expected, saved);
        assertEquals(0, documentService.getFull(old.getDocumentId())
                .orElseThrow(AssertionError::new).getFields().size());
    }

    @Test(expected = NoSuchEntityException.class)
    public void updateNonExistent() throws IOException {
        documentService.update(100000, new DocumentMeta.Update()
                .setTemplateId(200)
                .setDocumentName("value"));
    }

    @Test(expected = EmptyUpdate.class)
    public void updateNothing() throws Exception {
        final DocumentMeta expected = ALL_DOCUMENTS.get(0);
        documentService.update(expected.getDocumentId(), new DocumentMeta.Update());
        final DocumentMeta saved = documentService.get(expected.getDocumentId()).orElse(null);
        assertEquals(expected, saved);
    }

    @Test
    public void get() throws Exception {
        assertEquals(documentService.get(ALL_DOCUMENTS.get(0).getDocumentId())
                .orElse(null), ALL_DOCUMENTS.get(0));
    }

    @Test
    public void list() throws Exception {
        final List<DocumentMeta> qr = ALL_DOCUMENTS.stream()
                .sorted(Comparator.comparing(DocumentMeta::getDocumentName))
                .limit(2).collect(Collectors.toList());
        assertEquals(documentService.list(0, 2, Order.ASC).getItems(), qr);
    }

    @Test
    public void getFull() throws Exception {
        final DocumentFull expected = ALL_FULL_DOCUMENTS.get(0);
        assertEquals(expected, documentService.getFull(1).orElse(null));
    }

}