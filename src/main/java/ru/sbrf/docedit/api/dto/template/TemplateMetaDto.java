package ru.sbrf.docedit.api.dto.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import ru.sbrf.docedit.model.template.TemplateMeta;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by SBT-Bakhurskiy-IA on 15.02.2017.
 */
public class TemplateMetaDto {
    @NotNull
    @Min(0)
    @JsonProperty(value = "template_id")
    private long templateId;

    @NotNull
    @NotBlank
    @JsonProperty(value = "template_name", required = true)
    private String templateName;

    public TemplateMetaDto() {
    }

    public TemplateMetaDto(long templateId, String templateName) {
        this.templateId = templateId;
        this.templateName = templateName;
    }

    public static TemplateMeta fromDto(TemplateMetaDto dto) {
        return new TemplateMeta(dto.getTemplateId(), dto.getTemplateName());
    }

    public static TemplateMetaDto toDto(TemplateMeta meta) {
        return new TemplateMetaDto(meta.getTemplateId(), meta.getTemplateName());
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Override
    public String toString() {
        return "TemplateMetaDto{" +
                "templateId=" + templateId +
                ", templateName='" + templateName + '\'' +
                '}';
    }
}
