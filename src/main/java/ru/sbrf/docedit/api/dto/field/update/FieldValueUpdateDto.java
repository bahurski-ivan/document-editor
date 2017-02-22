package ru.sbrf.docedit.api.dto.field.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sbrf.docedit.api.dto.value.FieldValueDto;
import ru.sbrf.docedit.model.field.value.FieldType;

import javax.validation.constraints.NotNull;

/**
 * DTO for field value update request.
 */
public class FieldValueUpdateDto {
    @NotNull
    @JsonProperty(value = "type", required = true)
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
