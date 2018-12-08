package com.jim.framework.core.config;

import com.jim.framework.common.util.StringUtils;
import com.jim.framework.core.cloud.application.JimApplication;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 抽出一个抽象类，把过程中的一些关键配置统一展现，看起来清晰
 * Created by celiang.hu on 2018-11-23.
 */
public abstract class AbstractArgsLoader implements LaunchArgsLoader {
    // Springboot默认的启动配置文件
    protected static final String CLASSPATH_CONFIG_RESOURCE_NAME = "application.properties";

    // 加载本地配置时，接入平台需要的信息
    protected static final String JIM_CLOUD_REGISTRY_ADDRESS = "jim.cloud.registry.address";
    protected static final String JIM_CLOUD_ENABLED = "jim.cloud.enabled";
    protected static final String JIM_CLOUD_REGISTRY_TICKET = "jim.cloud.registry.ticket";

    // 固定配置，启动时第一次配置，之后不会再被覆盖
    protected static final String SPRING_APPLICATION_INDEX = "spring.application.index";
    protected static final String SPRING_APPLICATION_NAME = "spring.application.name";
    protected static final String SERVER_PORT = "server.port";
    protected static final String SERVER_CONTEXT_PATH = "server.context-path";

    // 应用模块的配置加载
    public static final String LAUNCH_ARG_DAO_PACKAGES = "jim.module.daoPackages";
    public static final String LAUNCH_ARG_CODE_PACKAGES = "jim.module.codePackages";

    protected JimApplication application;


    public AbstractArgsLoader(JimApplication application) {
        this.application = application;
    }

    @Override
    public void load() throws Exception {
        preLoad();
        Properties properties = this.doLoad();
        afterLoad(properties);
        overwriteApplicationProperties(properties);
    }

    protected abstract Properties doLoad() throws IOException;


    protected void preLoad() {

    }

    protected void afterLoad(Properties properties) throws Exception {
        overwriteApplicationProperties(properties);
    }

    protected void overwriteApplicationProperties(Properties properties) {
        if (properties == null)
            return;

        if (this.application.getGlobalProperties() == null) {
            this.application.setGlobalProperties(properties);
        } else {
            this.application.getGlobalProperties().putAll(properties);
        }

        if (!StringUtils.hasText(this.application.getAppId())) {
            this.application.setAppId(properties.getProperty(SPRING_APPLICATION_INDEX, System.getProperty(SPRING_APPLICATION_INDEX, "")));
            System.setProperty(SPRING_APPLICATION_INDEX, this.application.getAppId());
        }
        if (!StringUtils.hasText(this.application.getAppContext())) {
            this.application.setAppContext(properties.getProperty(SERVER_CONTEXT_PATH, System.getProperty(SERVER_CONTEXT_PATH, "/")));
        }

        if (!StringUtils.hasText(this.application.getAppName())) {
            this.application.setAppName(properties.getProperty(SPRING_APPLICATION_NAME, System.getProperty(SPRING_APPLICATION_NAME, "")));
            System.setProperty(SPRING_APPLICATION_NAME, this.application.getAppName());
        }

        if (!StringUtils.hasText(this.application.getAppPort())) {
            this.application.setAppPort(properties.getProperty(SERVER_PORT, System.getProperty(SERVER_PORT, "")));
        }

        String daoPackages = System.getProperty(LAUNCH_ARG_DAO_PACKAGES);
        if (!StringUtils.hasText(daoPackages)) {
            System.setProperty(LAUNCH_ARG_DAO_PACKAGES, properties.getProperty(LAUNCH_ARG_DAO_PACKAGES,""));
        }

        String codePackages = System.getProperty(LAUNCH_ARG_CODE_PACKAGES);
        if (!StringUtils.hasText(codePackages)) {
            System.setProperty(LAUNCH_ARG_CODE_PACKAGES,  properties.getProperty(LAUNCH_ARG_CODE_PACKAGES,""));
        }
    }


    protected void setGlobalProperty(String key, String value) {
        this.application.getGlobalProperties().setProperty(key, value);
    }

    protected String getGlobalProperty(String key) {
        return getGlobalProperty(key, "");
    }

    protected String getGlobalProperty(String key, String def) {
        return this.application.getGlobalProperties().getProperty(key, def);
    }

    protected void checkProperty(String property, String value, int maxlength, Pattern pattern) {
        if (value == null || value.length() == 0) {
            return;
        }
        if (value.length() > maxlength) {
            throw new IllegalStateException("Invalid " + property + "=\"" + value + "\" is longer than " + maxlength);
        }
        if (pattern != null) {
            Matcher matcher = pattern.matcher(value);
            if (!matcher.matches()) {
                throw new IllegalStateException("Invalid " + property + "=\"" + value + "\" contains illegal " +
                        "character, only digit, letter, '-', '_' or '.' is legal.");
            }
        }
    }
}
