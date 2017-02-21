package ru.sbrf.docedit.exception;

/**
 * Represents information about missing entity.
 */
public class NoSuchEntityInfo {
    private final String entityId;
    private final EntityType entityType;
    private final DBOperation dbOperation;

    public NoSuchEntityInfo(Object entityId, EntityType entityType, DBOperation dbOperation) {
        this.entityId = entityId.toString();
        this.entityType = entityType;
        this.dbOperation = dbOperation;
    }

    public NoSuchEntityInfo(String entityId, EntityType entityType, DBOperation dbOperation) {
        this.entityId = entityId;
        this.entityType = entityType;
        this.dbOperation = dbOperation;
    }

    public String getEntityId() {
        return entityId;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public DBOperation getDbOperation() {
        return dbOperation;
    }

    @Override
    public String toString() {
        return "NoSuchEntityInfo{" +
                "entityId='" + entityId + '\'' +
                ", entityType=" + entityType +
                ", dbOperation=" + dbOperation +
                '}';
    }
}
