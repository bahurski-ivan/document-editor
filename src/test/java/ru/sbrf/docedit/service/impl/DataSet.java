package ru.sbrf.docedit.service.impl;

import ru.sbrf.docedit.model.document.DocumentFull;
import ru.sbrf.docedit.model.document.DocumentMeta;
import ru.sbrf.docedit.model.field.FieldFull;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.FieldValueHolder;
import ru.sbrf.docedit.model.field.value.*;
import ru.sbrf.docedit.model.template.TemplateFull;
import ru.sbrf.docedit.model.template.TemplateMeta;
import ru.sbrf.docedit.model.user.Role;
import ru.sbrf.docedit.model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by SBT-Bakhurskiy-IA on 20.02.2017.
 */
class DataSet {
    final static List<DocumentMeta> ALL_DOCUMENTS;
    final static List<TemplateMeta> ALL_TEMPLATES;
    final static List<FieldMeta> ALL_FIELDS_META;
    final static List<FieldValue> ALL_FIELD_VALUES;
    final static List<FieldValueHolder> ALL_HOLDERS;
    final static List<FieldFull> ALL_FULL_FIELDS;
    final static List<TemplateFull> ALL_FULL_TEMPLATES;
    final static List<DocumentFull> ALL_FULL_DOCUMENTS;
    final static List<User> ALL_USERS;

    final static User ADMIN_USER;
    final static User REGULAR_USER;

    final static DocumentFull DOCUMENT_WITH_FIELDS;
    final static TemplateMeta TEMPLATE_WITH_NO_FIELDS, TEMPLATE_WITH_FIELDS;
    final static FieldFull FIELD_WITH_ONE_DOCUMENT_VALUE;

    static {
        ALL_TEMPLATES = new ArrayList<>();
        ALL_TEMPLATES.add(new TemplateMeta(1, "template#1"));
        ALL_TEMPLATES.add(new TemplateMeta(2, "template#2"));
        ALL_TEMPLATES.add(new TemplateMeta(3, "template#3"));

        ALL_DOCUMENTS = new ArrayList<>();
        ALL_DOCUMENTS.add(new DocumentMeta(1, 1, "document#1"));
        ALL_DOCUMENTS.add(new DocumentMeta(2, 1, "document#2"));
        ALL_DOCUMENTS.add(new DocumentMeta(3, 2, "document#3"));

        ALL_FIELDS_META = new ArrayList<>();
        ALL_FIELDS_META.add(new FieldMeta(1, 1, "field#1", "field#1", FieldType.INPUT, 0));
        ALL_FIELDS_META.add(new FieldMeta(2, 1, "field#2", "field#2", FieldType.CHECKBOX, 1));
        ALL_FIELDS_META.add(new FieldMeta(3, 2, "field#3", "field#3", FieldType.TEXTAREA, 0));

        ALL_FIELD_VALUES = new ArrayList<>();
        ALL_FIELD_VALUES.add(new InputValue("hello"));
        ALL_FIELD_VALUES.add(new CheckboxValue(true));
        ALL_FIELD_VALUES.add(new TextAreaValue("hello"));

        ALL_HOLDERS = new ArrayList<>();
        ALL_HOLDERS.add(new FieldValueHolder(1, 1, ALL_FIELD_VALUES.get(0)));
        ALL_HOLDERS.add(new FieldValueHolder(2, 1, ALL_FIELD_VALUES.get(1)));
        ALL_HOLDERS.add(new FieldValueHolder(3, 2, ALL_FIELD_VALUES.get(2)));

        ALL_FULL_FIELDS = new ArrayList<>();
        ALL_FULL_FIELDS.add(new FieldFull(ALL_FIELDS_META.get(0), ALL_FIELD_VALUES.get(0)));
        ALL_FULL_FIELDS.add(new FieldFull(ALL_FIELDS_META.get(1), ALL_FIELD_VALUES.get(1)));
        ALL_FULL_FIELDS.add(new FieldFull(ALL_FIELDS_META.get(2), ALL_FIELD_VALUES.get(2)));

        ALL_FULL_DOCUMENTS = new ArrayList<>();
        ALL_FULL_DOCUMENTS.add(createDocumentFull(0));
        ALL_FULL_DOCUMENTS.add(createDocumentFull(1));
        ALL_FULL_DOCUMENTS.add(createDocumentFull(2));

        ALL_FULL_TEMPLATES = new ArrayList<>();
        ALL_FULL_TEMPLATES.add(createTemplateFull(0));
        ALL_FULL_TEMPLATES.add(createTemplateFull(1));
        ALL_FULL_TEMPLATES.add(createTemplateFull(2));

        ALL_USERS = new ArrayList<>();
        ALL_USERS.add(new User(1, "user#1", "user#1", Role.ADMINISTRATOR));
        ALL_USERS.add(new User(2, "user#2", "user#2", Role.REGULAR));

        ADMIN_USER = ALL_USERS.get(0);
        REGULAR_USER = ALL_USERS.get(1);

        DOCUMENT_WITH_FIELDS = ALL_FULL_DOCUMENTS.get(0);

        TEMPLATE_WITH_FIELDS = ALL_TEMPLATES.get(0);
        TEMPLATE_WITH_NO_FIELDS = ALL_TEMPLATES.get(2);

        FIELD_WITH_ONE_DOCUMENT_VALUE = ALL_FULL_FIELDS.get(0);
    }

    private static DocumentFull createDocumentFull(int index) {
        final DocumentMeta meta = ALL_DOCUMENTS.get(index);
        final Set<Long> fieldIds = ALL_HOLDERS.stream()
                .filter(h -> h.getDocumentId() == meta.getDocumentId())
                .map(FieldValueHolder::getFieldId)
                .collect(Collectors.toSet());
        return new DocumentFull(meta, ALL_FULL_FIELDS.stream()
                .filter(f -> fieldIds.contains(f.getMeta().getFieldId()))
                .collect(Collectors.toList()));
    }

    private static TemplateFull createTemplateFull(int index) {
        final TemplateMeta meta = ALL_TEMPLATES.get(index);
        return new TemplateFull(meta, ALL_FIELDS_META.stream()
                .filter(f -> meta.getTemplateId() == f.getTemplateId())
                .collect(Collectors.toList()));
    }
}
