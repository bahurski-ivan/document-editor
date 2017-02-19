package ru.sbrf.docedit.model.field.value;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

/**
 * Created by SBT-Bakhurskiy-IA on 14.02.2017.
 */
public class FieldTypeTest {
    @Test
    public void isConversionPossible() throws Exception {
//        final FieldUpdate v1 = new CheckboxValue(true);
//        final FieldUpdate v2 = new InputValue("...");
        assertFalse(FieldType.INPUT.possibleToLoadFrom(FieldType.CHECKBOX));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void fromOther() throws Exception {
        final FieldValue v1 = new InputValue("...");
        final FieldValue expected = new TextAreaValue("...");
        assertEquals(FieldType.INPUT.toOther(FieldType.TEXTAREA, v1), expected);
    }

    @Test
    public void typeFromValue() throws Exception {
        assertEquals(FieldType.CHECKBOX, FieldType.typeFromValue(new CheckboxValue(false)));
    }
}