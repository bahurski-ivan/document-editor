package ru.sbrf.docedit.api.dto.value;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by SBT-Bakhurskiy-IA on 16.02.2017.
 */
public class BooleanValueDto implements FieldValueDto {
    @NotNull
    @JsonProperty(value = "value", required = true)
    private Boolean value;

    public BooleanValueDto(Boolean value) {
        this.value = value;
    }

    public BooleanValueDto() {
    }

    public static FieldValueDto of(boolean checked) {
        return new BooleanValueDto(checked);
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "BooleanValueDto{" +
                "value=" + value +
                '}';
    }
}
