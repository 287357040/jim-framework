package com.jim.framework.core.apollo;

public class ApolloClientConfig {
    private String env;
    private String appName;
    private String namespace;
    private String cluster;
    private String envMeta;

    public ApolloClientConfig(String env, String appName, String namespace, String cluster, String envMeta) {
        this.env = env;
        this.appName = appName;
        this.namespace = namespace;
        this.cluster = cluster;
        this.envMeta = envMeta;
    }

    public ApolloClientConfig() {

    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getEnvMeta() {
        return envMeta;
    }

    public void setEnvMeta(String envMeta) {
        this.envMeta = envMeta;
    }

    @Override
    public String toString() {
        return "ApolloClientConfig{" +
                "env='" + env + '\'' +
                ", appName='" + appName + '\'' +
                ", namespace='" + namespace + '\'' +
                ", cluster='" + cluster + '\'' +
                ", envMeta='" + envMeta + '\'' +
                '}';
    }
}
