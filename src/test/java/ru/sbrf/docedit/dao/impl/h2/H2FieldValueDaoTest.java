package ru.sbrf.docedit.dao.impl.h2;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sbrf.docedit.AbstractDbTest;
import ru.sbrf.docedit.model.field.Field;
import ru.sbrf.docedit.model.field.value.CheckboxValue;
import ru.sbrf.docedit.model.field.value.TextAreaValue;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by SBT-Bakhurskiy-IA on 14.02.2017.
 */
@DatabaseSetup("classpath:dataset/FieldValueDaoDataSet.xml")
@DatabaseTearDown("classpath:dataset/FieldValueDaoDataSet.xml")
public class H2FieldValueDaoTest extends AbstractDbTest {
    private final static List<Field> FIELDS_FOR_DOCUMENT_1 = new ArrayList<>();

    static {
//        FIELDS_FOR_DOCUMENT_1.add(new Field(0, 0, new InputValue("hello")));
        FIELDS_FOR_DOCUMENT_1.add(new Field(1, 1, new CheckboxValue(true)));
        FIELDS_FOR_DOCUMENT_1.add(new Field(2, 1, new TextAreaValue("hello")));
    }

    @Autowired
    private H2FieldValueDao fieldValueDao;

    @Test
    public void createFieldValue() throws Exception {
        final long fieldId = 3;
        final long documentId = 2;
        final Field field = new Field(fieldId, documentId, new TextAreaValue("..."));
        assertTrue(fieldValueDao.createFieldValue(field));
        assertEquals(field, fieldValueDao.getField(documentId, fieldId).orElse(null));
    }

    @Test
    public void updateFieldValue() throws Exception {
        final long fieldId = 0;
        final long documentId = 0;
        final Field field = new Field(fieldId, documentId, new TextAreaValue("..."));
        assertTrue(fieldValueDao.updateFieldValue(field));
        assertEquals(field, fieldValueDao.getField(documentId, fieldId).orElse(null));
    }

    @Test
    public void removeFieldValue() throws Exception {
        final List<Field> newV = new ArrayList<>(FIELDS_FOR_DOCUMENT_1);
        newV.remove(0);
        assertTrue(fieldValueDao.removeFieldValue(FIELDS_FOR_DOCUMENT_1.get(0)));
        assertEquals(newV, fieldValueDao.listDocumentFields(1));
    }

    @Test
    public void listDocumentFields() throws Exception {
        final long documentId = 1;
        List<Field> saved = fieldValueDao.listDocumentFields(documentId);
        assertEquals(FIELDS_FOR_DOCUMENT_1, saved);
    }

    @Test
    public void getField() throws Exception {
        assertEquals(fieldValueDao.getField(1, 1).orElse(null), FIELDS_FOR_DOCUMENT_1.get(0));
    }

    @Test
    public void testNullValue() throws Exception {
        final Field field = new Field(2, 2, null);
        fieldValueDao.createFieldValue(field);
        assertEquals(field, fieldValueDao.getField(2, 2).orElse(null));
    }
}