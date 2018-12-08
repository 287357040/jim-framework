package com.jim.framework.core.spring;

import com.jim.framework.core.mvc.service.AutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by celiang.hu on 2018-11-28.
 */
public class ApplicationContextListener implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationContextListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // root application context
        if (null == contextRefreshedEvent.getApplicationContext().getParent()) {
            logger.info("**spring初始化完成**");
            Map<String, Object> autoServiceBeans = contextRefreshedEvent.getApplicationContext().getBeansWithAnnotation(AutoService.class);
            for (Object autoService : autoServiceBeans.values()) {
                logger.debug("{}.initMapper", autoService.getClass().getName());
                try {
                    Method method = autoService.getClass().getMethod("initMapper");
                    method.invoke(autoService);
                } catch (Exception e) {
                    logger.error("初始化AutoServic注解服务的initMapper方法异常", e);
                }
            }
        }
    }
}
