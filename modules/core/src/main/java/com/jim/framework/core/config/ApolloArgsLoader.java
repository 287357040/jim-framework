package com.jim.framework.core.config;

import com.jim.framework.common.util.StringUtils;
import com.jim.framework.core.apollo.ApolloClient;
import com.jim.framework.core.apollo.ApolloClientConfig;
import com.jim.framework.core.cloud.application.JimApplication;
import com.jim.framework.core.cloud.registry.LinkedInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Created by celiang.hu on 2018-11-22.
 * 阿波罗远程配置中心的加载器
 */
public class ApolloArgsLoader extends RemoteArgsLoader {
    private ApolloClient apolloClient = ApolloClient.getInstance();
    public static final Logger logger = LoggerFactory.getLogger(ApolloArgsLoader.class);
    private Set<String> apolloEnvs = new HashSet<String>(Arrays.asList(new String[]{"dev", "fat", "uat", "pro"}));

    public ApolloArgsLoader(JimApplication jimApplication) {
        super(jimApplication);
    }


    @Override
    public int getOrder() {
        return 5;
    }

    @Override
    public Properties doLoad() throws IOException {
        logger.info("开始加载Apollo配置...");
        boolean isCloudEnabled = this.application.isCloudEnabled();
        if (!isCloudEnabled) {
            return null;
        }
        if (!this.apolloClient.isInitiated()) {
            LinkedInfo.ApolloInfo apolloInfo = this.application.getLinkedInfo().getApolloInfo();
            String env = apolloInfo.getEnv();
            if (!apolloEnvs.contains(env.toLowerCase())) {
                throw new IllegalArgumentException(" apollo env is invalid!");
            }

            String envMeta = apolloInfo.getEnvMeta();
            if (!StringUtils.hasText(envMeta)) {
                throw new IllegalArgumentException("apollo config.apollo." + env + "_meta" + " can not empty!");
            }
            logger.info("apollo env:{}", env);


            ApolloClientConfig clientConfig = new ApolloClientConfig();
            clientConfig.setAppName(application.getAppName());
            clientConfig.setCluster(apolloInfo.getClusterMode());
            clientConfig.setEnv(env);
            clientConfig.setEnvMeta(envMeta);

            if (apolloInfo.isSpecific()) {
                clientConfig.setNamespace(apolloInfo.getNamespace());
            }
            ApolloClient.getInstance().setApolloClientConfig(clientConfig);
        }

        return apolloClient.getProperties();
    }
}
