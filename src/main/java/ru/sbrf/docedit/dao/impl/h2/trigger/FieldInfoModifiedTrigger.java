package ru.sbrf.docedit.dao.impl.h2.trigger;

import org.h2.api.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbrf.docedit.model.field.Field;
import ru.sbrf.docedit.model.field.value.FieldType;
import ru.sbrf.docedit.model.field.value.FieldValue;
import ru.sbrf.docedit.util.SerializationHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.sbrf.docedit.dao.impl.h2.trigger.Helper.getOrdinals;
import static ru.sbrf.docedit.dao.impl.h2.trigger.Helper.updateOrdinals;

/**
 * This trigger will update field values when field type is modified.
 */
public class FieldInfoModifiedTrigger implements Trigger {
    private final static Logger LOGGER = LoggerFactory.getLogger(FieldInfoModifiedTrigger.class);

    @Override
    public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {
    }

    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
        assert newRow.length == 6;
        assert oldRow.length == 6;

        final long templateId = (long) newRow[0];
        final long fieldId = (long) newRow[1];
        final FieldType oldType = FieldType.valueOf((String) oldRow[4]);
        final FieldType newType = FieldType.valueOf((String) newRow[4]);
        final int oldOrdinal = (int) oldRow[5];
        final int newOrdinal = (int) newRow[5];

        int fieldModified = 0;

        if (oldType != newType) {
            final List<Field> fieldsToModify = new ArrayList<>();
            String sql = "SELECT document_id, field_id, value FROM FIELD_VALUES WHERE field_id=?";
            try (final PreparedStatement select = conn.prepareStatement(sql)) {
                select.setLong(1, fieldId);
                try (final ResultSet rs = select.executeQuery()) {
                    while (rs.next()) {
                        fieldsToModify.add(new Field(
                                rs.getLong("field_id"),
                                rs.getLong("document_id"),
                                SerializationHelper.readObject(rs.getBytes("value"), FieldValue.class)
                        ));
                    }
                }
            }
            sql = "UPDATE FIELD_VALUES SET value=? WHERE field_id=?";
            for (Field field : fieldsToModify)
                try (final PreparedStatement update = conn.prepareStatement(sql)) {
                    update.setBlob(1, SerializationHelper.writeObject(field.getValue().convertTo(newType)));
                }
            fieldModified = fieldsToModify.size();
        }

        if (oldOrdinal != newOrdinal) {
            final List<Long> ordinals = getOrdinals(conn, templateId).orElseThrow(AssertionError::new);
            int oldIndex = ordinals.indexOf(fieldId);
            assert oldIndex != -1;
            ordinals.remove((int) oldIndex);
            ordinals.add(newOrdinal, fieldId);
            assert updateOrdinals(conn, templateId, ordinals);
        }

        LOGGER.info("FieldInfo.afterTypeUpdate -- " +
                "fieldId: " + fieldId +
                " ; from ordinal: " + oldOrdinal + " to: " + newOrdinal +
                " ; from type: " + oldType + " to: " + newType +
                " ; values updated: " + fieldModified);
    }

    @Override
    public void close() throws SQLException {
    }

    @Override
    public void remove() throws SQLException {
    }
}
