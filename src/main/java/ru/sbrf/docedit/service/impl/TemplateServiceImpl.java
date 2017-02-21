package ru.sbrf.docedit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.sbrf.docedit.dao.TemplateDao;
import ru.sbrf.docedit.exception.DBOperation;
import ru.sbrf.docedit.exception.EntityType;
import ru.sbrf.docedit.exception.NoSuchEntityException;
import ru.sbrf.docedit.exception.NoSuchEntityInfo;
import ru.sbrf.docedit.model.pagination.Order;
import ru.sbrf.docedit.model.pagination.Page;
import ru.sbrf.docedit.model.template.TemplateFull;
import ru.sbrf.docedit.model.template.TemplateMeta;
import ru.sbrf.docedit.service.TemplateService;

import java.util.Optional;

/**
 * Created by SBT-Bakhurskiy-IA on 13.02.2017.
 */
@Component
@Transactional(isolation = Isolation.READ_COMMITTED)
public class TemplateServiceImpl implements TemplateService {
    private final TemplateDao templateDao;

    @Autowired
    public TemplateServiceImpl(TemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    @Override
    @Transactional(readOnly = true)
    public TemplateMeta create(String templateName) {
        final long id = templateDao.createTemplate(new TemplateMeta(-1L, templateName));
        return new TemplateMeta(id, templateName);
    }

    @Override
    public void update(long templateId, TemplateMeta.Update update) {
        if (!templateDao.updateTemplate(templateId, update))
            throw NoSuchEntityException.ofSingle(new NoSuchEntityInfo(templateId, EntityType.TEMPLATE, DBOperation.UPDATE));
    }

    @Override
    public void remove(long templateId) {
        if (!templateDao.removeTemplateMeta(templateId))
            throw NoSuchEntityException.ofSingle(new NoSuchEntityInfo(templateId, EntityType.TEMPLATE, DBOperation.REMOVE));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TemplateMeta> get(long templateId) {
        return templateDao.getTemplate(templateId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TemplateMeta> list(int pageNo, int pageSize, Order order) {
        return templateDao.listPaged(pageNo, pageSize, order);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TemplateFull> getFull(long templateId) {
        return templateDao.getFullTemplate(templateId);
    }
}
