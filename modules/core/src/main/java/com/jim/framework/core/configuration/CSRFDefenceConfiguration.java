package com.jim.framework.core.configuration;

import com.jim.framework.common.mvc.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.jim.framework.core.mvc.filter.security.CSRFDefenceFilter;

import javax.servlet.Filter;

/**
 * Created by celiang.hu on 2018-11-11.
 */
@ConditionalOnExpression("'${jim.framework.core.http.defence.csrf}'=='true'")
@Configuration
public class CSRFDefenceConfiguration {

    @Value("${jim.framework.core.mvc.dynamic-resource.pattern:/api/*}")
    private String dynamicResourcePattern;

    @Bean
    public FilterRegistrationBean xssDefenceFilterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(csrfDefenceFilter());
        registration.addUrlPatterns(dynamicResourcePattern);
        registration.setName("csrfDefenceFilter");
        registration.setOrder(Constants.FilterOrderGroups.SECURITY_FILTER + 1);
        return registration;
    }

    @Bean
    public Filter csrfDefenceFilter() {
        return new CSRFDefenceFilter();
    }
}
