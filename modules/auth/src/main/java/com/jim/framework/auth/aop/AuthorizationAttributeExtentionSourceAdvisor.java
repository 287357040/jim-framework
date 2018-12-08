package com.jim.framework.auth.aop;

import org.apache.shiro.spring.security.interceptor.AopAllianceAnnotationsAuthorizingMethodInterceptor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by celiang.hu on 2018-11-18.
 */
public class AuthorizationAttributeExtentionSourceAdvisor extends AuthorizationAttributeSourceAdvisor {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationAttributeExtentionSourceAdvisor.class);

    public AuthorizationAttributeExtentionSourceAdvisor(){
        this.setAdvice(new AnnotationsAuthorizingExtentionMethodInterceptor());
    }
}
