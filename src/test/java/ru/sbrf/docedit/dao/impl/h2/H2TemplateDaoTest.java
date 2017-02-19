package ru.sbrf.docedit.dao.impl.h2;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sbrf.docedit.AbstractDbTest;
import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.model.pagination.Page;
import ru.sbrf.docedit.model.template.TemplateMeta;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by SBT-Bakhurskiy-IA on 14.02.2017.
 */
@DatabaseSetup("classpath:dataset/TemplateMetaDaoDataSet.xml")
@DatabaseTearDown("classpath:dataset/TemplateMetaDaoDataSet.xml")
public class H2TemplateDaoTest extends AbstractDbTest {
    private static final List<TemplateMeta> ALL_TEMPLATES = new ArrayList<>();

    static {
        ALL_TEMPLATES.add(new TemplateMeta(1, "template#1"));
        ALL_TEMPLATES.add(new TemplateMeta(2, "template#2"));
        ALL_TEMPLATES.add(new TemplateMeta(3, "template#3"));
    }

    @Autowired
    private H2TemplateDao templateMetaDao;

    @Test
    public void createTemplate() throws Exception {
        final TemplateMeta meta = new TemplateMeta(0, "template#0");
        long id = templateMetaDao.createTemplate(meta);
        assertEquals(id, 0);
        assertEquals(meta, templateMetaDao.getTemplate(0).orElse(null));
    }

    @Test
    public void updateTemplateName() throws Exception {
        final TemplateMeta meta = new TemplateMeta(1, "template#1-0000");
        assertTrue(templateMetaDao.updateTemplate(meta));
        assertEquals(meta, templateMetaDao.getTemplate(1).orElse(null));
    }

    @Test
    public void listAll() throws Exception {
        assertEquals(templateMetaDao.listAll(), ALL_TEMPLATES);
    }

    @Test
    public void removeTemplateMeta() throws Exception {
        final List<TemplateMeta> newV = new ArrayList<>(ALL_TEMPLATES);
        newV.remove(0);
        assertTrue(templateMetaDao.removeTemplateMeta(ALL_TEMPLATES.get(0).getTemplateId()));
        assertEquals(newV, templateMetaDao.listAll());
    }

    @Test
    public void getTemplate() throws Exception {
        assertEquals(templateMetaDao.getTemplate(ALL_TEMPLATES.get(0).getTemplateId()).orElse(null), ALL_TEMPLATES.get(0));
    }

    @Test
    public void listPaged() throws Exception {
        Page<TemplateMeta> result = templateMetaDao.listPaged(0, 2, Order.ASC);
        final List<TemplateMeta> newV = new ArrayList<>(ALL_TEMPLATES);
        newV.remove(2);
        assertEquals(newV, result.getItems());
    }
}