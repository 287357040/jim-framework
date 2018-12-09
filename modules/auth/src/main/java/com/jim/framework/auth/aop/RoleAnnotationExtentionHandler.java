package com.jim.framework.auth.aop;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.RoleAnnotationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * Created by celiang.hu on 2018-11-18.
 */
public class RoleAnnotationExtentionHandler extends RoleAnnotationHandler{
    private static final Logger logger = LoggerFactory.getLogger(RoleAnnotationExtentionHandler.class);

    public RoleAnnotationExtentionHandler() {
    }

    @Override
    public void assertAuthorized(Annotation a) throws AuthorizationException {
        super.assertAuthorized(a);
    }
}
