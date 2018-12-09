package com.jim.framework.core.configuration;

import com.jim.framework.common.mvc.Constants;
import com.jim.framework.core.mvc.filter.security.XSSDefenceFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

/**
 * Created by celiang.hu on 2018-11-11.
 */
public class XSSDefenceConfiguration {
    @Value("${jim.framework.core.mvc.dynamic-resource.pattern:/api/*}")
    private String dynamicResourcePattern;

    @Bean
    public FilterRegistrationBean xssDefenceFilterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(xssDefenceFilter());
        registration.addUrlPatterns(dynamicResourcePattern);
        registration.setName("xssDefenceFilter");
        registration.setOrder(Constants.FilterOrderGroups.SECURITY_FILTER + 2);
        return registration;
    }

    @Bean
    public Filter xssDefenceFilter() {
        return new XSSDefenceFilter();
    }
}
