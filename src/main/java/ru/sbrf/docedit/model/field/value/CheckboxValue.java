package ru.sbrf.docedit.model.field.value;

/**
 * Represents checkbox value field.
 */
public class CheckboxValue implements FieldValue {
    private static final long serialVersionUID = -4458540801250759427L;

    private final boolean isChecked;

    public CheckboxValue(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public FieldValue convertTo(FieldType type) {
        return FieldType.CHECKBOX.toOther(type, this);
    }

    @Override
    public String toString() {
        return "CheckboxValue{" +
                "isChecked=" + isChecked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CheckboxValue that = (CheckboxValue) o;

        return isChecked == that.isChecked;

    }

    @Override
    public int hashCode() {
        return (isChecked ? 1 : 0);
    }
}
