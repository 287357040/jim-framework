package com.jim.framework.core.config;

import com.jim.framework.common.util.StringUtils;
import com.jim.framework.core.cloud.application.JimApplication;
import com.jim.framework.core.cloud.registry.LinkedInfo;
import com.jim.framework.core.cloud.registry.LinkedManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;


import java.io.InputStream;
import java.util.Properties;

/**
 * 从本地的配置文件加载启动参数
 * 不打算再让用户可以自定义配置，就是用springboot的标准classpath
 * Created by celiang.hu on 2018-11-22.
 */
public class LocalFileArgsLoader extends AbstractArgsLoader {
    private static Logger logger = LoggerFactory.getLogger(LocalFileArgsLoader.class);

    private String filePath;

    public LocalFileArgsLoader(JimApplication application) {
        super(application);
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public Properties doLoad() {
        logger.info("开始加载本地配置...");
        Properties properties = new Properties();
        ClassPathResource classPathResource = new ClassPathResource(CLASSPATH_CONFIG_RESOURCE_NAME);
        InputStream is = null;

        try {
            is = classPathResource.getInputStream();
            properties.load(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    logger.warn("close local configuration file stream failed!");
                }
            }
        }
        return properties;
    }

    @Override
    protected void afterLoad(Properties properties) throws Exception {
        overwriteApplicationProperties(properties);
        Assert.hasText(this.application.getAppId(), "srping application Id can not be empty");
        Assert.hasText(this.application.getAppName(), "srping application name can not be empty");
        buildRegistry(properties);
    }

    /**
     * 通过本地配置文件，在注册中心校验合法性
     *
     * @param properties
     */
    private void buildRegistry(Properties properties) throws Exception {
        // 默认不接入用户中心
        boolean isCloudEnabled = Boolean.parseBoolean(properties.getProperty(JIM_CLOUD_ENABLED, "false"));
        application.setCloudEnabled(isCloudEnabled);

        if (isCloudEnabled) {
            String registryUrl = properties.getProperty(JIM_CLOUD_REGISTRY_ADDRESS, "http://example.jim.com");
            String ticket = properties.getProperty(JIM_CLOUD_REGISTRY_TICKET, "");
            Assert.hasText(ticket, "ticket can not be empty!");

            logger.info("***NOTE*** :开始接入到注册中心...");
            LinkedManager linkedManager = new LinkedManager(registryUrl,this.application);
            LinkedInfo linkedInfo = linkedManager.obtainLinkedInfo(ticket);
            Assert.notNull(linkedInfo,"link info can not be null.");
            this.application.setLinkedInfo(linkedInfo);
            linkedManager.destroy();

        } else {
            logger.info("***NOTE*** :以本地服务模式启动...");
        }
    }
}
