package com.jim.framework.core.configuration;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.jim.framework.core.RuntimeContext;
import com.jim.framework.core.apollo.ConfigChange;
import com.jim.framework.core.apollo.JimAoplloConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by celiang.hu on 2018-11-24.
 */
@Configuration
@EnableApolloConfig
@ConditionalOnExpression("'${jim.cloud.enabled:false}'=='true'")
public class ApolloConfiguration {
    public static final Logger logger = LoggerFactory.getLogger(JimAoplloConfigChangeListener.class);

    @Bean
    public JimAoplloConfigChangeListener apolloConfigChangeListener() {
        return new JimAoplloConfigChangeListener() {

            @Override
            public void onChange(ConfigChange configChange) {
                if (configChange.getChangeType() == ConfigChange.ChangeType.DELETED) {
                    RuntimeContext.getInstance().getJimApplication().
                            getGlobalProperties()
                            .remove(configChange.getPropertyName());
                } else {
                    RuntimeContext.getInstance().getJimApplication().
                            getGlobalProperties()
                            .setProperty(configChange.getPropertyName(), configChange.getNewValue());
                    logger.info("udpate configration," + configChange.toString());
                }
            }
        };
    }

}
