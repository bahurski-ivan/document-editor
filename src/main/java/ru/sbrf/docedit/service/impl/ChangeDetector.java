package ru.sbrf.docedit.service.impl;

import ru.sbrf.docedit.model.update.FieldUpdate;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Helper class to simplify change detection.
 */
class ChangeDetector<V> {
    private final Supplier<V> oldSupplier;
    private int changesCount = 0;
    private V oldValue = null;

    ChangeDetector(Supplier<V> oldSupplier) {
        this.oldSupplier = oldSupplier;
    }

    <T> T updatedValue(Function<V, T> oldGetter, Supplier<FieldUpdate<T>> updateGetter) {
        final FieldUpdate<T> update = updateGetter.get();
        if (update.needToUpdate()) {
            final T newValue = update.getValue();
            if (oldValue == null || !oldGetter.apply(oldValue).equals(newValue)) {
                ++changesCount;
                return newValue;
            }
        }
        if (oldValue == null)
            oldValue = oldSupplier.get();

        return oldGetter.apply(oldValue);
    }

    boolean notEmpty() {
        return changesCount != 0;
    }
}
