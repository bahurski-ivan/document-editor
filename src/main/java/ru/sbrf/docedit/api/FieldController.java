package ru.sbrf.docedit.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.sbrf.docedit.api.dto.field.DocumentFieldFullDto;
import ru.sbrf.docedit.api.dto.field.FieldMetaDto;
import ru.sbrf.docedit.api.dto.field.update.FieldMetaUpdateDto;
import ru.sbrf.docedit.api.dto.field.update.FieldValueUpdateDto;
import ru.sbrf.docedit.api.dto.value.CollectionDto;
import ru.sbrf.docedit.model.field.FieldFull;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.service.FieldService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

/**
 * Rest controller for field api requests.
 */
@Controller
@RequestMapping("/field")
public class FieldController {
    private final FieldService fieldService;

    @Autowired
    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }


    // Template fields related methods

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public HttpEntity<FieldMetaDto> create(@RequestBody @Valid FieldMetaDto dto) {
        return ok(FieldMetaDto.toDto(fieldService.create(dto.getTemplateId(), dto.getTechnicalName(), dto.getDisplayName(), dto.getType())));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{field_id}")
    public void update(@PathVariable("field_id") long fieldId, @RequestBody @Valid FieldMetaUpdateDto dto) {
        fieldService.update(fieldId, dto.getTechnicalName(), dto.getDisplayName(), dto.getType(), dto.getOrdinal());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{field_id}")
    public void remove(@PathVariable("field_id") long fieldId) {
        fieldService.remove(fieldId);
    }

    @GetMapping(value = "/{field_id}", produces = APPLICATION_JSON_VALUE)
    public HttpEntity<FieldMetaDto> getOne(@PathVariable("field_id") long fieldId) {
        final Optional<FieldMeta> meta = fieldService.getOne(fieldId);

        if (!meta.isPresent())
            return notFound().build();
        else
            return ok(FieldMetaDto.toDto(meta.get()));
    }

    @GetMapping(value = "/template/{template_id}", produces = APPLICATION_JSON_VALUE)
    public HttpEntity<CollectionDto<FieldMetaDto>> getAllForTemplate(@PathVariable("template_id") long templateId) {
        final List<FieldMeta> meta = fieldService.getAll(templateId);
        return ok(CollectionDto.toDto(meta, FieldMetaDto::toDto));
    }


    // Document value related methods

    @GetMapping(value = "/{field_id}/{document_id}", produces = APPLICATION_JSON_VALUE)
    public HttpEntity<DocumentFieldFullDto> getDocumentField(@PathVariable("field_id") long fieldId,
                                                             @PathVariable("document_id") long documentId) {
        final Optional<FieldFull> fieldFull = fieldService.getDocumentField(documentId, fieldId);
        return fieldFull.isPresent() ? ok(DocumentFieldFullDto.toDto(fieldFull.get())) : notFound().build();
    }

    @GetMapping(value = "/document/{document_id}", produces = APPLICATION_JSON_VALUE)
    public HttpEntity<CollectionDto<DocumentFieldFullDto>> getAllForDocument(@PathVariable("document_id") long documentId) {
        final List<FieldFull> fields = fieldService.getAllDocumentFields(documentId);
        return ok(CollectionDto.toDto(fields, DocumentFieldFullDto::toDto));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{field_id}/{document_id}", produces = APPLICATION_JSON_VALUE)
    public void update(@PathVariable("field_id") long fieldId,
                       @PathVariable("document_id") long documentId,
                       @RequestBody FieldValueUpdateDto updateDto) {
        fieldService.updateFieldValue(
                fieldId,
                documentId,
                updateDto.getType().fromDto(updateDto.getValueDto())
        );
    }
}
