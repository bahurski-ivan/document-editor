package ru.sbrf.docedit.api.dto.field;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.value.FieldType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by SBT-Bakhurskiy-IA on 15.02.2017.
 */
public class FieldMetaDto {
    @Min(0)
    @JsonProperty("field_id")
    private long fieldId;

    @Min(0)
    @NotNull
    @JsonProperty(value = "template_id", required = true)
    private Long templateId;

    @NotNull
    @NotBlank
    @JsonProperty(value = "technical_name", required = true)
    private String technicalName;

    @NotNull
    @NotBlank()
    @JsonProperty(value = "display_name", required = true)
    private String displayName;

    @NotNull
    @JsonProperty(value = "field_type", required = true)
    private FieldType type;

    @NotNull
    @JsonProperty(value = "field_ordinal", required = true)
    private int ordinal;


    public FieldMetaDto(long fieldId, Long templateId, String technicalName, String displayName, FieldType type, int ordinal) {
        this.fieldId = fieldId;
        this.templateId = templateId;
        this.technicalName = technicalName;
        this.displayName = displayName;
        this.type = type;
        this.ordinal = ordinal;
    }

    public FieldMetaDto() {
    }


    public static FieldMetaDto toDto(FieldMeta fieldMeta) {
        return new FieldMetaDto(fieldMeta.getFieldId(), fieldMeta.getTemplateId(), fieldMeta.getTechnicalName(), fieldMeta.getDisplayName(),
                fieldMeta.getType(), fieldMeta.getOrdinal());
    }

    public static FieldMeta fromDto(FieldMetaDto dto) {
        return new FieldMeta(dto.getFieldId(), dto.getTemplateId(), dto.getTechnicalName(), dto.getDisplayName(), dto.getType(), dto.getOrdinal());
    }


    public long getFieldId() {
        return fieldId;
    }

    public void setFieldId(long fieldId) {
        this.fieldId = fieldId;
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
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

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    @Override
    public String toString() {
        return "FieldMetaDto{" +
                "fieldId=" + fieldId +
                ", templateId=" + templateId +
                ", technicalName='" + technicalName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", type=" + type +
                ", ordinal=" + ordinal +
                '}';
    }
}
