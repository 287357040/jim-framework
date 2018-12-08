package com.jim.framework.auth.aop;

import org.aopalliance.aop.Advice;
import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.authz.aop.*;
import org.apache.shiro.spring.aop.SpringAnnotationResolver;
import org.apache.shiro.spring.security.interceptor.AopAllianceAnnotationsAuthorizingMethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by celiang.hu on 2018-11-18.
 */
public class AnnotationsAuthorizingExtentionMethodInterceptor extends AopAllianceAnnotationsAuthorizingMethodInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationsAuthorizingExtentionMethodInterceptor.class);

    public AnnotationsAuthorizingExtentionMethodInterceptor() {
        List<AuthorizingAnnotationMethodInterceptor> interceptors =
                new ArrayList<AuthorizingAnnotationMethodInterceptor>(5);

        //use a Spring-specific Annotation resolver - Spring's AnnotationUtils is nicer than the
        //raw JDK resolution process.
        AnnotationResolver resolver = new SpringAnnotationResolver();
        //we can re-use the same resolver instance - it does not retain state:
        interceptors.add(new RoleAnnotationExtentionMethodInterceptor(resolver));
        interceptors.add(new PermissionAnnotationExtentionMethodInterceptor(resolver));
        interceptors.add(new AuthenticatedAnnotationMethodInterceptor(resolver));
        interceptors.add(new UserAnnotationMethodInterceptor(resolver));
        interceptors.add(new GuestAnnotationMethodInterceptor(resolver));

        this.setMethodInterceptors(interceptors);
    }
}
