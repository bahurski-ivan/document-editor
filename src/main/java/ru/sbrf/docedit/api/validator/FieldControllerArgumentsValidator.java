package ru.sbrf.docedit.api.validator;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.sbrf.docedit.api.dto.field.update.FieldMetaUpdateDto;

/**
 * Created by SBT-Bakhurskiy-IA on 15.02.2017.
 */
@Aspect
@Component
public class FieldControllerArgumentsValidator {
    private final static Logger LOGGER = LoggerFactory.getLogger(FieldControllerArgumentsValidator.class);

    @Pointcut("execution (public * ru.sbrf.docedit.api.FieldController.* (..))")
    public void allPublicMethods() {

    }

    @Before(value = "execution (public * ru.sbrf.docedit.api.*.* (long,..))" +
            "&& args(fieldId,..)", argNames = "joinPoint,fieldId")
    public void validateId(JoinPoint joinPoint, long fieldId) {
        LOGGER.info(joinPoint.getSignature().getName() + ": id -- " + fieldId);
    }

    @Before(value = "execution (public * ru.sbrf.docedit.api.FieldController.update (..))" +
            "&& args(fieldId, dto)", argNames = "joinPoint,fieldId,dto")
    public void validateUpdateParameters(JoinPoint joinPoint, long fieldId, FieldMetaUpdateDto dto) {
    }
}
