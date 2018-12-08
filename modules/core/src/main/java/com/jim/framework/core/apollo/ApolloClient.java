package com.jim.framework.core.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.jim.framework.core.spring.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by celiang.hu on 2018-11-22.
 * Apollo的客户端封装
 */
public class ApolloClient {
    public static final Logger logger = LoggerFactory.getLogger(ApolloClient.class);
    private static final String APOLLO_BOOTSTRAP_NAMESPACES = "apollo.bootstrap.namespaces";
    private ApolloClientConfig apolloClientConfig;
    private static final ApolloClient instance = new ApolloClient();
    private boolean isInitiated = false;

    private ApolloClient() {
    }

    public static ApolloClient getInstance() {
        return instance;
    }

    public synchronized void setApolloClientConfig(ApolloClientConfig config) {
        if (isInitiated)
            return;

        this.apolloClientConfig = config;
        logger.info("开始加载远程配置," + config.toString());
        //set apollo properties
        System.setProperty("apollo.bootstrap.enabled", "true");
        System.setProperty("env", config.getEnv());
        System.setProperty("app.id", config.getAppName());
        System.setProperty(config.getEnv() + "_meta", config.getEnvMeta());
        if (StringUtils.hasText(config.getCluster())) {
            System.setProperty("apollo.cluster", config.getCluster());
        }
    }

    public boolean isInitiated() {
        return isInitiated;
    }

    public Properties getProperties() {
        Properties properties = new Properties();
        Config config = this.getConfig();
        if (config != null) {
            Set<String> propertyNames = config.getPropertyNames();
            if (propertyNames != null) {
                for (String propertyName : propertyNames) {
                    properties.setProperty(propertyName, config.getProperty(propertyName, null));
                }
            }
        }
        return properties;
    }

    private Config getConfig() {
        Config apolloConfig;
        if (StringUtils.hasText(apolloClientConfig.getNamespace())) {
            System.setProperty(APOLLO_BOOTSTRAP_NAMESPACES, apolloClientConfig.getNamespace());
            apolloConfig = ConfigService.getConfig(apolloClientConfig.getNamespace());
        } else {
            apolloConfig = ConfigService.getAppConfig();
        }
        if (apolloConfig != null) {
            apolloConfig.addChangeListener(new ConfigChangeListener() {
                @Override
                public void onChange(ConfigChangeEvent changeEvent) {
                    Map<String, JimAoplloConfigChangeListener> listeners = SpringContextUtils.getApplicationContext()
                            .getBeansOfType(JimAoplloConfigChangeListener.class);
                    if (listeners != null) {
                        //notify listeners
                        for (String key : changeEvent.changedKeys()) {
                            com.ctrip.framework.apollo.model.ConfigChange change = changeEvent.getChange(key);
                            for (JimAoplloConfigChangeListener l : listeners.values()) {
                                ConfigChange configChange = new ConfigChange(change.getNamespace(), change.getPropertyName(),
                                        change.getOldValue(), change.getNewValue(), ConfigChange.ChangeType.valueOf(change.getChangeType().name()));
                                l.onChange(configChange);
                            }
                        }
                    }
                }
            });
        }
        return apolloConfig;
    }

}
