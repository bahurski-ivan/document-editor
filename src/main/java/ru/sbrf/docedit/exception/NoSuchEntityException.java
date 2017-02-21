package ru.sbrf.docedit.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by SBT-Bakhurskiy-IA on 15.02.2017.
 */
public class NoSuchEntityException extends RuntimeException {
    private final List<NoSuchEntityInfo> missingEntities;

    public NoSuchEntityException(List<NoSuchEntityInfo> missingEntities) {
        this.missingEntities = new ArrayList<>(missingEntities);
    }

    public static NoSuchEntityException ofSingle(NoSuchEntityInfo info) {
        return new NoSuchEntityException(Collections.singletonList(info));
    }

    @Override
    public String toString() {
        return "NoSuchEntityException{" +
                "missingEntities=" + missingEntities +
                '}';
    }
}
