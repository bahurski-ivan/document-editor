package ru.sbrf.docedit.api.dto.field.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sbrf.docedit.api.dto.value.FieldValueDto;
import ru.sbrf.docedit.model.field.value.FieldType;

import javax.validation.constraints.NotNull;

/**
 * Created by SBT-Bakhurskiy-IA on 16.02.2017.
 */
public class FieldValueUpdateDto {
    @NotNull
    @JsonProperty(value = "value_type", required = true)
    private FieldType type;
    @JsonProperty(value = "value", required = true)
    private FieldValueDto valueDto;


    public FieldValueUpdateDto(FieldType type, FieldValueDto valueDto) {
        this.type = type;
        this.valueDto = valueDto;
    }

    public FieldValueUpdateDto() {
    }


    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public FieldValueDto getValueDto() {
        return valueDto;
    }

    public void setValueDto(FieldValueDto valueDto) {
        this.valueDto = valueDto;
    }


    @Override
    public String toString() {
        return "FieldValueUpdateDto{" +
                "type=" + type +
                ", valueDto=" + valueDto +
                '}';
    }
}
