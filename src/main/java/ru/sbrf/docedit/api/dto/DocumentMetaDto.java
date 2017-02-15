package ru.sbrf.docedit.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import ru.sbrf.docedit.model.document.DocumentMeta;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by SBT-Bakhurskiy-IA on 15.02.2017.
 */
public class DocumentMetaDto {
    @Min(0)
    @JsonProperty(value = "document_id")
    private long documentId;

    @NotNull
    @Min(0)
    @JsonProperty(value = "template_id", required = true)
    private Long templateId;

    @NotNull
    @NotBlank
    @JsonProperty(value = "document_name", required = true)
    private String documentName;

    public DocumentMetaDto(long documentId, long templateId, String documentName) {
        this.documentId = documentId;
        this.templateId = templateId;
        this.documentName = documentName;
    }

    public DocumentMetaDto() {
    }

    public static DocumentMetaDto toDto(DocumentMeta meta) {
        return new DocumentMetaDto(meta.getDocumentId(), meta.getTemplateId(), meta.getDocumentName());
    }

    public static DocumentMeta fromDto(DocumentMetaDto dto) {
        return new DocumentMeta(dto.getDocumentId(), dto.getTemplateId(), dto.getDocumentName());
    }

    public long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(long documentId) {
        this.documentId = documentId;
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    @Override
    public String toString() {
        return "DocumentMetaDto{" +
                "documentId=" + documentId +
                ", templateId=" + templateId +
                ", documentName='" + documentName + '\'' +
                '}';
    }
}
