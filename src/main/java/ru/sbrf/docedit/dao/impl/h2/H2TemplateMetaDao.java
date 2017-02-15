package ru.sbrf.docedit.dao.impl.h2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.sbrf.docedit.dao.TemplateMetaDao;
import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.model.pagination.Page;
import ru.sbrf.docedit.model.pagination.Pagination;
import ru.sbrf.docedit.model.pagination.impl.PageImpl;
import ru.sbrf.docedit.model.pagination.impl.PaginationImpl;
import ru.sbrf.docedit.model.template.TemplateMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by SBT-Bakhurskiy-IA on 09.02.2017.
 */
@Component
public class H2TemplateMetaDao implements TemplateMetaDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public H2TemplateMetaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Creates new document template.
     *
     * @param templateMeta template meta information
     * @return id of new template
     */
    @Override
    public long createTemplate(TemplateMeta templateMeta) {
        final String sql = "INSERT INTO DOCUMENT_TEMPLATES (template_name) VALUES (?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        int result = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, templateMeta.getTemplateName());
            return ps;
        }, keyHolder);

        assert result == 1;

        return keyHolder.getKey().longValue();
    }

    /**
     * Updates template by its id.
     *
     * @param templateMeta id of template to update
     * @return {@code true} if template has been updated
     */
    @Override
    public boolean updateTemplateName(TemplateMeta templateMeta) {
        final String sql = "UPDATE DOCUMENT_TEMPLATES " +
                "SET template_name=? " +
                "WHERE template_id=?";

        int result = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, templateMeta.getTemplateName());
            ps.setLong(2, templateMeta.getTemplateId());
            return ps;
        });

        return result == 1;
    }

    @Override
    public List<TemplateMeta> listAll() {
        final String sql = "SELECT template_id, template_name FROM DOCUMENT_TEMPLATES";
        return jdbcTemplate.query(sql, TemplateMetaRowMapper.INSTANCE);
    }

    @Override
    public boolean removeTemplateMeta(long templateId) {
        final String sql = "DELETE FROM DOCUMENT_TEMPLATES WHERE template_id=?";

        int result = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, templateId);
            return ps;
        });

        return result == 1;
    }

    @Override
    public Optional<TemplateMeta> getTemplate(long templateId) {
        final String sql = "SELECT template_id, template_name FROM DOCUMENT_TEMPLATES WHERE template_id=?";
        List<TemplateMeta> list = jdbcTemplate.query(sql, TemplateMetaRowMapper.INSTANCE, templateId);

        assert list.size() <= 1;

        return list.size() == 0 ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    public Page<TemplateMeta> listPaged(int pageNo, int pageSize, Order order) {
        String sql = "SELECT COUNT(*) FROM DOCUMENT_TEMPLATES";
        final List<Integer> queryResult = jdbcTemplate.query(sql, (rs, rn) -> rs.getInt(1));
        assert !queryResult.isEmpty();

        final Pagination pagination = new PaginationImpl(pageNo, pageSize, queryResult.get(0), order);

        if (pagination.getItemsCount() != 0) {
            sql = "SELECT template_id, template_name FROM DOCUMENT_TEMPLATES ORDER BY template_name " + order.toString() + " LIMIT ? OFFSET ?";
            final List<TemplateMeta> items = jdbcTemplate.query(sql, TemplateMetaRowMapper.INSTANCE, pagination.getItemsCount(), pagination.getOffset());
            assert items.size() == pagination.getItemsCount();
            return new PageImpl<>(pagination, items);
        }

        return new PageImpl<>(pagination, Collections.emptyList());
    }

    private static class TemplateMetaRowMapper implements RowMapper<TemplateMeta> {
        private final static TemplateMetaRowMapper INSTANCE = new TemplateMetaRowMapper();

        @Override
        public TemplateMeta mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TemplateMeta(
                    rs.getLong("template_id"),
                    rs.getString("template_name")
            );
        }
    }
}
