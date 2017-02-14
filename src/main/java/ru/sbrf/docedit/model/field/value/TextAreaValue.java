package ru.sbrf.docedit.model.field.value;

/**
 * Represents text area field value.
 */
public class TextAreaValue implements FieldValue {
    private static final long serialVersionUID = 3578890994817599694L;

    private final String value;

    public TextAreaValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TextAreaValue{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextAreaValue that = (TextAreaValue) o;

        return value.equals(that.value);

    }

    @Override
    public FieldValue convertTo(FieldType type) {
        return FieldType.TEXTAREA.toOther(type, this);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
