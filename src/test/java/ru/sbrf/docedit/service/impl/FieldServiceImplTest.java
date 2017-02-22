package ru.sbrf.docedit.service.impl;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sbrf.docedit.AbstractDbTest;
import ru.sbrf.docedit.exception.EmptyUpdate;
import ru.sbrf.docedit.exception.NoSuchEntityException;
import ru.sbrf.docedit.model.document.DocumentFull;
import ru.sbrf.docedit.model.field.FieldFull;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.FieldValueHolder;
import ru.sbrf.docedit.model.field.value.FieldType;
import ru.sbrf.docedit.model.field.value.InputValue;
import ru.sbrf.docedit.model.field.value.TextAreaValue;
import ru.sbrf.docedit.model.template.TemplateFull;
import ru.sbrf.docedit.model.template.TemplateMeta;
import ru.sbrf.docedit.service.TemplateService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static ru.sbrf.docedit.service.impl.DataSet.*;

/**
 * Created by SBT-Bakhurskiy-IA on 14.02.2017.
 */
@DatabaseSetup("classpath:dataset/ServicesDataSet.xml")
@DatabaseTearDown(value = "classpath:dataset/ServicesDataSet.xml", type = DatabaseOperation.DELETE_ALL)
public class FieldServiceImplTest extends AbstractDbTest {
    @Autowired
    private FieldServiceImpl fieldService;

    @Autowired
    private TemplateService templateService;

    @Test
    public void updateFieldValue() throws Exception {
        final DocumentFull documentFull = ALL_FULL_DOCUMENTS.get(0);
        final FieldFull targetField = documentFull.getFields().get(0);
        final FieldFull expected = new FieldFull(
                targetField.getMeta(),
                new TextAreaValue("...")
        );
        fieldService.updateFieldValue(documentFull.getDocumentMeta().getDocumentId(),
                targetField.getMeta().getFieldId(), expected.getValue());
        final FieldFull persisted = fieldService
                .getDocumentField(documentFull.getDocumentMeta().getDocumentId(),
                        targetField.getMeta().getFieldId()).orElse(null);
        assertEquals(expected, persisted);
    }

    @Test(expected = NoSuchEntityException.class)
    public void updateNonExistentFieldValueDoc() throws Exception {
        fieldService.updateFieldValue(10000000, ALL_FIELDS_META.get(0).getFieldId(), new InputValue("test"));
    }

    @Test(expected = NoSuchEntityException.class)
    public void updateNonExistentValueField() throws Exception {
        fieldService.updateFieldValue(ALL_DOCUMENTS.get(0).getDocumentId(), 100000000, new InputValue("test"));
    }

    @Test(expected = NoSuchEntityException.class)
    public void updateNonExistentValueFieldAndDoc() throws Exception {
        fieldService.updateFieldValue(1000000, 100000000, new InputValue("test"));
    }

    @Test
    public void updateFieldValueNull() throws Exception {
        final FieldValueHolder holder = ALL_HOLDERS.get(0);
        fieldService.updateFieldValue(holder.getDocumentId(), holder.getFieldId(), null);
        assertNull(fieldService.getDocumentField(holder.getDocumentId(), holder.getFieldId()).orElseThrow(AssertionError::new).getValue());
    }

    @Test
    public void getDocumentField() throws Exception {
        final DocumentFull documentFull = ALL_FULL_DOCUMENTS.get(0);
        final FieldFull expected = documentFull.getFields().get(0);
        final FieldFull persisted = fieldService.getDocumentField(
                documentFull.getDocumentMeta().getDocumentId(),
                expected.getMeta().getFieldId()).orElse(null);
        assertEquals(expected, persisted);
    }

    @Test
    public void getAllDocumentFields() throws Exception {
        final DocumentFull documentFull = ALL_FULL_DOCUMENTS.get(0);
        final List<FieldFull> expected = documentFull.getFields().stream()
                .sorted((f1, f2) -> Long.compare(f1.getMeta().getFieldId(), f2.getMeta().getFieldId()))
                .collect(Collectors.toList());
        assertEquals(expected, fieldService.getAllDocumentFields(documentFull.getDocumentMeta()
                .getDocumentId()).stream()
                .sorted((f1, f2) -> Long.compare(f1.getMeta().getFieldId(), f2.getMeta().getFieldId()))
                .collect(Collectors.toList()));
    }

    @Test
    public void create() throws Exception {
        final TemplateMeta meta = ALL_TEMPLATES.get(0);

        final FieldMeta expected = new FieldMeta(0, meta.getTemplateId(), "field#0",
                "field#0", FieldType.INPUT, 0);
        final FieldMeta created = fieldService.create(expected);

        final FieldMeta persisted = fieldService.getOne(expected.getFieldId()).orElse(null);

        assertEquals(expected, created);
        assertEquals(persisted, expected);
    }

