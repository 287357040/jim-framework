package com.jim.framework.auth.aop;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.authz.aop.RoleAnnotationMethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by celiang.hu on 2018-11-18.
 */
public class RoleAnnotationExtentionMethodInterceptor extends RoleAnnotationMethodInterceptor {
    public static final Logger logger = LoggerFactory.getLogger(RoleAnnotationExtentionMethodInterceptor.class);

    public RoleAnnotationExtentionMethodInterceptor(AnnotationResolver resolver) {
        this.setResolver(resolver);
        this.setHandler(new RoleAnnotationExtentionHandler());
    }
}
