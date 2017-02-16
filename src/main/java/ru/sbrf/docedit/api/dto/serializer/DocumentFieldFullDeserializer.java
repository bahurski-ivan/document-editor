package ru.sbrf.docedit.api.dto.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.stereotype.Component;
import ru.sbrf.docedit.api.dto.field.DocumentFieldFullDto;
import ru.sbrf.docedit.api.dto.field.FieldMetaDto;
import ru.sbrf.docedit.api.dto.value.FieldValueDto;
import ru.sbrf.docedit.model.field.value.FieldType;
import ru.sbrf.docedit.model.field.value.FieldValue;

import java.io.IOException;

import static ru.sbrf.docedit.api.dto.serializer.DocumentFieldFullSerializer.META_NAME;
import static ru.sbrf.docedit.api.dto.serializer.DocumentFieldFullSerializer.VALUE_NAME;

/**
 * Created by SBT-Bakhurskiy-IA on 16.02.2017.
 */
@Component
public class DocumentFieldFullDeserializer extends JsonDeserializer<DocumentFieldFullDto> {
    @Override
    public DocumentFieldFullDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        FieldMetaDto meta = null;
        TreeNode valueNode = null;
        FieldValue value = null;

        while (jsonParser.hasCurrentToken()) {
            final String name = jsonParser.getCurrentName();
            if (name != null) {
                if (META_NAME.equals(name))
                    meta = jsonParser.readValueAs(FieldMetaDto.class);
                else if (VALUE_NAME.equals(name))
                    valueNode = jsonParser.readValueAsTree();
            }
        }

        if (meta != null && valueNode != null) {
            final FieldType type = meta.getType();
            final FieldValueDto valueDto = deserializationContext.readValue(valueNode.traverse(), type.getDtoClass());
            value = type.fromDto(valueDto);
        }

        return new DocumentFieldFullDto(meta, value);
    }
}
