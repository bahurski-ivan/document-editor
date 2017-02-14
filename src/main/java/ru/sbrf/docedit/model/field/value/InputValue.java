package ru.sbrf.docedit.model.field.value;

/**
 * Represents value of input field.
 */
public class InputValue implements FieldValue {
    private static final long serialVersionUID = -6782395015407485311L;

    private final String value;

    public InputValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public FieldValue convertTo(FieldType type) {
        return FieldType.INPUT.toOther(type, this);
    }

    @Override
    public String toString() {
        return "InputValue{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InputValue that = (InputValue) o;

        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
