package ru.sbrf.docedit.service.impl;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sbrf.docedit.AbstractDbTest;
import ru.sbrf.docedit.model.field.FieldFull;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.value.*;
import ru.sbrf.docedit.model.template.TemplateFull;
import ru.sbrf.docedit.service.TemplateService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by SBT-Bakhurskiy-IA on 14.02.2017.
 */
@DatabaseSetup("classpath:dataset/ServicesDataSet.xml")
@DatabaseTearDown("classpath:dataset/ServicesDataSet.xml")
public class FieldServiceImplTest extends AbstractDbTest {
    @Autowired
    private FieldServiceImpl fieldService;

    @Autowired
    private TemplateService templateService;

    @Test
    public void updateFieldValue() throws Exception {
        final FieldValue value = new InputValue("...");
        fieldService.updateFieldValue(1, 1, value);
        final FieldFull field = fieldService.getDocumentField(1, 1).orElse(null);
        assertNotNull(field);
        assertEquals(value, field.getValue());
    }

    @Test
    public void getDocumentField() throws Exception {
        final FieldValue value = new InputValue("hello");
        final FieldFull field = fieldService.getDocumentField(1, 1).orElse(null);
        assertEquals(value, field.getValue());
    }

    @Test
    public void getAllDocumentFields() throws Exception {
        final List<FieldValue> expected = new ArrayList<>();

        expected.add(new InputValue("hello"));
        expected.add(new CheckboxValue(true));

        assertEquals(expected, fieldService.getAllDocumentFields(1)
                .stream()
                .map(FieldFull::getValue)
                .collect(Collectors.toList()));
    }

    @Test
    public void create() throws Exception {
        final FieldMeta expected = new FieldMeta(0, 2, "field#0", "field#0", FieldType.INPUT);
        final FieldMeta created = fieldService.create(2, "field#0", "field#0", FieldType.INPUT);
        final FieldMeta saved = fieldService.getOne(0).orElse(null);
        assertEquals(expected, created);
        assertEquals(saved, expected);
    }

    @Test
    public void update() throws Exception {
        final FieldMeta expected = new FieldMeta(1, 1, "field#1_NV", "field#1_NV", FieldType.TEXTAREA);
        final FieldMeta updated = fieldService.update(1, "field#1_NV", "field#1_NV", FieldType.TEXTAREA, -1000);
        final FieldMeta saved = fieldService.getOne(1).orElse(null);
        final FieldFull documentField = fieldService.getDocumentField(1, 1).orElse(null);

        assertEquals(expected, saved);
        assertEquals(expected, updated);
        assertEquals(documentField.getValue(), new TextAreaValue("hello"));
        assertEquals(documentField.getMeta(), expected);

        final TemplateFull full = templateService.getFull(1).orElse(null);

        assertNotNull(full);
        assertThat(full.getFields().size(), org.hamcrest.CoreMatchers.not(CoreMatchers.equalTo(0)));
        assertEquals(full.getFields().get(0), expected);
    }

    @Test
    public void remove() throws Exception {
        fieldService.remove(1);

        final List<FieldMeta> fields = new ArrayList<>();
        fields.add(new FieldMeta(2, 1, "field#2", "field#2", FieldType.CHECKBOX));
        assertEquals(fieldService.getAll(1), fields);
    }

    @Test
    public void getOne() throws Exception {
        assertEquals(fieldService.getOne(1).orElse(null), new FieldMeta(1, 1, "field#1", "field#1", FieldType.INPUT));
    }

    @Test
    public void getAll() throws Exception {
        final List<FieldMeta> fields = new ArrayList<>();
        fields.add(new FieldMeta(1, 1, "field#1", "field#1", FieldType.INPUT));
        fields.add(new FieldMeta(2, 1, "field#2", "field#2", FieldType.CHECKBOX));
        assertEquals(fieldService.getAll(1), fields);
    }

}