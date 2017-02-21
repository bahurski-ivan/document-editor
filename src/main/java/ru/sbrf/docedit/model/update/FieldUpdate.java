package ru.sbrf.docedit.model.update;

/**
 * Represents value of the field.
 */
public class FieldUpdate<T> {
    private boolean isSet = false;
    private T value, defaultValue;


    public FieldUpdate(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public FieldUpdate() {
    }

    public boolean needToUpdate() {
        return isSet;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        isSet = true;
    }

    public T getDefault() {
        return defaultValue;
    }

    @Override
    public String toString() {
        if (needToUpdate())
            return value.toString();
        else
            return "";
    }
}
