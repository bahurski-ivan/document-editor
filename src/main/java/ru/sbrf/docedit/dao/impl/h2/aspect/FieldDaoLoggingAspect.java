package ru.sbrf.docedit.dao.impl.h2.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sbrf.docedit.dao.impl.h2.H2FieldDao;
import ru.sbrf.docedit.model.field.FieldMeta;

/**
 * Logging aspect for field dao impl.
 */
@Component
@Aspect
public class FieldDaoLoggingAspect {
    private final static org.slf4j.Logger LOGGER =
            org.slf4j.LoggerFactory.getLogger(FieldDaoLoggingAspect.class);
    private final H2FieldDao fieldDao;

    @Autowired
    public FieldDaoLoggingAspect(H2FieldDao fieldDao) {
        this.fieldDao = fieldDao;
    }

    @AfterReturning(value = "execution (public long " +
            "ru.sbrf.docedit.dao.impl.h2.H2FieldDao.createFieldMeta " +
            "(ru.sbrf.docedit.model.field.FieldMeta)) ", returning = "fieldId")
    public void afterFieldAdded(JoinPoint joinPoint, long fieldId) {
        final Object[] args = joinPoint.getArgs();
        if (args.length == 1) {
            final FieldMeta toInsert = (FieldMeta) args[0];
            final FieldMeta persisted = fieldDao.getFieldMeta(fieldId).orElse(null);
            LOGGER.info("FieldInfoInserted: argument -- " + toInsert +
                    " ; persisted -- " + persisted);
        }
    }

    @AfterReturning(value = "execution (public boolean ru.sbrf.docedit.dao.impl.h2." +
            "H2FieldDao.updateFieldMeta(..))", returning = "result")
    public void afterFieldUpdated(JoinPoint joinPoint, boolean result) {
        final Object[] args = joinPoint.getArgs();
        if (args.length == 2) {
            final long fieldId = (long) args[0];
            final FieldMeta newValue = (FieldMeta) args[1];
            final FieldMeta persisted = result ? fieldDao.getFieldMeta(fieldId).orElse(null) : null;

            LOGGER.info("FieldInfoUpdated: " + (result ? ("toSave -- " + newValue + " ; persisted -- " + persisted)
                    : "method returned false -- update failed"));
        }
    }
}
