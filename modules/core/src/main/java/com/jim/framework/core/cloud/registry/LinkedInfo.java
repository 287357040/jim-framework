package com.jim.framework.core.cloud.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 在票据认证成功后，注册中心返回的链接信息
 * Created by celiang.hu on 2018-11-23.
 */
public class LinkedInfo implements Serializable {
    public static final Logger logger = LoggerFactory.getLogger(LinkedInfo.class);

    private RegistryInfo registryInfo;
    private String gatewayUrl;
    private String projectId;
    private ApolloInfo apolloInfo;

    public RegistryInfo getRegistryInfo() {
        return registryInfo;
    }

    public void setRegistryInfo(RegistryInfo registryInfo) {
        this.registryInfo = registryInfo;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public ApolloInfo getApolloInfo() {
        return apolloInfo;
    }

    public void setApolloInfo(ApolloInfo apolloInfo) {
        this.apolloInfo = apolloInfo;
    }

    public static class RegistryInfo implements Serializable {
        // Dubbo的zookeeper,redis
        private String RegistryType;

        private String connectionString;

        private String authString;

        public String getRegistryType() {
            return RegistryType;
        }

        public void setRegistryType(String registryType) {
            RegistryType = registryType;
        }

        public String getConnectionString() {
            return connectionString;
        }

        public void setConnectionString(String connectionString) {
            this.connectionString = connectionString;
        }

        public String getAuthString() {
            return authString;
        }

        public void setAuthString(String authString) {
            this.authString = authString;
        }
    }

    /**
     * 配置中心的信息
     */
    public static class ApolloInfo implements Serializable {
        // apollo配置中心信息，先不考虑扩展，干了再说
        /**
         * apollo当前运行环境
         */
        private String env;
        /**
         * Apollo连接地址
         */
        private String envMeta;
        /**
         * 应用的命名空间
         */
        private String namespace;
        /**
         * 集群模式
         */
        private String clusterMode;

        private boolean specific;

        public String getEnv() {
            return env;
        }

        public void setEnv(String env) {
            this.env = env;
        }

        public String getEnvMeta() {
            return envMeta;
        }

        public void setEnvMeta(String envMeta) {
            this.envMeta = envMeta;
        }

        public String getNamespace() {
            return namespace;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public String getClusterMode() {
            return clusterMode;
        }

        public void setClusterMode(String clusterMode) {
            this.clusterMode = clusterMode;
        }

        public boolean isSpecific() {
            return specific;
        }

        public void setSpecific(boolean specific) {
            this.specific = specific;
        }

        @Override
        public String toString() {
            return "ApolloInfo{" +
                    "env='" + env + '\'' +
                    ", envMeta='" + envMeta + '\'' +
                    ", namespace='" + namespace + '\'' +
                    ", clusterMode='" + clusterMode + '\'' +
                    ", specific=" + specific +
                    '}';
        }
    }
}
