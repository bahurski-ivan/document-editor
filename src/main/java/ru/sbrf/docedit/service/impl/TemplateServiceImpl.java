package ru.sbrf.docedit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.sbrf.docedit.dao.FieldMetaDao;
import ru.sbrf.docedit.dao.FieldOrdinalsDao;
import ru.sbrf.docedit.dao.TemplateMetaDao;
import ru.sbrf.docedit.exception.NoSuchEntityException;
import ru.sbrf.docedit.model.field.FieldMeta;
import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.model.pagination.Page;
import ru.sbrf.docedit.model.template.TemplateFull;
import ru.sbrf.docedit.model.template.TemplateMeta;
import ru.sbrf.docedit.service.TemplateService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by SBT-Bakhurskiy-IA on 13.02.2017.
 */
@Component
@Transactional(isolation = Isolation.READ_COMMITTED)
public class TemplateServiceImpl implements TemplateService {
    private final TemplateMetaDao templateMetaDao;
    private final FieldOrdinalsDao ordinalsDao;
    private final FieldMetaDao fieldMetaDao;

    @Autowired
    public TemplateServiceImpl(TemplateMetaDao templateMetaDao, FieldOrdinalsDao ordinalsDao, FieldMetaDao fieldMetaDao) {
        this.templateMetaDao = templateMetaDao;
        this.ordinalsDao = ordinalsDao;
        this.fieldMetaDao = fieldMetaDao;
    }

    @Override
    @Transactional(readOnly = true)
    public TemplateMeta create(String templateName) {
        final long id = templateMetaDao.createTemplate(new TemplateMeta(-1L, templateName));
        return new TemplateMeta(id, templateName);
    }

    @Override
    public TemplateMeta update(long templateId, String templateName) {
        if (!templateMetaDao.updateTemplateName(new TemplateMeta(templateId, templateName)))
            throw new NoSuchEntityException();
        return new TemplateMeta(templateId, templateName);
    }

    @Override
    public void remove(long templateId) {
        if (!templateMetaDao.removeTemplateMeta(templateId))
            throw new NoSuchEntityException();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TemplateMeta> get(long templateId) {
        return templateMetaDao.getTemplate(templateId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TemplateMeta> list(int pageNo, int pageSize, Order order) {
        return templateMetaDao.listPaged(pageNo, pageSize, order);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    public Optional<TemplateFull> getFull(long templateId) {
        final Optional<TemplateMeta> mm = templateMetaDao.getTemplate(templateId);

        if (mm.isPresent()) {
            final TemplateMeta meta = mm.get();
            final List<Long> orderedIds = ordinalsDao.getOrderedFields(templateId);

            final Map<Long, Integer> fieldIdIndexMap = IntStream
                    .range(0, orderedIds.size()).mapToObj(i -> i)
                    .collect(Collectors.toMap(orderedIds::get, i -> i));
            final List<FieldMeta> metaList = fieldMetaDao.listFields(templateId);

            assert orderedIds.size() == metaList.size();

            return Optional.of(new TemplateFull(meta.getTemplateId(), meta.getTemplateName(),
                    metaList.stream().sorted((f1, f2) -> {
                        final int i1 = fieldIdIndexMap.get(f1.getFieldId());
                        final int i2 = fieldIdIndexMap.get(f2.getFieldId());
                        return Integer.compare(i1, i2);
                    }).collect(Collectors.toList()))
            );
        }

        return Optional.empty();
    }
}
