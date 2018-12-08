package com.jim.framework.core.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * Created by celiang.hu on 2018-11-30.
 */
@EnableSwagger2
@Configuration
public class SwaggerDocketConfiguration implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {
    private static final Logger logger = LoggerFactory.getLogger(SwaggerDocketConfiguration.class);


    @Bean
    public Docket docket() {
        ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2)
                .groupName("所有接口")
                .apiInfo(new ApiInfo("", "", "", "", new Contact("", "", ""), "", "", new ArrayList<VendorExtension>()))
                .genericModelSubstitutes(ResponseEntity.class)
                // .paths(PathSelectors.ant("/jim-framework/**"))
                .select();


        return builder.build();

    }
//
//    @Bean
//    public Docket docket(final EndpointHandlerMapping actuatorEndpointHandlerMapping) {
//        ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2)
//                .groupName("所有接口")
//                .apiInfo(new ApiInfo("", "", "", "", new Contact("", "", ""), "", "", new ArrayList<VendorExtension>()))
//                .genericModelSubstitutes(ResponseEntity.class)
//                // .paths(PathSelectors.ant("/jim-framework/**"))
//                .select();
//
//        // Ignore the spring-boot-actuator endpoints:
//        Set<MvcEndpoint> endpoints = actuatorEndpointHandlerMapping.getEndpoints();
//        endpoints.forEach(endpoint -> {
//            String path = endpoint.getPath();
//            logger.debug("excluded path for swagger {}", path);
//            builder.paths(Predicates.not(PathSelectors.regex(path + ".*")));
//        });
//        return builder.build();
//
//    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void setEnvironment(Environment environment) {

    }
}
