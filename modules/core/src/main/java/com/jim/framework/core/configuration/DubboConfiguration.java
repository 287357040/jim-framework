package com.jim.framework.core.configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.jim.framework.core.RuntimeContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by celiang.hu on 2018-11-23.
 */
@Configuration
public class DubboConfiguration {

    @Bean
    ApplicationConfig applicationConfig() {
        return RuntimeContext.getInstance().getJimApplication();
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(RuntimeContext.getInstance().getJimApplication().getLinkedInfo().getRegistryInfo().getConnectionString());

//        registryConfig.setClient("curator");
        return registryConfig;
    }
}
