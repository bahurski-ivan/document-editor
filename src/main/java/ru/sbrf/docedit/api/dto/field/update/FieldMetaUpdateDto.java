package ru.sbrf.docedit.api.dto.field.update;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.value.FieldType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.EnumSet;
import java.util.Set;

/**
 * DTO for {@code FieldMeta.Update} class.
 */
public class FieldMetaUpdateDto {
    /**
     * Values state.
     */
    @JsonIgnore
    private final transient Set<Fields> valueState = EnumSet.noneOf(Fields.class);

    @Min(0)
    @JsonProperty("template_id")
    private long templateId;
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


    public FieldMetaUpdateDto(long templateId, String technicalName, String displayName, Integer ordinal, FieldType type) {
        this.templateId = templateId;
        this.technicalName = technicalName;
        this.displayName = displayName;
        this.ordinal = ordinal;
        this.type = type;
    }

    public FieldMetaUpdateDto() {
    }


    public FieldMeta.Update toEntity() {
        final FieldMeta.Update update = new FieldMeta.Update();

        if (valueState.contains(Fields.TEMPLATE_ID))
            update.setTemplateId(templateId);
        if (valueState.contains(Fields.TECHNICAL_NAME))
            update.setTechnicalName(technicalName);
        if (valueState.contains(Fields.DISPLAY_NAME))
            update.setDisplayName(displayName);
        if (valueState.contains(Fields.ORDINAL))
            update.setOrdinal(ordinal);
        if (valueState.contains(Fields.TYPE))
            update.setType(type);

        return update;
    }


    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        valueState.add(Fields.TEMPLATE_ID);
        this.templateId = templateId;
    }

    public String getTechnicalName() {
        return technicalName;
    }

    public void setTechnicalName(String technicalName) {
        valueState.add(Fields.TECHNICAL_NAME);
        this.technicalName = technicalName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        valueState.add(Fields.DISPLAY_NAME);
        this.displayName = displayName;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        valueState.add(Fields.ORDINAL);
        this.ordinal = ordinal;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        valueState.add(Fields.TYPE);
        this.type = type;
    }


    @Override
    public String toString() {
        return "FieldMetaUpdateDto{" +
                "templateId=" + templateId +
                ", technicalName='" + technicalName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", ordinal=" + ordinal +
                ", type=" + type +
                '}';
    }


    /**
     * Enumeration of all fields that participate in DTO role.
     */
    private enum Fields {
        TEMPLATE_ID, TECHNICAL_NAME,
        DISPLAY_NAME, ORDINAL, TYPE
    }
}
