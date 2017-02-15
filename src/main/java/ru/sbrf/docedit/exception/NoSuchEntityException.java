package ru.sbrf.docedit.exception;

/**
 * Created by SBT-Bakhurskiy-IA on 15.02.2017.
 */
public class NoSuchEntityException extends RuntimeException {
    private final long entityId;

    public NoSuchEntityException(long entityId) {
        this.entityId = entityId;
    }

    public NoSuchEntityException() {
        entityId = -1;
    }

    public long getEntityId() {
        return entityId;
    }
}
