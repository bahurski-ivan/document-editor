package ru.sbrf.docedit.dao.impl.h2;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.sbrf.docedit.dao.DocumentMetaDao;
import ru.sbrf.docedit.model.document.DocumentMeta;
import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.model.pagination.Page;
import ru.sbrf.docedit.model.pagination.Pagination;
import ru.sbrf.docedit.model.pagination.impl.PageImpl;
import ru.sbrf.docedit.model.pagination.impl.PaginationImpl;

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
public class H2DocumentMetaDao implements DocumentMetaDao {
    private final JdbcTemplate jdbcTemplate;

    public H2DocumentMetaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long createDocument(DocumentMeta documentMeta) {
        final String sql = "INSERT INTO DOCUMENTS (template_id, document_name) VALUES (?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        int result = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, documentMeta.getTemplateId());
            ps.setString(2, documentMeta.getDocumentName());
            return ps;
        }, keyHolder);

        assert result == 1;

        return keyHolder.getKey().longValue();
    }

    @Override
    public boolean updateDocument(DocumentMeta documentMeta) {
        final String sql = "UPDATE DOCUMENTS SET template_id=?, document_name=? WHERE document_id=?";

        int result = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, documentMeta.getTemplateId());
            ps.setString(2, documentMeta.getDocumentName());
            ps.setLong(3, documentMeta.getDocumentId());
            return ps;
        });

        return result == 1;
    }

    @Override
    public List<DocumentMeta> listAllDocuments() {
        final String sql = "SELECT document_id, template_id, document_name FROM DOCUMENTS";
        return jdbcTemplate.query(sql, DocumentMetaRowMapper.INSTANCE);
    }

    @Override
    public boolean removeDocument(long documentId) {
        final String sql = "DELETE FROM DOCUMENTS WHERE document_id=?";
        int result = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, documentId);
            return ps;
        });
        return result == 1;
    }

    @Override
    public Optional<DocumentMeta> get(long documentId) {
        final String sql = "SELECT document_id, template_id, document_name FROM DOCUMENTS WHERE document_id=?";
        final List<DocumentMeta> queryResult = jdbcTemplate.query(sql, DocumentMetaRowMapper.INSTANCE, documentId);
        return queryResult.size() == 0 ? Optional.empty() : Optional.of(queryResult.get(0));
    }

    @Override
    public Page<DocumentMeta> listPaged(int pageNo, int pageSize, Order order) {
        String sql = "SELECT COUNT(*) FROM DOCUMENTS";
        final List<Integer> queryResult = jdbcTemplate.query(sql, (rs, rn) -> rs.getInt(1));
        assert !queryResult.isEmpty();

        final Pagination pagination = new PaginationImpl(pageNo, pageSize, queryResult.get(0), order);

        if (pagination.getItemsCount() != 0) {
            sql = "SELECT document_id, template_id, document_name FROM DOCUMENTS ORDER BY document_name " + order.toString() + " LIMIT ? OFFSET ?";
            final List<DocumentMeta> items = jdbcTemplate.query(sql, DocumentMetaRowMapper.INSTANCE, pagination.getItemsCount(), pagination.getOffset());
            assert items.size() == pagination.getItemsCount();
            return new PageImpl<>(pagination, items);
        }

        return new PageImpl<>(pagination, Collections.emptyList());
    }

    @Override
    public Page<DocumentMeta> listForTemplate(long templateId, int pageNo, int pageSize, Order order) {
        String sql = "SELECT COUNT(*) FROM DOCUMENTS WHERE template_id=?";
        final List<Integer> queryResult = jdbcTemplate.query(sql, (rs, rn) -> rs.getInt(1), templateId);
        assert !queryResult.isEmpty();

        final Pagination pagination = new PaginationImpl(pageNo, pageSize, queryResult.get(0), order);

        if (pagination.getItemsCount() != 0) {
            sql = "SELECT document_id, template_id, document_name FROM DOCUMENTS WHERE template_id=? ORDER BY document_name " + order.toString() + " LIMIT ? OFFSET ?";
            final List<DocumentMeta> items = jdbcTemplate.query(sql, DocumentMetaRowMapper.INSTANCE, templateId, pagination.getItemsCount(), pagination.getOffset());
            assert items.size() == pagination.getItemsCount();
            return new PageImpl<>(pagination, items);
        }

        return new PageImpl<>(pagination, Collections.emptyList());
    }

    private static class DocumentMetaRowMapper implements RowMapper<DocumentMeta> {
        static final DocumentMetaRowMapper INSTANCE = new DocumentMetaRowMapper();

        @Override
        public DocumentMeta mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DocumentMeta(
                    rs.getLong("document_id"),
                    rs.getLong("template_id"),
                    rs.getString("document_name")
            );
        }
    }
}
