package ru.sbrf.docedit.dao.impl.h2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.sbrf.docedit.dao.FieldOrdinalsDao;
import ru.sbrf.docedit.exception.NoSuchEntityException;
import ru.sbrf.docedit.util.SerializationHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by SBT-Bakhurskiy-IA on 13.02.2017.
 */
@Component
public class H2FieldOrdinalsDao implements FieldOrdinalsDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public H2FieldOrdinalsDao(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }

    @Override
    public void create(long templateId, long fieldId, int ordinal) {
        String sql = "SELECT template_id, ordinals FROM FIELDS_ORDINALS WHERE template_id=?";
        final List<List<Long>> queriedResult = jdbcTemplate.query(sql, FieldsOrdinalsRowMapper.INSTANCE, templateId);
        assert queriedResult.size() <= 1;

        if (queriedResult.isEmpty()) {
            sql = "INSERT INTO FIELDS_ORDINALS (template_id, ordinals) VALUES (?, ?)";
            int result = jdbcTemplate.update(sql, templateId, Collections.singletonList(fieldId));
        } else {
            final List<Long> orderedIds = queriedResult.get(0);
            ordinal = adjustOrdinal(ordinal, orderedIds.size());
            orderedIds.add(ordinal, fieldId);
            updateBatch(templateId, orderedIds);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void update(long templateId, long fieldId, int ordinal) {
        final List<Long> orderedIds = getOrderedFields(templateId);
        final int oldIndex = orderedIds.indexOf(fieldId);
        ordinal = adjustOrdinal(ordinal, orderedIds.size());

        if (oldIndex != -1 && oldIndex != ordinal) {
            orderedIds.remove((int) oldIndex);
            orderedIds.add(ordinal, fieldId);
            updateBatch(templateId, orderedIds);
        }
    }

    @Override
    public void updateBatch(long templateId, List<Long> orderedFieldsIds) {
        Objects.requireNonNull(orderedFieldsIds);
        final String sql = "UPDATE FIELDS_ORDINALS SET ordinals=? WHERE template_id=?";
        int result = jdbcTemplate.update(sql, SerializationHelper.writeObject(orderedFieldsIds), templateId);

        if (result != 1)
            throw new NoSuchEntityException(templateId);
    }

    @Override
    public List<Long> getOrderedFields(long templateId) {
        final String sql = "SELECT template_id, ordinals FROM FIELDS_ORDINALS WHERE template_id=?";
        final List<List<Long>> queriedResult = jdbcTemplate.query(sql, FieldsOrdinalsRowMapper.INSTANCE, templateId);
        assert queriedResult.size() <= 1;
        return queriedResult.isEmpty() ? Collections.emptyList() : queriedResult.get(0);
    }

    @Override
    public int getOrdinal(long templateId, long fieldId) {
        final List<Long> orderedIds = getOrderedFields(templateId);
        return orderedIds.indexOf(fieldId);
    }

    private int adjustOrdinal(int ordinal, int max) {
        if (ordinal < 0)
            ordinal = 0;
        else if (ordinal > max)
            ordinal = max;

        return ordinal;
    }

    private static class FieldsOrdinalsRowMapper implements RowMapper<List<Long>> {
        final static FieldsOrdinalsRowMapper INSTANCE = new FieldsOrdinalsRowMapper();

        @Override
        public List<Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
            @SuppressWarnings("unchecked")
            final List<Long> result = (List<Long>) SerializationHelper.readObject(rs.getBytes("ordinals"), List.class);
            return result;
        }
    }
}
