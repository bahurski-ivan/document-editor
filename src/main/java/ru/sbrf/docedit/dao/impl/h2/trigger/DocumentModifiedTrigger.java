package ru.sbrf.docedit.dao.impl.h2.trigger;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * If document template is modified then this trigger will remove all field values assigned to this document.
 */
public class DocumentModifiedTrigger implements Trigger {
    @Override
    public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {
    }

    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
        assert oldRow.length == 3;
        assert newRow.length == 3;

        final long oldTemplateId = (long) oldRow[0];
        final long newTemplateId = (long) newRow[0];

        if (oldTemplateId != newTemplateId) {
            final String sql = "DELETE FROM FIELD_VALUES WHERE document_id=?";

            try (final PreparedStatement delete = conn.prepareStatement(sql)) {
                delete.setLong(1, oldTemplateId);
                delete.executeUpdate();
            }
        }
    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public void remove() throws SQLException {

    }
}
