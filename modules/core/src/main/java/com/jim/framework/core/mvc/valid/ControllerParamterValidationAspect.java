package com.jim.framework.core.mvc.valid;

import com.jim.framework.common.mvc.ErrorTable;
import com.jim.framework.common.util.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Valid;
import javax.validation.executable.ExecutableValidator;
import javax.validation.groups.Default;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by celiang.hu on 2018-12-02.
 */
@Aspect
public class ControllerParamterValidationAspect {
    private static final Logger logger = LoggerFactory.getLogger(ControllerParamterValidationAspect.class);

    private ExecutableValidator validator;

    public ControllerParamterValidationAspect(ExecutableValidator validator) {
        this.validator = validator;
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    private void validInvocation() {

    }

    @Before("validInvocation()")
    public void beforeValid() {
        logger.info(Thread.currentThread().getContextClassLoader().toString());
    }


    @Around("validInvocation()")
    public Object aroundValid(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Class[] parameterTypes = methodSignature.getParameterTypes();
        Object[] args = joinPoint.getArgs();
        String[] argNames = methodSignature.getParameterNames();


        if (hasValidAnnotations(method)) {
            Set<ConstraintViolation<Object>> constraintViolationSet = this.validator
                    .validateParameters(joinPoint.getTarget(), method, args, Default.class);

            if (!CollectionUtils.isEmpty(constraintViolationSet)) {
                Iterator<ConstraintViolation<Object>> iterator = constraintViolationSet.iterator();
                Map<String, String> errorInfo = new HashMap<>();
                while (iterator.hasNext()) {
                    ConstraintViolation<Object> constraintViolation = iterator.next();
                    Path.Node last = null;
                    Iterator<Path.Node> p = constraintViolation.getPropertyPath().iterator();
                    while (p.hasNext()) {
                        last = p.next();
                    }
                    String str = last.toString();
                    if (str.indexOf("arg") >= 0) {
                        try {
                            int paramIndex = Integer.parseInt(str.substring(str.indexOf("arg") + 3));
                            errorInfo.put(this
                                            .getRequestParameterName(argNames[paramIndex], parameterAnnotations[paramIndex]),
                                    constraintViolation.getMessage());
                        } catch (NumberFormatException e) {
                            errorInfo.put(str, ErrorTable.convertCode2LocaleMessage(constraintViolation.getMessage()));
                        }
                    } else {
                        errorInfo.put(str, ErrorTable.convertCode2LocaleMessage(constraintViolation.getMessage()));
                    }
                    throw new ControllerParameterValidationException(errorInfo);
                }
            }
        }
        Object res = joinPoint.proceed(joinPoint.getArgs());
        return res;
    }

    private String getRequestParameterName(String methodParameterName, Annotation[] methodParameterAnnotations) {

        for (Annotation annotation : methodParameterAnnotations) {
            if (RequestParam.class.isInstance(annotation)) {
                RequestParam requestParam = (RequestParam) (annotation);
                String name = requestParam.name();
                String value = requestParam.value();
                if (StringUtils.hasText(value)) {
                    return value;
                }

                if (StringUtils.hasText(name)) {
                    return name;
                }
            }
        }

        return methodParameterName;
    }

    private boolean hasValidAnnotations(Method method) {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (Valid.class.isInstance(annotation)) {
                return true;
            }
        }
        return false;
    }


}
