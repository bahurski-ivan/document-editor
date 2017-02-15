package ru.sbrf.docedit.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.sbrf.docedit.api.dto.CollectionDto;
import ru.sbrf.docedit.api.dto.FieldMetaDto;
import ru.sbrf.docedit.api.dto.FieldMetaUpdateDto;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.service.FieldService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

/**
 * Created by SBT-Bakhurskiy-IA on 15.02.2017.
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

    @GetMapping("/{field_id}")
    public HttpEntity<FieldMetaDto> getOne(@PathVariable("field_id") long fieldId) {
        final Optional<FieldMeta> meta = fieldService.getOne(fieldId);

        if (!meta.isPresent())
            return notFound().build();
        else
            return ok(FieldMetaDto.toDto(meta.get()));
    }

    @GetMapping("/template/{template_id}")
    public HttpEntity<CollectionDto<FieldMetaDto>> getAllForTemplate(@PathVariable("template_id") long templateId) {
        final List<FieldMeta> meta = fieldService.getAll(templateId);
        return ok(CollectionDto.toDto(meta, FieldMetaDto::toDto));
    }


    // Document value related methods
}
