package ru.sbrf.docedit.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sbrf.docedit.api.dto.DocumentMetaDto;
import ru.sbrf.docedit.model.document.DocumentMeta;
import ru.sbrf.docedit.service.DocumentService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Controller for document api requests.
 */
@Controller
@RequestMapping("/document")
public class DocumentController {
    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public HttpEntity<DocumentMetaDto> create(@RequestBody DocumentMetaDto document) {
        final DocumentMeta meta = documentService.create(document.getTemplateId(), document.getDocumentName());
        return new HttpEntity<>(DocumentMetaDto.toDto(meta));
    }
}
