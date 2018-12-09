package com.jim.framework.auth.aop;

import org.apache.shiro.authz.aop.PermissionAnnotationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * RequirePermissions注解处理类的扩展，参考Sweet框架实现（处理多租户场景，即同一租户有相同权限命名），方便后续扩展
 * Created by celiang.hu on 2018-11-18.
 */
public class PermissionAnnotationExtentionHandler extends PermissionAnnotationHandler {
    private static final Logger logger = LoggerFactory.getLogger(PermissionAnnotationExtentionHandler.class);

    @Override
    protected String[] getAnnotationValue(Annotation a) {
        return super.getAnnotationValue(a);
    }
}
