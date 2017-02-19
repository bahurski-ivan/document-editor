package ru.sbrf.docedit.api.dto.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sbrf.docedit.api.dto.field.FieldMetaDto;
import ru.sbrf.docedit.model.template.TemplateFull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by SBT-Bakhurskiy-IA on 15.02.2017.
 */
public class TemplateFullDto {
    @JsonProperty("meta_information")
    private TemplateMetaDto meta;

    @JsonProperty("fields")
    private List<FieldMetaDto> fieldList;

    public TemplateFullDto(TemplateMetaDto meta, List<FieldMetaDto> fieldList) {
        this.meta = meta;
        this.fieldList = fieldList;
    }

    public TemplateFullDto() {
    }


    public static TemplateFull fromDto(TemplateFullDto dto) {
        return new TemplateFull(
                TemplateMetaDto.fromDto(dto.getMeta()),
                dto.getFieldList().stream().map(FieldMetaDto::fromDto).collect(Collectors.toList())
        );
    }

    public static TemplateFullDto toDto(TemplateFull templateFull) {
        return new TemplateFullDto(
                TemplateMetaDto.toDto(templateFull.getTemplateMeta()),
                templateFull.getFields().stream()
                        .map(FieldMetaDto::toDto)
                        .collect(Collectors.toList())
        );
    }


    public TemplateMetaDto getMeta() {
        return meta;
    }

    public void setMeta(TemplateMetaDto meta) {
        this.meta = meta;
    }

    public List<FieldMetaDto> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<FieldMetaDto> fieldList) {
        this.fieldList = fieldList;
    }

    @Override
    public String toString() {
        return "TemplateFullDto{" +
                "meta=" + meta +
                ", fieldList=" + fieldList +
                '}';
    }
}
