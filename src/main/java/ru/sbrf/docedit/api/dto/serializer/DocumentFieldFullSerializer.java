package ru.sbrf.docedit.api.dto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;
import ru.sbrf.docedit.api.dto.field.DocumentFieldFullDto;
import ru.sbrf.docedit.api.dto.field.FieldMetaDto;
import ru.sbrf.docedit.api.dto.value.FieldValueDto;

import java.io.IOException;

/**
 * Created by SBT-Bakhurskiy-IA on 16.02.2017.
 */
@Component
public class DocumentFieldFullSerializer extends JsonSerializer<DocumentFieldFullDto> {
    static final String META_NAME = "meta_information";
    static final String VALUE_NAME = "field_value";

    @Override
    public void serialize(DocumentFieldFullDto documentFieldFullDto, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        final FieldMetaDto metaDto = documentFieldFullDto.getMetaDto();
        final FieldValueDto dto = metaDto.getType().toDto(documentFieldFullDto.getValue());

        jsonGenerator.writeObjectField(META_NAME, metaDto);
        jsonGenerator.writeObjectField(VALUE_NAME, dto);
    }
}
