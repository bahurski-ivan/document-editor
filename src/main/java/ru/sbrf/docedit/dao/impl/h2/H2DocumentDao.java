package ru.sbrf.docedit.dao.impl.h2;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.sbrf.docedit.dao.DocumentDao;
import ru.sbrf.docedit.model.document.DocumentFull;
import ru.sbrf.docedit.model.document.DocumentMeta;
import ru.sbrf.docedit.model.field.FieldFull;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.field.value.FieldValue;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by SBT-Bakhurskiy-IA on 09.02.2017.
 */
@Component
public class H2DocumentDao implements DocumentDao {
    private final JdbcTemplate jdbcTemplate;
    private final H2FieldDao fieldDao;

    public H2DocumentDao(JdbcTemplate jdbcTemplate, H2FieldDao fieldDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.fieldDao = fieldDao;
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
    public boolean updateDocument(long documentId, DocumentMeta.Update update) {
        int updateSize = 0;
        Optional<DocumentMeta> oldValue = null;

        if (!update.getTemplateId().needToUpdate()) {
            oldValue = getDocumentMeta(documentId);
            if (!oldValue.isPresent()) return false;
            update.getTemplateId().setValue(oldValue.get().getTemplateId());
        } else ++updateSize;

        if (!update.getDocumentName().needToUpdate()) {
            oldValue = getDocumentMeta(documentId);
            if (!oldValue.isPresent()) return false;
            update.getDocumentName().setValue(oldValue.get().getDocumentName());
        } else ++updateSize;

        if (updateSize == 0)
            return false;

        return updateDocument(documentId, new DocumentMeta(
                documentId,
                update.getTemplateId().getValue(),
                update.getDocumentName().getValue()
        ));
    }

    private boolean updateDocument(long documentId, DocumentMeta m) {
        final String sql = "UPDATE DOCUMENTS SET template_id=?, document_name=? WHERE document_id=?";
        return jdbcTemplate.update(sql, ps -> {
            ps.setLong(1, m.getTemplateId());
            ps.setString(2, m.getDocumentName());
            ps.setLong(3, documentId);
        }) == 1;
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
    public Optional<DocumentMeta> getDocumentMeta(long documentId) {
        final String sql = "SELECT document_id, template_id, document_name FROM DOCUMENTS WHERE document_id=?";
        final List<DocumentMeta> queryResult = jdbcTemplate.query(sql, DocumentMetaRowMapper.INSTANCE, documentId);
        return queryResult.size() == 0 ? Optional.empty() : Optional.of(queryResult.get(0));
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
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
    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
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

    @Override
    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    public Optional<DocumentFull> getFullDocument(long documentId) {
        final Optional<DocumentMeta> dd = getDocumentMeta(documentId);

        if (dd.isPresent()) {
            final DocumentMeta documentMeta = dd.get();
            final List<FieldMeta> metaList = fieldDao.getTemplateFields(documentMeta.getTemplateId());
            final List<Long> ordinals = fieldDao.getOrdinals(documentMeta.getTemplateId()).orElseThrow(AssertionError::new);
            final Map<Long, FieldValue> setValues = fieldDao.getDocumentNonEmptyFields(documentMeta.getDocumentId());
            final Map<Long, Integer> ordinalMap = IntStream.range(0, metaList.size())
                    .mapToObj(i -> i)
                    .collect(Collectors.toMap(ordinals::get, i -> i));

            final List<FieldFull> sortedFields = metaList.stream()
                    .sorted((f1, f2) -> {
                        final Integer i1 = ordinalMap.get(f1.getFieldId());
                        final Integer i2 = ordinalMap.get(f2.getFieldId());
                        assert i1 != null && i2 != null;
                        return i1.compareTo(i2);
                    })
                    .map(fieldMeta -> new FieldFull(fieldMeta, setValues.get(fieldMeta.getFieldId())))
                    .collect(Collectors.toList());

            return Optional.of(new DocumentFull(documentMeta, sortedFields));
        }

        return Optional.empty();
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