    @Test
    public void updateAll() throws Exception {
        final long templateId = 2;
        final FieldMeta old = ALL_FIELDS_META.get(0);

        final FieldMeta expected = new FieldMeta(
                old.getFieldId(),
                templateId,
                old.getTechnicalName() + "_new",
                old.getDisplayName() + "_new",
                FieldType.TEXTAREA,
                old.getOrdinal() + 1
        );

        fieldService.update(old.getFieldId(), new FieldMeta.Update()
                .setTemplateId(expected.getTemplateId())
                .setTechnicalName(expected.getTechnicalName())
                .setDisplayName(expected.getDisplayName())
                .setType(expected.getType())
                .setOrdinal(expected.getOrdinal()));

        final FieldMeta persisted = fieldService.getOne(old.getFieldId()).orElse(null);

        assertEquals(expected, persisted);

        final TemplateFull full = templateService.getFull(templateId).orElse(null);

        assertNotNull(full);
        assertTrue(full.getFields().contains(expected));
    }

    @Test
    public void updateTemplateId() throws Exception {
        final FieldMeta original = ALL_FIELDS_META.get(0);
        final long templateId = TEMPLATE_WITH_NO_FIELDS.getTemplateId();
        final FieldMeta expected = new FieldMeta(
                original.getFieldId(),
                templateId,
                original.getTechnicalName(),
                original.getDisplayName(),
                original.getType(),
                original.getOrdinal()
        );

        genericUpdateTest(expected, original);

        final List<FieldMeta> fields = fieldService.getAll(templateId);

        assertEquals(fields.size(), 1);
        assertEquals(fields.get(0), expected);
    }

    @Test
    public void updateTechName() throws Exception {
        final FieldMeta old = ALL_FIELDS_META.get(0);

        final FieldMeta expected = new FieldMeta(
                old.getFieldId(),
                old.getTemplateId(),
                old.getTechnicalName() + "_new",
                old.getDisplayName(),
                old.getType(),
                old.getOrdinal()
        );

        fieldService.update(old.getFieldId(), new FieldMeta.Update()
                .setTechnicalName(expected.getTechnicalName()));

        final FieldMeta persisted = fieldService.getOne(old.getFieldId()).orElse(null);

        assertEquals(expected, persisted);
    }

    @Test
    public void updateDisplayName() throws Exception {
        final FieldMeta original = ALL_FIELDS_META.get(0);

        final FieldMeta expected = new FieldMeta(
                original.getFieldId(),
                original.getTemplateId(),
                original.getTechnicalName(),
                original.getDisplayName() + "_new",
                original.getType(),
                original.getOrdinal()
        );

        genericUpdateTest(expected, original);
    }

    @Test
    public void updateTechnicalName() throws Exception {
        final FieldMeta original = ALL_FIELDS_META.get(0);

        final FieldMeta expected = new FieldMeta(
                original.getFieldId(),
                original.getTemplateId(),
                original.getTechnicalName() + "_new",
                original.getDisplayName(),
                original.getType(),
                original.getOrdinal()
        );

        genericUpdateTest(expected, original);
    }

    @Test
    public void updateType() throws Exception {
        final FieldMeta original = FIELD_WITH_ONE_DOCUMENT_VALUE.getMeta();

        final FieldMeta expected = new FieldMeta(
                original.getFieldId(),
                original.getTemplateId(),
                original.getTechnicalName(),
                original.getDisplayName(),
                FieldType.TEXTAREA,
                original.getOrdinal()
        );

        genericUpdateTest(expected, original);

        final List<FieldFull> fields = fieldService.getAllDocumentFields(1);
        assertEquals(new FieldFull(expected, FIELD_WITH_ONE_DOCUMENT_VALUE.getValue()
                        .convertTo(FieldType.TEXTAREA)),
                fields.get(0)
        );
    }

    @Test
    public void updateOrdinal() throws Exception {
        final FieldMeta original = ALL_FIELDS_META.get(0);
        final TemplateFull template = ALL_FULL_TEMPLATES.stream()
                .filter(t -> t.getTemplateMeta().getTemplateId() == original.getTemplateId())
                .findFirst().orElseThrow(AssertionError::new);

        final FieldMeta expected = new FieldMeta(
                original.getFieldId(),
                original.getTemplateId(),
                original.getTechnicalName(),
                original.getDisplayName(),
                original.getType(),
                template.getFields().size() - 1
        );

        genericUpdateTest(expected, original);

        final List<FieldMeta> fields = templateService.getFull(template.getTemplateMeta()
                .getTemplateId())
                .orElseThrow(AssertionError::new)
                .getFields();

        assertEquals(fields.size() - 1, fields.indexOf(expected));
    }

