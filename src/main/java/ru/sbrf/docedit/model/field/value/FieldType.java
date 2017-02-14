package ru.sbrf.docedit.model.field.value;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Represents type of field inside document.
 */
public enum FieldType {
    INPUT,
    TEXTAREA,
    CHECKBOX;

    private final static Map<Class<? extends FieldValue>, FieldType> TYPE_MAP;
    private final static Map<FieldType, Map<FieldType, Function<FieldValue, FieldValue>>> CONVERSION_MAP;

    static {
        TYPE_MAP = new HashMap<>();
        CONVERSION_MAP = new HashMap<>();
        TYPE_MAP.put(CheckboxValue.class, CHECKBOX);
        TYPE_MAP.put(InputValue.class, INPUT);
        TYPE_MAP.put(TextAreaValue.class, TEXTAREA);

        final Map<FieldType, Function<FieldValue, FieldValue>> inputMap = new HashMap<>();
        final Map<FieldType, Function<FieldValue, FieldValue>> textAreaMap = new HashMap<>();
        final Map<FieldType, Function<FieldValue, FieldValue>> checkBoxMap = new HashMap<>();

        final Function<FieldValue, FieldValue> direct = v -> v;

        inputMap.put(TEXTAREA, v -> new TextAreaValue(((InputValue) v).getValue()));
        inputMap.put(INPUT, direct);

        textAreaMap.put(INPUT, v -> new InputValue(((TextAreaValue) v).getValue()));
        textAreaMap.put(TEXTAREA, direct);

        checkBoxMap.put(CHECKBOX, direct);

        CONVERSION_MAP.put(INPUT, inputMap);
        CONVERSION_MAP.put(TEXTAREA, textAreaMap);
        CONVERSION_MAP.put(CHECKBOX, checkBoxMap);
    }

    public static FieldType typeFromValue(FieldValue value) {
        return TYPE_MAP.get(value.getClass());
    }

    boolean possibleToLoadFrom(FieldType other) {
        final Map<FieldType, Function<FieldValue, FieldValue>> conversionMap = CONVERSION_MAP.get(this);
        return conversionMap != null && conversionMap.containsKey(other);
    }

    FieldValue toOther(FieldType other, FieldValue value) {
        final Map<FieldType, Function<FieldValue, FieldValue>> conversionMap = CONVERSION_MAP.getOrDefault(this, Collections.emptyMap());
        return conversionMap.getOrDefault(other, v -> null).apply(value);
    }
}
