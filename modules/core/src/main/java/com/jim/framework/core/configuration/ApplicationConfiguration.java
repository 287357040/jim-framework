package com.jim.framework.core.configuration;

import com.jim.framework.core.spring.ApplicationContextListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by celiang.hu on 2018-11-28.
 */
@Configuration
public class ApplicationConfiguration {
    @Bean
    public ApplicationContextListener applicationContextListener() {
        return new ApplicationContextListener();
    }
}