    @Test
    public void updateOrdinalOutOfRangeHigh() throws Exception {
        final FieldMeta original = ALL_FIELDS_META.get(0);
        final TemplateFull template = ALL_FULL_TEMPLATES.stream()
                .filter(t -> t.getTemplateMeta().getTemplateId() == original.getTemplateId())
                .findFirst().orElseThrow(AssertionError::new);

        final FieldMeta expected = new FieldMeta(
                original.getFieldId(),
                original.getTemplateId(),
                original.getTechnicalName(),
                original.getDisplayName(),
                original.getType(),
                template.getFields().size() - 1
        );

        fieldService.update(original.getFieldId(), new FieldMeta.Update().setOrdinal(template.getFields().size() + 100));

        assertEquals(expected, fieldService.getOne(original.getFieldId()).orElse(null));

        final List<FieldMeta> fields = templateService.getFull(template.getTemplateMeta()
                .getTemplateId())
                .orElseThrow(AssertionError::new)
                .getFields();

        assertEquals(fields.size() - 1, fields.indexOf(expected));
    }

    @Test(expected = EmptyUpdate.class)
    public void updateNothing() throws Exception {
        genericUpdateTest(ALL_FIELDS_META.get(0), ALL_FIELDS_META.get(0));
    }

    private void genericUpdateTest(FieldMeta expected, FieldMeta original) {
        final FieldMeta.Update update = new FieldMeta.Update();

        if (original.getTemplateId() != expected.getTemplateId())
            update.setTemplateId(expected.getTemplateId());

        if (!original.getTechnicalName().equals(expected.getTechnicalName()))
            update.setTechnicalName(expected.getTechnicalName());

        if (!original.getDisplayName().equals(expected.getDisplayName()))
            update.setDisplayName(expected.getDisplayName());

        if (original.getOrdinal() != expected.getOrdinal())
            update.setOrdinal(expected.getOrdinal());

        if (original.getType() != expected.getType())
            update.setType(expected.getType());

        fieldService.update(original.getFieldId(), update);
        final FieldMeta persisted = fieldService.getOne(original.getFieldId()).orElse(null);

        assertEquals(expected, persisted);
    }

    @Test
    public void remove() throws Exception {
        final long templateId = 1;
        final List<FieldMeta> expected = new ArrayList<>(ALL_FIELDS_META).stream()
                .filter(m -> m.getTemplateId() == templateId)
                .sorted(Comparator.comparing(FieldMeta::getFieldId))
                .collect(Collectors.toList());

        final FieldMeta toRemove = ALL_FIELDS_META.get(0);
        expected.remove(toRemove);

        fieldService.remove(ALL_FIELDS_META.get(0).getFieldId());
        assertEquals(fieldService.getAll(templateId).stream()
                .sorted(Comparator.comparing(FieldMeta::getFieldId))
                .collect(Collectors.toList()), expected);
    }

    @Test(expected = NoSuchEntityException.class)
    public void removeNonExistent() throws Exception {
        fieldService.remove(100000000);
    }

    @Test
    public void getOne() throws Exception {
        final FieldMeta expected = ALL_FIELDS_META.get(0);
        assertEquals(fieldService.getOne(expected.getFieldId()).orElse(null), expected);
    }

    @Test
    public void getAll() throws Exception {
        final long templateId = 1;

        final List<FieldMeta> expected = new ArrayList<>(ALL_FIELDS_META).stream()
                .filter(m -> m.getTemplateId() == templateId)
                .sorted(Comparator.comparing(FieldMeta::getFieldId))
                .collect(Collectors.toList());

        assertEquals(expected, fieldService.getAll(1).stream()
                .sorted(Comparator.comparing(FieldMeta::getFieldId))
                .collect(Collectors.toList()));
    }

    @Test(expected = NoSuchEntityException.class)
    public void updateNonExistent() throws Exception {
        fieldService.update(10000, new FieldMeta.Update().setTechnicalName("------------"));
    }

    @Test
    public void getOrdinals() throws Exception {
        final TemplateFull templateFull = ALL_FULL_TEMPLATES.get(0);
        final List<Long> listOfFieldIds = templateFull.getFields().stream()
                .map(FieldMeta::getFieldId)
                .collect(Collectors.toList());

        final Map<Long, Integer> expected = IntStream.range(0, listOfFieldIds.size())
                .mapToObj(i -> i).collect(Collectors.toMap(listOfFieldIds::get, i -> i));

        assertEquals(expected, fieldService.getOrdinalMap(templateFull.getTemplateMeta().getTemplateId()));
    }

    @Test
    public void getOrdinalsAfterChange() throws Exception {
        final TemplateFull templateFull = ALL_FULL_TEMPLATES.get(0);
        final List<Long> listOfFieldIds = templateFull.getFields().stream()
                .map(FieldMeta::getFieldId)
                .collect(Collectors.toList());

        final long swapId = listOfFieldIds.get(1);
        listOfFieldIds.remove(swapId);
        listOfFieldIds.add(0, swapId);
        fieldService.update(swapId, new FieldMeta.Update().setOrdinal(0));

        final Map<Long, Integer> expected = IntStream.range(0, listOfFieldIds.size())
                .mapToObj(i -> i).collect(Collectors.toMap(listOfFieldIds::get, i -> i));

        assertEquals(expected, fieldService.getOrdinalMap(templateFull.getTemplateMeta().getTemplateId()));
    }
}