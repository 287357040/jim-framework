package com.jim.framework.core.config;

import com.jim.framework.core.cloud.application.JimApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import java.util.Properties;

/**
 * 命令行参数的加载器，JVM的-D启动参数是在全局系统变量，通过main的args是获取不到的
 * Created by celiang.hu on 2018-11-22.
 */
public class CommandArgsLoader extends AbstractArgsLoader {
    public static final Logger logger = LoggerFactory.getLogger(CommandArgsLoader.class);

    public CommandArgsLoader(JimApplication application) {
        super(application);
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public Properties doLoad() {
        logger.info("开始加载JVM参数...");
        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(this.application.jvmArgs);
        String[] propertyNames = source.getPropertyNames();

        Properties properties = new Properties();
        for (String propertyName : propertyNames) {
            properties.setProperty(propertyName, source.getProperty(propertyName));
        }
        return properties;
    }
}
