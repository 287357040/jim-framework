package com.jim.framework.auth.aop;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.authz.aop.PermissionAnnotationMethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by celiang.hu on 2018-11-18.
 */
public class PermissionAnnotationExtentionMethodInterceptor extends PermissionAnnotationMethodInterceptor {
    public static final Logger logger = LoggerFactory.getLogger(PermissionAnnotationExtentionMethodInterceptor.class);

    public PermissionAnnotationExtentionMethodInterceptor(AnnotationResolver annotationResolver) {
        this.setResolver(annotationResolver);
        this.setHandler(new PermissionAnnotationExtentionHandler());
    }
}
