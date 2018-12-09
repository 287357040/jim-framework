package com.jim.framework.auth;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ShiroFilterFactoryBean的创建接口
 * Created by celiang.hu on 2018-11-16.
 */
public interface ShiroFilterBuilder {
    void build(ShiroFilterFactoryBean shiroFilterFactoryBean);

    class Default implements ShiroFilterBuilder {
        private static final Logger logger = LoggerFactory.getLogger(Default.class);

        public static final Default instance = new Default();

        public static Default getInstance() {
            return instance;
        }

        @Override
        public void build(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        }
    }
}
