package ru.sbrf.docedit.api.dto.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import ru.sbrf.docedit.model.document.DocumentMeta;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * DTO for {@code DocumentMeta.Update} class.
 */
public class DocumentMetaUpdateDto {
    @Min(0)
    @JsonProperty("template_id")
    private long templateId = -1;
    @NotNull
    @NotEmpty
    @JsonProperty("template_name")
    private String templateName;

    /**
     * Template name set flag.
     */
    @JsonIgnore
    private transient boolean templateNameWasSet;


    DocumentMetaUpdateDto(long templateId, String templateName) {
        this.templateId = templateId;
        this.templateName = templateName;
    }


    public DocumentMeta.Update fromDto() {
        final DocumentMeta.Update update = new DocumentMeta.Update();

        if (templateId != -1) update.setTemplateId(templateId);
        if (templateNameWasSet) update.setDocumentName(templateName);

        return update;
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
        templateNameWasSet = true;
        this.templateName = templateName;
    }


    @Override
    public String toString() {
        return "DocumentMetaUpdateDto{" +
                "templateId=" + templateId +
                ", templateName='" + templateName + '\'' +
                '}';
    }
}
