package ru.sbrf.docedit.dao.impl.h2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.sbrf.docedit.dao.FieldDao;
import ru.sbrf.docedit.exception.*;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.FieldValueHolder;
import ru.sbrf.docedit.model.field.value.FieldType;
import ru.sbrf.docedit.model.field.value.FieldValue;
import ru.sbrf.docedit.util.SerializationHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by SBT-Bakhurskiy-IA on 09.02.2017.
 */
@Component
public class H2FieldDao implements FieldDao {
    private final static Logger LOGGER = LoggerFactory.getLogger(H2FieldDao.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public H2FieldDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long createFieldMeta(FieldMeta fieldMeta) {
        final String sql = "INSERT INTO FIELDS_INFO (template_id, technical_name, display_name, type, ordinal) VALUES (?, ?, ?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        int result = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, fieldMeta.getTemplateId());
            ps.setString(2, fieldMeta.getTechnicalName());
            ps.setString(3, fieldMeta.getDisplayName());
            ps.setString(4, fieldMeta.getType().toString());
            ps.setInt(5, fieldMeta.getOrdinal());
            return ps;
        }, keyHolder);

        assert result == 1;
        return keyHolder.getKey().longValue();
    }

    @Override
    public List<FieldMeta> getTemplateFields(long templateId) {
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
    public boolean updateFieldMeta(long fieldId, FieldMeta.Update update) {
        int updateSize = 0;
        Optional<FieldMeta> oldValue = null;

        long templateId;
        String technicalName, displayName;
        FieldType type;
        int ordinal;

        if (update.getTemplateId().needToUpdate()) {
            templateId = update.getTemplateId().getValue();
            ++updateSize;
        } else {
            oldValue = getFieldMeta(fieldId);
            if (!oldValue.isPresent()) return false;
            templateId = oldValue.get().getTemplateId();
        }

        if (update.getTechnicalName().needToUpdate()) {
            technicalName = update.getTechnicalName().getValue();
            ++updateSize;
        } else {
            if (oldValue == null) oldValue = getFieldMeta(fieldId);
            if (!oldValue.isPresent()) return false;
            technicalName = oldValue.get().getTechnicalName();
        }

        if (update.getDisplayName().needToUpdate()) {
            displayName = update.getDisplayName().getValue();
            ++updateSize;
        } else {
            if (oldValue == null) oldValue = getFieldMeta(fieldId);
            if (!oldValue.isPresent()) return false;
            displayName = oldValue.get().getDisplayName();
        }

        if (update.getType().needToUpdate()) {
            type = update.getType().getValue();
            ++updateSize;
        } else {
            if (oldValue == null) oldValue = getFieldMeta(fieldId);
            if (!oldValue.isPresent()) return false;
            type = oldValue.get().getType();
        }

        if (update.getOrdinal().needToUpdate()) {
            ordinal = update.getOrdinal().getValue();
            ++updateSize;
        } else {
            if (oldValue == null) oldValue = getFieldMeta(fieldId);
            if (!oldValue.isPresent()) return false;
            ordinal = oldValue.get().getOrdinal();
        }

        if (updateSize == 0)
            throw new EmptyUpdate();

        return updateFieldMeta(fieldId, new FieldMeta(
                fieldId,
                templateId,
                technicalName,
                displayName,
                type,
                ordinal
        ));
    }

    private boolean updateFieldMeta(long fieldId, FieldMeta m) {
        final String sql = "UPDATE FIELDS_INFO SET template_id=?, technical_name=?, " +
                "display_name=?, type=?, ordinal=? WHERE field_id=?";
        return jdbcTemplate.update(sql, ps -> {
            ps.setLong(1, m.getTemplateId());
            ps.setString(2, m.getTechnicalName());
            ps.setString(3, m.getDisplayName());
            ps.setString(4, m.getType().toString());
            ps.setInt(5, m.getOrdinal());
            ps.setLong(6, fieldId);
        }) == 1;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FieldMeta> getFieldMeta(long fieldId) {
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

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean setFieldValue(long documentId, long fieldId, FieldValue newValue) {
        boolean documentExist = jdbcTemplate.query("SELECT COUNT(*) FROM DOCUMENTS WHERE document_id=?",
                (rs, rn) -> rs.getInt(1), documentId).get(0) == 1;
        boolean fieldExist = jdbcTemplate.query("SELECT COUNT(*) FROM FIELDS_INFO WHERE field_id=?",
                (rs, rn) -> rs.getInt(1), fieldId).get(0) == 1;

        if (!documentExist)
            throw NoSuchEntityException.ofSingle(new NoSuchEntityInfo(documentId, EntityType.DOCUMENT, DBOperation.UPDATE));

        if (!fieldExist)
            throw NoSuchEntityException.ofSingle(new NoSuchEntityInfo(fieldId, EntityType.FIELD, DBOperation.UPDATE));

        int rowsAffected;
        String sql = "SELECT value FROM FIELD_VALUES WHERE field_id=? AND document_id=?";

        if (jdbcTemplate.query(sql, (rs, rn) -> null, fieldId, documentId).size() == 0) {
            sql = "INSERT INTO FIELD_VALUES(document_id, field_id, value) VALUES (?,?,?)";
            rowsAffected = jdbcTemplate.update(sql, documentId, fieldId, SerializationHelper.writeObject(newValue));
        } else {
            sql = "UPDATE FIELD_VALUES SET value=? WHERE document_id=? AND field_id=?";
            rowsAffected = jdbcTemplate.update(sql, SerializationHelper.writeObject(newValue), documentId, fieldId);
        }

        return rowsAffected == 1;
    }

    @Override
    public Optional<FieldValueHolder> getFieldValue(long documentId, long fieldId) {
        final String sql = "SELECT value FROM FIELD_VALUES WHERE document_id=? AND field_id=?";
        final List<FieldValue> queryResult = jdbcTemplate.query(sql, (rs, rn) ->
                        SerializationHelper.readObject(rs.getBytes(1), FieldValue.class),
                documentId, fieldId);

        return queryResult.isEmpty() ? Optional.empty() :
                Optional.of(new FieldValueHolder(fieldId, documentId, queryResult.get(0)));
    }

    @Override
    public Map<Long, FieldValue> getDocumentNonEmptyFields(long documentId) {
        final String sql = "SELECT field_id, value FROM FIELD_VALUES WHERE document_id=?";
        return jdbcTemplate.query(sql, (rs, rn) ->
                new FieldValueHolder(rs.getLong(1), documentId, SerializationHelper.
                        readObject(rs.getBytes(2), FieldValue.class)), documentId)
                .stream()
                .collect(Collectors.toMap(FieldValueHolder::getFieldId, FieldValueHolder::getValue));
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

