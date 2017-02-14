package ru.sbrf.docedit.service.impl;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sbrf.docedit.AbstractDbTest;
import ru.sbrf.docedit.model.document.DocumentFull;
import ru.sbrf.docedit.model.document.DocumentMeta;
import ru.sbrf.docedit.model.field.FieldFull;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.value.CheckboxValue;
import ru.sbrf.docedit.model.field.value.FieldType;
import ru.sbrf.docedit.model.field.value.InputValue;
import ru.sbrf.docedit.model.pagination.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by SBT-Bakhurskiy-IA on 14.02.2017.
 */
@DatabaseSetup("classpath:dataset/ServicesDataSet.xml")
@DatabaseTearDown("classpath:dataset/ServicesDataSet.xml")
public class DocumentServiceImplTest extends AbstractDbTest {

    private final static List<DocumentMeta> ALL_DOCUMENTS = new ArrayList<>();

    static {
        ALL_DOCUMENTS.add(new DocumentMeta(1, 1, "document#1"));
        ALL_DOCUMENTS.add(new DocumentMeta(2, 1, "document#2"));
        ALL_DOCUMENTS.add(new DocumentMeta(3, 2, "document#3"));
    }

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
        newV.remove(1);
        documentService.remove(2);
        assertEquals(documentService.list(0, Integer.MAX_VALUE, Order.ASC).getItems(), newV);
    }

    @Test
    public void update() throws Exception {
        final DocumentMeta meta = new DocumentMeta(1, 1, "document#1_new_version");
        final DocumentMeta updated = documentService.update(1, "document#1_new_version");
        final DocumentMeta saved = documentService.get(1).orElse(null);

        assertEquals(meta, updated);
        assertEquals(saved, meta);
    }

    @Test
    public void get() throws Exception {
        assertEquals(documentService.get(1).orElse(null), ALL_DOCUMENTS.get(0));
    }

    @Test
    public void list() throws Exception {
        final List<DocumentMeta> qr = new ArrayList<>(ALL_DOCUMENTS);
        qr.remove(2);
        assertEquals(documentService.list(0, 2, Order.ASC).getItems(), qr);
    }

    @Test
    public void getFull() throws Exception {
        final DocumentFull expected = new DocumentFull(1, 1,
                "document#1",
                Arrays.asList(
                        new FieldFull(new FieldMeta(2, 1, "field#2", "field#2", FieldType.CHECKBOX),
                                new CheckboxValue(true)),
                        new FieldFull(new FieldMeta(1, 1, "field#1", "field#1", FieldType.INPUT),
                                new InputValue("hello"))
                )
        );
        assertEquals(expected, documentService.getFull(1).orElse(null));
    }

}