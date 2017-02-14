package ru.sbrf.docedit.dao.impl.h2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.sbrf.docedit.dao.FieldValueDao;
import ru.sbrf.docedit.model.field.Field;
import ru.sbrf.docedit.model.field.value.FieldValue;
import ru.sbrf.docedit.util.SerializationHelper;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

/**
 * Created by SBT-Bakhurskiy-IA on 09.02.2017.
 */
@Component
public class H2FieldValueDao implements FieldValueDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public H2FieldValueDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean createFieldValue(Field field) {
        final String sql = "INSERT INTO FIELD_VALUES (document_id, field_id, value) VALUES (?, ?, ?)";

        int result = jdbcTemplate.update(connection -> {
            final InputStream blob = SerializationHelper.writeObject(field.getValue());
            final PreparedStatement ps = connection.prepareStatement(sql);

            ps.setLong(1, field.getDocumentId());
            ps.setLong(2, field.getFieldId());

            if (blob == null)
                ps.setNull(3, Types.BLOB);
            else
                ps.setBlob(3, blob);

            return ps;
        });

        return result == 1;
    }

    @Override
    public boolean updateFieldValue(Field field) {
        final String sql = "UPDATE FIELD_VALUES SET value=? WHERE document_id=? AND field_id=?";

        int result = jdbcTemplate.update(connection -> {
            final InputStream blob = SerializationHelper.writeObject(field.getValue());
            final PreparedStatement ps = connection.prepareStatement(sql);

            if (blob == null)
                ps.setNull(1, Types.BLOB);
            else ps.setBlob(1, blob);

            ps.setLong(2, field.getDocumentId());
            ps.setLong(3, field.getFieldId());

            return ps;
        });

        return result == 1;
    }

    @Override
    public boolean removeFieldValue(Field field) {
        final String sql = "DELETE FROM FIELD_VALUES WHERE document_id=? AND field_id=?";

        int result = jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(sql);

            ps.setLong(1, field.getDocumentId());
            ps.setLong(2, field.getFieldId());

            return ps;
        });

        return result == 1;
    }

    @Override
    public List<Field> listDocumentFields(long documentId) {
        final String sql = "SELECT field_id, document_id, value FROM FIELD_VALUES WHERE document_id=?";
        return jdbcTemplate.query(sql, FieldRowMapper.INSTANCE, documentId);
    }

    @Override
    public Optional<Field> getField(long documentId, long fieldId) {
        final String sql = "SELECT field_id, document_id, value FROM FIELD_VALUES WHERE document_id=? AND field_id=?";
        final List<Field> queryResult = jdbcTemplate.query(sql, FieldRowMapper.INSTANCE, documentId, fieldId);
        return queryResult.size() == 0 ? Optional.empty() : Optional.of(queryResult.get(0));
    }

    @Override
    public List<Field> listFieldValuesByFieldId(long fieldId) {
        final String sql = "SELECT field_id, document_id, value FROM FIELD_VALUES WHERE field_id=?";
        return jdbcTemplate.query(sql, FieldRowMapper.INSTANCE, fieldId);
    }

    private static class FieldRowMapper implements RowMapper<Field> {
        final static FieldRowMapper INSTANCE = new FieldRowMapper();

        @Override
        public Field mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Field(
                    rs.getLong("field_id"),
                    rs.getLong("document_id"),
                    SerializationHelper.readObject(rs.getBytes("value"), FieldValue.class)
            );
        }
    }
}
