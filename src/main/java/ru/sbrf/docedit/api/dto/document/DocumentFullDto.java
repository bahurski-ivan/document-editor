package ru.sbrf.docedit.api.dto.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.sbrf.docedit.api.dto.field.DocumentFieldFullDto;
import ru.sbrf.docedit.model.document.DocumentFull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by SBT-Bakhurskiy-IA on 16.02.2017.
 */
public class DocumentFullDto {
    @JsonProperty("meta_information")
    private DocumentMetaDto metaDto;

    @JsonProperty("fields")
    private List<DocumentFieldFullDto> fields;

    public DocumentFullDto(DocumentMetaDto metaDto, List<DocumentFieldFullDto> fields) {
        this.metaDto = metaDto;
        this.fields = fields;
    }


    public static DocumentFullDto toDto(DocumentFull documentFull) {
        return new DocumentFullDto(
                new DocumentMetaDto(documentFull.getDocumentId(), documentFull.getTemplateId(), documentFull.getDocumentName()),
                documentFull.getFields().stream()
                        .map(DocumentFieldFullDto::toDto)
                        .collect(Collectors.toList())
        );
    }


    public DocumentMetaDto getMetaDto() {
        return metaDto;
    }

    public void setMetaDto(DocumentMetaDto metaDto) {
        this.metaDto = metaDto;
    }

    public List<DocumentFieldFullDto> getFields() {
        return fields;
    }

    public void setFields(List<DocumentFieldFullDto> fields) {
        this.fields = fields;
    }


    @Override
    public String toString() {
        return "DocumentFullDto{" +
                "metaDto=" + metaDto +
                ", fields=" + fields +
                '}';
    }
}
