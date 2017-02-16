package ru.sbrf.docedit.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.sbrf.docedit.api.dto.template.TemplateFullDto;
import ru.sbrf.docedit.api.dto.template.TemplateMetaDto;
import ru.sbrf.docedit.api.dto.value.PageResultDto;
import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.model.template.TemplateFull;
import ru.sbrf.docedit.model.template.TemplateMeta;
import ru.sbrf.docedit.service.TemplateService;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller for template api requests.
 */
@Controller
@RequestMapping("/template")
public class TemplateController {
    private final static String DEFAULT_PAGE_NO = "0";
    private final static String DEFAULT_PAGE_SIZE = "100";
    private final static String DEFAULT_ORDER = "ASC";

    private final TemplateService templateService;


    @Autowired
    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }


    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public HttpEntity<TemplateMetaDto> create(@RequestBody @Valid TemplateMetaDto template) {
        return new HttpEntity<>(TemplateMetaDto.toDto(templateService.create(template.getTemplateName())));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{template_id}", consumes = APPLICATION_JSON_VALUE)
    public void update(@PathVariable("template_id") long templateId, @RequestBody @Valid TemplateMetaDto dto) {
        templateService.update(templateId, dto.getTemplateName());
    }

    @GetMapping(value = "/{template_id}", produces = APPLICATION_JSON_VALUE)
    public HttpEntity<?> getOne(@PathVariable("template_id") long templateId,
                                @RequestParam(value = "full", defaultValue = "false") boolean fullInformation) {

        if (!fullInformation) {
            final Optional<TemplateMeta> meta = templateService.get(templateId);
            if (!meta.isPresent())
                return notFound().build();
            return ok(TemplateMetaDto.toDto(meta.get()));
        } else {
            final Optional<TemplateFull> templateFull = templateService.getFull(templateId);
            if (!templateFull.isPresent())
                return notFound().build();
            return ok(TemplateFullDto.toDto(templateFull.get()));
        }
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public HttpEntity<?> getAll(@RequestParam(value = "page_no", defaultValue = DEFAULT_PAGE_NO) int pageNo,
                                @RequestParam(value = "page_size", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                                @RequestParam(value = "order", defaultValue = DEFAULT_ORDER) Order order) {
        return ok(PageResultDto.toDto(templateService.list(pageNo, pageSize, order), TemplateMetaDto::toDto));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{template_id}")
    public void remove(@PathVariable("template_id") long templateId) {
        templateService.remove(templateId);
    }
}
