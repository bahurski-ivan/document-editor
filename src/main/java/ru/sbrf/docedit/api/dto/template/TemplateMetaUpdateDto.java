package ru.sbrf.docedit.api.dto.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sbrf.docedit.model.template.TemplateMeta;

/**
 * DTO for {@code TemplateMeta.Update} class.
 */
public class TemplateMetaUpdateDto {
    @JsonProperty("template_name")
    private String templateName;


    public TemplateMetaUpdateDto(String templateName) {
        this.templateName = templateName;
    }

    public TemplateMetaUpdateDto() {
    }


    public TemplateMeta.Update toEntity() {
        final TemplateMeta.Update update = new TemplateMeta.Update();

        if (templateName != null)
            update.setTemplateName(templateName);

        return update;
    }


    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }


    @Override
    public String toString() {
        return "TemplateMetaUpdateDto{" +
                "templateName='" + templateName + '\'' +
                '}';
    }
}
