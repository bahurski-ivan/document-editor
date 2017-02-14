package ru.sbrf.docedit.dao.impl.h2;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sbrf.docedit.AbstractDbTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by SBT-Bakhurskiy-IA on 13.02.2017.
 */
@DatabaseSetup("classpath:dataset/FieldsOrdinalsDataSet.xml")
@DatabaseTearDown("classpath:dataset/FieldsOrdinalsDataSet.xml")
public class H2FieldOrdinalsDaoTest extends AbstractDbTest {
    private final static List<Long> ORDINALS_TEMPLATE_ID_0 = new ArrayList<>();

    static {
        ORDINALS_TEMPLATE_ID_0.add(0L);
        ORDINALS_TEMPLATE_ID_0.add(2L);
        ORDINALS_TEMPLATE_ID_0.add(1L);
    }

    @Autowired
    private H2FieldOrdinalsDao ordinalsDao;

    @Test
    public void testGet() throws Exception {
        final List<Long> ordinals = ordinalsDao.getOrderedFields(0);
        assertEquals(ordinals, ORDINALS_TEMPLATE_ID_0);
    }

    @Test
    public void testBatchUpdate() throws Exception {
        final List<Long> newV = new ArrayList<>(ORDINALS_TEMPLATE_ID_0);
        newV.remove(2L);
        ordinalsDao.updateBatch(0, newV);
        assertEquals(ordinalsDao.getOrderedFields(0), newV);
    }

    /**
     * Test creation mod of create method.
     */
    @Test
    public void testCreateOutRange() throws Exception {
        final List<Long> newV = new ArrayList<>(ORDINALS_TEMPLATE_ID_0);
        newV.add(5L);
        ordinalsDao.create(0L, 5L, 700);
        assertEquals(ordinalsDao.getOrderedFields(0L), newV);
    }

    /**
     * Test create method in creation mode.
     */
    @Test
    public void testCreateNonExistent() throws Exception {
        ordinalsDao.create(2L, 5L, -1);
        assertEquals(ordinalsDao.getOrdinal(2L, 5L), 0);
    }

    /**
     * Test update.
     */
    @Test
    public void testUpdate() throws Exception {
        final List<Long> newV = new ArrayList<>();
        newV.add(2L);
        newV.add(0L);
        newV.add(1L);
        ordinalsDao.update(0L, 2L, 0);
        assertEquals(ordinalsDao.getOrderedFields(0L), newV);
    }

    /**
     * Test updating non-existent id.
     * <p>
     * Should be equal to no-op.
     */
    @Test
    public void testUpdateNoSuchId() throws Exception {
        ordinalsDao.update(0L, 5L, 0);
        assertEquals(ordinalsDao.getOrderedFields(0L), ORDINALS_TEMPLATE_ID_0);
    }
}