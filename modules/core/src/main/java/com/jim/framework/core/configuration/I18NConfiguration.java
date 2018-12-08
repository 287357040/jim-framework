package com.jim.framework.core.configuration;

import com.jim.framework.common.mvc.ErrorTable;
import com.jim.framework.core.mvc.i18n.RequestLocaleMixedResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.io.IOException;

/**
 * Created by celiang.hu on 2018-11-09.
 */
@Configuration
public class I18NConfiguration implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(I18NConfiguration.class);

    @Value("${jim.framework.core.i18n.locale.param:lang}")
    private String param;
    @Value("${jim.framework.core.i18n.resources.baseName:i18n/messages}")
    private String baseName;
    @Value("${jim.framework.core.i18n.resources.encoding:utf-8}")
    private String encoding;
    @Value("${jim.framework.core.mvc.app.i18n.basename:classpath*:i18n/app}")
    private String apiI18NBaseNames;
    @Value("${jim.framework.core.mvc.app.i18n.locale:zh_CN}")
    private String apiI18nLocale;


    // html文件的国际化资源文件基础设置，前后端分离项目实际不使用
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.setBasename(baseName);
        ms.setDefaultEncoding(encoding);
        ms.setCacheSeconds(-1);
        return ms;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName(param);
        return localeChangeInterceptor;
    }

    @Bean
    public LocaleResolver localeResolver(){
        return new RequestLocaleMixedResolver();
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            ErrorTable.init(apiI18NBaseNames,apiI18nLocale);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
    }
}
