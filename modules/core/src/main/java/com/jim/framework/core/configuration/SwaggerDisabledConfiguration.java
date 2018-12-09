package com.jim.framework.core.configuration;

import com.jim.framework.common.mvc.Constants;
import com.jim.framework.core.mvc.filter.SwaggerDocumentFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by celiang.hu on 2018-11-29.
 */
@Configuration
@ConditionalOnExpression("'${jim.framework.core.http.restful.doc.enabled:true}'=='false'")
public class SwaggerDisabledConfiguration {
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        final String SWAGGER_PATTERN = "/swagger-ui.html";
        registrationBean.addUrlPatterns(SWAGGER_PATTERN);
        registrationBean.setFilter(swaggerDocumentFilter());
        registrationBean.setName("swaggerDoucmentFilter");
        registrationBean.setOrder(Constants.FilterOrderGroups.TOP_FILTER + 1);
        return registrationBean;
    }

    @Bean
    public SwaggerDocumentFilter swaggerDocumentFilter() {
        return new SwaggerDocumentFilter();
    }
}
