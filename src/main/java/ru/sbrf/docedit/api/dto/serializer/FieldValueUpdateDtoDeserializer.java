package ru.sbrf.docedit.api.dto.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.stereotype.Component;
import ru.sbrf.docedit.api.dto.field.update.FieldValueUpdateDto;
import ru.sbrf.docedit.api.dto.value.FieldValueDto;
import ru.sbrf.docedit.model.field.value.FieldType;

import java.io.IOException;

/**
 * Deserializer for class {@code FieldValueUpdateDto}.
 */
@Component
public class FieldValueUpdateDtoDeserializer extends JsonDeserializer<FieldValueUpdateDto> {
    static final String TYPE_FIELD_NAME = "value_type", VALUE_FIELD_NAME = "value";

    @Override
    public FieldValueUpdateDto deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        FieldType type = null;
        TreeNode valueNode = null;
        FieldValueDto valueDto = null;

        while (p.hasCurrentToken()) {
            final String name = p.getCurrentName();
            if (name != null) {
                if (TYPE_FIELD_NAME.equals(name))
                    type = p.readValueAs(FieldType.class);
                else if (VALUE_FIELD_NAME.equals(name))
                    valueNode = p.readValueAsTree();
            }
        }

        if (type != null && valueNode != null)
            valueDto = deserializationContext.readValue(valueNode.traverse(), type.getDtoClass());

        return new FieldValueUpdateDto(type, valueDto);
    }
}
