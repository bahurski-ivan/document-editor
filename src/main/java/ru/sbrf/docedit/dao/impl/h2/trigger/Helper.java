package ru.sbrf.docedit.dao.impl.h2.trigger;

import ru.sbrf.docedit.util.SerializationHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Static jdbc helper.
 */
class Helper {
    static Optional<List<Long>> getOrdinals(Connection conn, long templateId) throws SQLException {
        final String sql = "SELECT ordinals FROM FIELDS_ORDINALS WHERE template_id=?";
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
            assert queryResult.size() <= 1;
            return queryResult.isEmpty() ? Optional.empty() : Optional.of(queryResult.get(0));
        }
    }

    static boolean updateOrdinals(Connection conn, long templateId, List<Long> ordinals) throws SQLException {
        ordinals = makeWritable(ordinals);
        final String sql = "UPDATE FIELDS_ORDINALS SET ordinals=? WHERE template_id=?";
        try (final PreparedStatement update = conn.prepareStatement(sql)) {
            update.setBlob(1, SerializationHelper.writeObject(ordinals));
            update.setLong(2, templateId);
            return update.executeUpdate() == 1;
        }
    }

    static boolean createOrdinals(Connection conn, long templateId, List<Long> ordinals) throws SQLException {
        ordinals = makeWritable(ordinals);
        final String sql = "INSERT INTO FIELDS_ORDINALS (template_id, ordinals) VALUES (?, ?)";
        try (final PreparedStatement insert = conn.prepareStatement(sql)) {
            insert.setLong(1, templateId);
            insert.setBlob(2, SerializationHelper.writeObject(ordinals));
            return insert.executeUpdate() == 1;
        }
    }

    static List<Long> makeWritable(List<Long> list) {
        return list instanceof ArrayList ? list : new ArrayList<>(list);
    }

    static int adjustOrdinal(int ordinal, int max) {
        if (ordinal < 0)
            return 0;
        else if (ordinal > max)
            return max;
        return ordinal;
    }
}
