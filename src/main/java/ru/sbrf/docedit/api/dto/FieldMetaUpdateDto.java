package ru.sbrf.docedit.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import ru.sbrf.docedit.model.field.value.FieldType;

import javax.validation.constraints.NotNull;

/**
 * Created by SBT-Bakhurskiy-IA on 15.02.2017.
 */
public class FieldMetaUpdateDto {
    @NotNull
    @NotBlank
    @JsonProperty("technical_name")
    private String technicalName;

    @NotNull
    @NotBlank
    @JsonProperty("display_name")
    private String displayName;

    @NotNull
    @JsonProperty("field_ordinal")
    private Integer ordinal;

    @NotNull
    @JsonProperty("field_type")
    private FieldType type;


    public FieldMetaUpdateDto(String technicalName, String displayName, Integer ordinal, FieldType type) {
        this.technicalName = technicalName;
        this.displayName = displayName;
        this.ordinal = ordinal;
        this.type = type;
    }

    public FieldMetaUpdateDto() {
    }


    public String getTechnicalName() {
        return technicalName;
    }

    public void setTechnicalName(String technicalName) {
        this.technicalName = technicalName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "FieldMetaUpdateDto{" +
                "technicalName='" + technicalName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", ordinal=" + ordinal +
                ", type=" + type +
                '}';
    }
}
