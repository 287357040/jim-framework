package com.jim.framework.core.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by celiang.hu on 2018-11-09.
 */
@Configuration
public class DataSourceConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);
    private static final String PREFIX = "spring.datasource";

    @Value("${jim.framework.core.mybatis.page-helper.offsetAsPageNum:true}")
    private boolean offsetAsPageNum;
    @Value("${jim.framework.core.mybatis.page-helper.rowBoundsWithCount:true}")
    private boolean rowBoundsWithCount;
    @Value("${jim.framework.core.mybatis.page-helper.reasonable:true}")
    private String reasonable;
    @Value("${jim.framework.core.mybatis.page-helper.pageSizeZero:false")
    private String pageSizeZero;
    @Value("${jim.framework.core.mybatis.page-helper.dialect:}")
    private String dialect;


    /**
     * 由于spring dubbo默认配置了actuator，导致对datasorce默认做了监控
     * 导致启动连接数据库报错
     * @return
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = DataSourceConfiguration.PREFIX)
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", String.valueOf(offsetAsPageNum));
        p.setProperty("rowBoundsWithCount", String.valueOf(rowBoundsWithCount));
        p.setProperty("reasonable", String.valueOf(reasonable));
        p.setProperty("pageSizeZero", String.valueOf(pageSizeZero));
        if(StringUtils.hasText(dialect)) {
            p.setProperty("dialect", dialect);
        }
        pageHelper.setProperties(p);
        return pageHelper;
    }
}
