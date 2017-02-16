package ru.sbrf.docedit.api.dto.value;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by SBT-Bakhurskiy-IA on 16.02.2017.
 */
public class StringValueDto implements FieldValueDto {
    @JsonProperty(value = "value", required = true)
    private String value;


    public StringValueDto(String value) {
        this.value = value;
    }

    public StringValueDto() {
    }


    public static StringValueDto of(String v) {
        return new StringValueDto(v);
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "StringValueDto{" +
                "value='" + value + '\'' +
                '}';
    }
}
