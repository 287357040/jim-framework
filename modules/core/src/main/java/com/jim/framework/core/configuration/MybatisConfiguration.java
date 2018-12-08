package com.jim.framework.core.configuration;

import com.jim.framework.common.util.StringUtils;
import com.jim.framework.core.config.ModuleArgsLoader;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

/**
 * 不太理解sweet要自定义dao的basepackage,和mapper annoation，觉得没必要
 * 简化处理
 * Created by celiang.hu on 2018-11-27.
 */
@Configuration
@Order(Integer.MAX_VALUE)
public class MybatisConfiguration implements EnvironmentAware {

    private String mybatisMapperScanBasePackage;

    @Override
    public void setEnvironment(Environment environment) {
        String modulesMapperScanBasePackage = environment.resolvePlaceholders("${" + ModuleArgsLoader.LAUNCH_ARG_DAO_PACKAGES + "}");

        if (StringUtils.hasText(modulesMapperScanBasePackage) && !"null".equalsIgnoreCase(modulesMapperScanBasePackage)) {
            this.mybatisMapperScanBasePackage = modulesMapperScanBasePackage;
        }
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        if (!StringUtils.hasText(mybatisMapperScanBasePackage)) {
            mybatisMapperScanBasePackage = "com.jim.framework.core";
        }
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        configurer.setBasePackage(mybatisMapperScanBasePackage);
        return configurer;
    }
}
