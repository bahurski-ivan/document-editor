package ru.sbrf.docedit.dao.impl.h2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.sbrf.docedit.dao.FieldMetaDao;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.value.FieldType;
import ru.sbrf.docedit.util.SerializationHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by SBT-Bakhurskiy-IA on 09.02.2017.
 */
@Component
public class H2FieldMetaDao implements FieldMetaDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(H2FieldMetaDao.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public H2FieldMetaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long createFieldMeta(FieldMeta fieldMeta) {
        final String sql = "INSERT INTO FIELDS_INFO (template_id, technical_name, display_name, type) VALUES (?, ?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        int result = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, fieldMeta.getTemplateId());
            ps.setString(2, fieldMeta.getTechnicalName());
            ps.setString(3, fieldMeta.getDisplayName());
            ps.setString(4, fieldMeta.getType().toString());
            return ps;
        }, keyHolder);

        assert result == 1;
        return keyHolder.getKey().longValue();
    }

    @Override
    public List<FieldMeta> listFields(long templateId) {
        final String sql = "SELECT field_id, template_id, technical_name, display_name, type, ordinal FROM FIELDS_INFO WHERE template_id=?";
        return jdbcTemplate.query(sql, FieldMetaRowMapper.INSTANCE, templateId);
    }

    @Override
    public boolean removeFieldMeta(long fieldId) {
        final String sql = "DELETE FROM FIELDS_INFO WHERE field_id=?";

        int result = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, fieldId);
            return ps;
        });

        return result == 1;
    }

    @Override
    public boolean updateFieldMeta(FieldMeta fieldMeta) {
        final String sql = "UPDATE FIELDS_INFO SET technical_name=?, display_name=?, type=?, ordinal=? WHERE field_id=?";

        int result = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, fieldMeta.getTechnicalName());
            ps.setString(2, fieldMeta.getDisplayName());
            ps.setString(3, fieldMeta.getType().toString());
            ps.setLong(4, fieldMeta.getFieldId());
            ps.setInt(5, fieldMeta.getOrdinal());
            return ps;
        });

        return result == 1;
    }

    @Override
    public Optional<FieldMeta> get(long fieldId) {
        final String sql = "SELECT field_id, template_id, technical_name, display_name, type, ordinal FROM FIELDS_INFO WHERE field_id=?";
        final List<FieldMeta> queryResult = jdbcTemplate.query(sql, FieldMetaRowMapper.INSTANCE, fieldId);
        return queryResult.size() == 0 ? Optional.empty() : Optional.of(queryResult.get(0));
    }

    @Override
    public Optional<List<Long>> getOrdinals(long templateId) {
        final String sql = "SELECT ordinals FROM FIELDS_ORDINALS WHERE template_id=?";

        @SuppressWarnings("unchecked")
        final List<List<Long>> queryResult = jdbcTemplate.query(sql,
                (rs, rn) -> SerializationHelper.readObject(rs.getBytes(1), List.class), templateId);

        assert queryResult.size() <= 1;

        List<Long> result = null;

        if (!queryResult.isEmpty()) {
            result = queryResult.get(0);
            if (!(result instanceof ArrayList)) {
                LOGGER.error("getOrdinals.result -- not instance of ArrayList !!! converting it to ArrayList ; class -- " + result.getClass().toString());
                result = new ArrayList<>(result);
            }
        }

        return Optional.ofNullable(result);
    }

    private static class FieldMetaRowMapper implements RowMapper<FieldMeta> {
        static final FieldMetaRowMapper INSTANCE = new FieldMetaRowMapper();

        @Override
        public FieldMeta mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new FieldMeta(
                    rs.getLong("field_id"),
                    rs.getLong("template_id"),
                    rs.getString("technical_name"),
                    rs.getString("display_name"),
                    FieldType.valueOf(rs.getString("type")),
                    rs.getInt("ordinal")
            );
        }
    }
}

