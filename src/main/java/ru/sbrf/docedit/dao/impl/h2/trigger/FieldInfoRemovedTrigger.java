package ru.sbrf.docedit.dao.impl.h2.trigger;

import org.h2.api.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbrf.docedit.util.SerializationHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This trigger will delete field id from ordinal list after field with field id is removed.
 */
public class FieldInfoRemovedTrigger implements Trigger {
    private final static Logger LOGGER = LoggerFactory.getLogger(FieldInfoRemovedTrigger.class);


    @Override
    public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {

    }

    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
        assert oldRow.length >= 2;

        final long templateId = (long) oldRow[0];
        final long fieldId = (long) oldRow[1];

//        LOGGER.info("on field remove -- fieldId: " + fieldId + " ; templateId: " + templateId);

        String sql = "SELECT ordinals FROM FIELDS_ORDINALS WHERE template_id=?";

        try (final PreparedStatement select = conn.prepareStatement(sql)) {
            select.setLong(1, templateId);
            final List<List<Long>> queryResult = new ArrayList<>();

            try (final ResultSet rs = select.executeQuery()) {
                while (rs.next()) {
                    @SuppressWarnings("unchecked")
                    final List<Long> ordinals = SerializationHelper.readObject(rs.getBytes(1), List.class);
                    queryResult.add(ordinals);
                }
            }

            if (queryResult.isEmpty())
                queryResult.add(Collections.emptyList());

//            assert queryResult.size() == 1;

            final List<Long> ordinals =
                    queryResult.get(0) instanceof ArrayList ?
                            queryResult.get(0) :
                            new ArrayList<>(queryResult.get(0));

            ordinals.remove((Long) fieldId);

            sql = "UPDATE FIELDS_ORDINALS SET ordinals=? WHERE template_id=?";
            try (final PreparedStatement update = conn.prepareStatement(sql)) {
                update.setBlob(1, SerializationHelper.writeObject(ordinals));
                update.setLong(2, templateId);
                update.executeUpdate();
            }

            LOGGER.info("FieldInfo.afterDelete -- " +
                    "fieldId: " + fieldId +
                    " ; ordinals: " + ordinals);
        }
    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public void remove() throws SQLException {

    }
}
