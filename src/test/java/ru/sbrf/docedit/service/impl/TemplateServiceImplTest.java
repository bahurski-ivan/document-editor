package ru.sbrf.docedit.service.impl;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sbrf.docedit.AbstractDbTest;
import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.model.template.TemplateFull;
import ru.sbrf.docedit.model.template.TemplateMeta;
import ru.sbrf.docedit.service.TemplateService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static ru.sbrf.docedit.service.impl.DataSet.ALL_FULL_TEMPLATES;
import static ru.sbrf.docedit.service.impl.DataSet.ALL_TEMPLATES;

/**
 * Created by SBT-Bakhurskiy-IA on 14.02.2017.
 */
@DatabaseSetup("classpath:dataset/ServicesDataSet.xml")
@DatabaseTearDown("classpath:dataset/ServicesDataSet.xml")
public class TemplateServiceImplTest extends AbstractDbTest {
    @Autowired
    private TemplateService templateService;

    @Test
    public void create() throws Exception {
        final TemplateMeta expected = new TemplateMeta(0, "template#0");
        final TemplateMeta created = templateService.create("template#0");
        final TemplateMeta saved = templateService.get(0).orElse(null);
        assertEquals(expected, created);
        assertEquals(expected, saved);
    }

    @Test
    public void update() throws Exception {
        final TemplateMeta old = ALL_TEMPLATES.get(0);
        final TemplateMeta expected = new TemplateMeta(
                old.getTemplateId(), old.getTemplateName() + "_newValue");
        templateService.update(old.getTemplateId(),
                new TemplateMeta.Update().setTemplateName(expected.getTemplateName()));
        final TemplateMeta saved = templateService.get(old.getTemplateId()).orElse(null);
        assertEquals(expected, saved);
    }

    // TODO write tests to test that all exceptions thrown as expected

    @Test
    public void remove() throws Exception {
        final TemplateMeta old = ALL_TEMPLATES.get(0);
        final List<TemplateMeta> newV = new ArrayList<>(ALL_TEMPLATES);
        newV.remove(old);
        templateService.remove(old.getTemplateId());
        assertEquals(newV, templateService.list(0, Integer.MAX_VALUE, Order.ASC).getItems());
    }

    @Test
    public void get() throws Exception {
        assertEquals(ALL_TEMPLATES.get(0), templateService.get(ALL_TEMPLATES.get(0).getTemplateId())
                .orElse(null));
    }

    @Test
    public void list() throws Exception {
        assertEquals(ALL_TEMPLATES, templateService
                .list(0, Integer.MAX_VALUE, Order.ASC).getItems());
    }

    @Test
    public void getFull() throws Exception {
        final TemplateFull expected = ALL_FULL_TEMPLATES.get(0);
        final TemplateFull persisted = templateService
                .getFull(expected.getTemplateMeta().getTemplateId()).orElse(null);
        assertEquals(expected, persisted);
    }
}