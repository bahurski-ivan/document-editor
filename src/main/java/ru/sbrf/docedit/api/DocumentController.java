package ru.sbrf.docedit.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.sbrf.docedit.api.dto.document.DocumentFullDto;
import ru.sbrf.docedit.api.dto.document.DocumentMetaDto;
import ru.sbrf.docedit.api.dto.value.PageResultDto;
import ru.sbrf.docedit.model.document.DocumentFull;
import ru.sbrf.docedit.model.document.DocumentMeta;
import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.service.DocumentService;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller for document api requests.
 */
@Controller
@RequestMapping("/document")
public class DocumentController {
    private final static String DEFAULT_PAGE_NO = "0";
    private final static String DEFAULT_PAGE_SIZE = "100";
    private final static String DEFAULT_ORDER = "ASC";

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public HttpEntity<DocumentMetaDto> create(@RequestBody @Valid DocumentMetaDto document) {
        final DocumentMeta meta = documentService.create(document.getTemplateId(), document.getDocumentName());
        return ok(DocumentMetaDto.toDto(meta));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/{document_id}", consumes = APPLICATION_JSON_VALUE)
    public void update(@PathVariable("document_id") long documentId, @RequestBody @Valid DocumentMetaDto dto) {
        documentService.update(documentId, dto.getDocumentName());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{document_id}")
    public void remove(@PathVariable("document_id") long documentId) {
        documentService.remove(documentId);
    }

    @GetMapping(value = "/{document_id}", produces = APPLICATION_JSON_VALUE)
    public HttpEntity<?> getOne(@PathVariable("document_id") long documentId,
                                @RequestParam(value = "full", defaultValue = "false") boolean full) {
        if (full) {
            final Optional<DocumentFull> documentFull = documentService.getFull(documentId);
            return documentFull.isPresent() ? ok(DocumentFullDto.toDto(documentFull.get())) : notFound().build();
        } else {
            final Optional<DocumentMeta> documentMeta = documentService.get(documentId);
            return documentMeta.isPresent() ? ok(DocumentMetaDto.toDto(documentMeta.get())) : notFound().build();
        }
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public HttpEntity<PageResultDto<DocumentMetaDto>> getAll(@RequestParam(value = "page_no", defaultValue = DEFAULT_PAGE_NO) int pageNo,
                                                             @RequestParam(value = "page_size", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                                                             @RequestParam(value = "order", defaultValue = DEFAULT_ORDER) Order order) {
        return ok(PageResultDto.toDto(documentService.list(pageNo, pageSize, order), DocumentMetaDto::toDto));
    }
}
