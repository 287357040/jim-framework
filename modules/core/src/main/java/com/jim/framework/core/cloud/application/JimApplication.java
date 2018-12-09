package com.jim.framework.core.cloud.application;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.jim.framework.core.cloud.registry.LinkedInfo;

import java.util.Properties;

/**
 * 扩展Dubbo的应用信息，增加认证的信息
 * 设计考虑此对象只是作为信息数据和状态的载体，而不像sweet一样聚合了太多的其他对象
 * 如果有必要可以创建一个ApplicationContext来封装零散的功能
 * <p>
 * 不把初始化过程的Properties放到JimApplication，因为初始化完成后不再需要
 * Created by celiang.hu on 2018-11-21.
 */
public class JimApplication extends ApplicationConfig {
    private String appId;

    private String appName;

    public String[] jvmArgs;

    private String ticket;

    private boolean cloudEnabled;

    private LinkedInfo linkedInfo;

    private String frameworkVersion;
    private String host;
    private long launchTime;

    private Properties globalProperties;
    private String appPort;
    private String appContext;

    public JimApplication(String frameworkVersion,String host, long launchTime) {
        this.frameworkVersion = frameworkVersion;
        this.host = host;
        this.launchTime = launchTime;
    }

    public String[] getJvmArgs() {
        return jvmArgs;
    }

    public void setJvmArgs(String[] jvmArgs) {
        this.jvmArgs = jvmArgs;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public boolean isCloudEnabled() {
        return cloudEnabled;
    }

    public void setCloudEnabled(boolean cloudEnabled) {
        this.cloudEnabled = cloudEnabled;
    }

    public LinkedInfo getLinkedInfo() {
        return linkedInfo;
    }

    public void setLinkedInfo(LinkedInfo linkedInfo) {
        this.linkedInfo = linkedInfo;
    }

    public String getFrameworkVersion() {
        return frameworkVersion;
    }

    public void setFrameworkVersion(String frameworkVersion) {
        this.frameworkVersion = frameworkVersion;
    }

    public Properties getGlobalProperties() {
        return globalProperties;
    }

    public void setGlobalProperties(Properties globalProperties) {
        this.globalProperties = globalProperties;
    }

    public void setAppId(String appId){
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppName() {
        return super.getName();
    }

    public void setAppName(String appName) {
        super.setName(appName);
    }

    public String getAppPort() {
        return appPort;
    }

    public void setAppPort(String appPort) {
        this.appPort = appPort;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public long getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(long launchTime) {
        this.launchTime = launchTime;
    }

    public String getAppContext() {
        return appContext;
    }

    public void setAppContext(String appContext) {
        this.appContext = appContext;
    }


    public static class Builder {
        private String frameworkVersion;
        private String appHost;
        private long launchTime;

        public Builder setFrameworkVersion(String frameworkVersion) {
            this.frameworkVersion = frameworkVersion;
            return this;
        }
        public Builder setAppHost(String appHost) {
            this.appHost = appHost;
            return this;
        }

        public Builder setLaunchTime(long launchTime) {
            this.launchTime = launchTime;
            return this;
        }

        public JimApplication build() {
            return new JimApplication(this.frameworkVersion,this.appHost,this.launchTime);
        }
    }
}
