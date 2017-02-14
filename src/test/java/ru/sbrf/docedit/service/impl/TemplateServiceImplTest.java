package ru.sbrf.docedit.service.impl;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sbrf.docedit.AbstractDbTest;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.value.FieldType;
import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.model.template.TemplateFull;
import ru.sbrf.docedit.model.template.TemplateMeta;
import ru.sbrf.docedit.service.TemplateService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by SBT-Bakhurskiy-IA on 14.02.2017.
 */
@DatabaseSetup("classpath:dataset/ServicesDataSet.xml")
@DatabaseTearDown("classpath:dataset/ServicesDataSet.xml")
public class TemplateServiceImplTest extends AbstractDbTest {
    private final static List<TemplateMeta> ALL_TEMPLATES;

    static {
        ALL_TEMPLATES = new ArrayList<>();

        ALL_TEMPLATES.add(new TemplateMeta(1, "template#1"));
        ALL_TEMPLATES.add(new TemplateMeta(2, "template#2"));
        ALL_TEMPLATES.add(new TemplateMeta(3, "template#3"));
    }

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
        final TemplateMeta expected = new TemplateMeta(1, "field#1_newValue");
        final TemplateMeta updated = templateService.update(1, "field#1_newValue");
        final TemplateMeta saved = templateService.get(1).orElse(null);

        assertEquals(expected, updated);
        assertEquals(expected, saved);
    }

    @Test
    public void remove() throws Exception {
        final List<TemplateMeta> newV = new ArrayList<>(ALL_TEMPLATES);
        newV.remove(0);

        templateService.remove(1);
        assertEquals(newV, templateService.list(0, Integer.MAX_VALUE, Order.ASC).getItems());
    }

    @Test
    public void get() throws Exception {
        assertEquals(ALL_TEMPLATES.get(0), templateService.get(1).orElse(null));
    }

    @Test
    public void list() throws Exception {
        assertEquals(ALL_TEMPLATES, templateService.list(0, Integer.MAX_VALUE, Order.ASC).getItems());
    }

    @Test
    public void getFull() throws Exception {
        final TemplateFull templateFull = templateService.getFull(1).orElse(null);
        final List<FieldMeta> fields = new ArrayList<>();
        fields.add(new FieldMeta(2, 1, "field#2", "field#2", FieldType.CHECKBOX));
        fields.add(new FieldMeta(1, 1, "field#1", "field#1", FieldType.INPUT));
        final TemplateFull expected = new TemplateFull(1, "template#1", fields);

        assertEquals(expected, templateFull);
    }

}