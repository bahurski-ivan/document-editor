package ru.sbrf.docedit.api.dto.field;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sbrf.docedit.model.field.FieldFull;
import ru.sbrf.docedit.model.field.value.FieldType;
import ru.sbrf.docedit.model.field.value.FieldValue;

import javax.validation.constraints.NotNull;

/**
 * Created by SBT-Bakhurskiy-IA on 16.02.2017.
 */
public class DocumentFieldFullDto {
    @NotNull
    @JsonProperty(value = "meta_information", required = true)
    private FieldMetaDto metaDto;

    @JsonProperty(value = "field_value", required = true)
    private FieldValue value;


    public DocumentFieldFullDto(FieldMetaDto metaDto, FieldValue value) {
        this.metaDto = metaDto;
        this.value = value;
    }

    public DocumentFieldFullDto() {
    }


    public static DocumentFieldFullDto toDto(FieldFull fieldFull) {
        final FieldValue value = fieldFull.getValue();
        final FieldType type = fieldFull.getMeta().getType();
        return new DocumentFieldFullDto(FieldMetaDto.toDto(fieldFull.getMeta()), value);
    }


    public FieldMetaDto getMetaDto() {
        return metaDto;
    }

    public void setMetaDto(FieldMetaDto metaDto) {
        this.metaDto = metaDto;
    }

    public FieldValue getValue() {
        return value;
    }

    public void setValue(FieldValue value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "DocumentFieldFullDto{" +
                "metaDto=" + metaDto +
                ", value=" + value +
                '}';
    }
}
