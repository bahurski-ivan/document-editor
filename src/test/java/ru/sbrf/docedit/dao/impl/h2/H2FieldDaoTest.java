package ru.sbrf.docedit.dao.impl.h2;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sbrf.docedit.AbstractDbTest;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.value.FieldType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by SBT-Bakhurskiy-IA on 13.02.2017.
 */
@DatabaseSetup("classpath:dataset/FieldMetaDaoDataSet.xml")
@DatabaseTearDown("classpath:dataset/FieldMetaDaoDataSet.xml")
public class H2FieldDaoTest extends AbstractDbTest {
    private static long TEMPLATE_0_ID = 0;
    @Autowired
    private H2FieldDao fieldMetaDao;

    @Test
    public void createFieldMeta() throws Exception {
        final FieldMeta meta = new FieldMeta(0, TEMPLATE_0_ID, "field1", "something", FieldType.INPUT, Integer.MAX_VALUE);
        long id = fieldMetaDao.createFieldMeta(meta);
        final Optional<FieldMeta> saved = fieldMetaDao.getFieldMeta(0);
        assertTrue(saved.isPresent());
        assertEquals(saved.get(), meta);
    }

    @Test
    public void listFields() throws Exception {
        final List<FieldMeta> ll = fieldMetaDao.getTemplateFields(TEMPLATE_0_ID);
        assertEquals(ll.size(), 3);
        assertEquals(Collections.emptyList(), fieldMetaDao.getTemplateFields(1L));
    }

    @Test
    public void removeFieldMeta() throws Exception {
        assertTrue(fieldMetaDao.removeFieldMeta(1));
        final List<FieldMeta> ll = fieldMetaDao.getTemplateFields(TEMPLATE_0_ID);
        assertEquals(ll.size(), 2);
    }

    @Test
    public void updateFieldMeta() throws Exception {
        final FieldMeta expected = new FieldMeta(1, TEMPLATE_0_ID, "field#1", "new value", FieldType.TEXTAREA, 0);
        assertTrue(fieldMetaDao.updateFieldMeta(expected));
        final Optional<FieldMeta> saved = fieldMetaDao.getFieldMeta(1);
        assertTrue(saved.isPresent());
        assertEquals(expected, saved.get());
    }

    @Test
    public void get() throws Exception {
        final Optional<FieldMeta> ff = fieldMetaDao.getFieldMeta(1);
        final FieldMeta expected = new FieldMeta(1, TEMPLATE_0_ID, "field#1", "some field", FieldType.INPUT, 0);
        assertTrue(ff.isPresent());
        assertEquals(ff.get(), expected);
    }
}