package ru.sbrf.docedit.dao.impl.h2.trigger;

import org.h2.api.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.sbrf.docedit.dao.impl.h2.trigger.Helper.*;

/**
 * This trigger will add field id to ordinals list after field with given field id is added.
 */
public class FieldInfoAddedTrigger implements Trigger {
    private final static Logger LOGGER = LoggerFactory.getLogger(FieldInfoAddedTrigger.class);

    @Override
    public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {
    }

    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
        assert newRow.length >= 2;

        final long templateId = (long) newRow[0];
        final long fieldId = (long) newRow[1];

        final Optional<List<Long>> ordinals = getOrdinals(conn, templateId);
        List<Long> o;

        if (ordinals.isPresent()) {
            o = ordinals.get();
            o.add(fieldId);
            updateOrdinals(conn, templateId, o);
        } else {
            o = Collections.singletonList(fieldId);
            createOrdinals(conn, templateId, o);
        }

        LOGGER.info("FieldInfo.afterInsert -- " +
                "fieldId: " + fieldId +
                " ; templateId: " + templateId +
                " ; ordinals: " + o);
    }

    @Override
    public void close() throws SQLException {
    }

    @Override
    public void remove() throws SQLException {
    }
}
