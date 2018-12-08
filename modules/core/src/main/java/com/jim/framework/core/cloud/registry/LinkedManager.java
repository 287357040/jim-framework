package com.jim.framework.core.cloud.registry;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.jim.framework.common.mvc.JimResponse;
import com.jim.framework.core.cloud.api.LinkedService;
import com.jim.framework.core.cloud.application.JimApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 注册管理器，
 * Created by celiang.hu on 2018-11-22.
 */
public class LinkedManager {
    private static final Logger logger = LoggerFactory.getLogger(LinkedManager.class);

    private static final String REGISTRY = "registry:";

    private LinkedService linkedService;
    private ReferenceConfig<LinkedService> reference;
    private String registryUrl;
    private final
    JimApplication application;
    private String ticket;

    public LinkedManager(String registryUrl, JimApplication application) {
        this.registryUrl = registryUrl;
        this.application = application;
        if (!registryUrl.startsWith(REGISTRY)) {
            throw new IllegalArgumentException("registry url format is not valid.");
        }
        String dubboUrl = registryUrl.replace(REGISTRY, "dubbo:");
        createRegistryService(dubboUrl);
    }


    private void createRegistryService(String dubboUrl) {
        // 引用远程服务
        reference = new ReferenceConfig<LinkedService>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
        reference.setUrl(dubboUrl);
        reference.setApplication(this.application);
        // 默认采用failover策略
        reference.setCluster("failover");
        reference.setRetries(2);
        reference.setInterface(LinkedService.class);
        this.linkedService = reference.get();
    }

    public String getRegistryUrl() {
        return registryUrl;
    }

    public void setRegistryUrl(String registryUrl) {
        this.registryUrl = registryUrl;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public void touchHosts() {
        //  TODO 这里有个问题，dubbo的调用如何获取调用的集群的哪个节点
        logger.info("尝试连接到云【{}】...");

        JimResponse response = this.linkedService.touch();
        if (response.isSuccess()) {
            logger.info("尝试连接到云【{}】成功");
        } else {
            logger.error("连接到云失败, 原因 -> {}", response.getMessage());
        }
    }

    public LinkedInfo obtainLinkedInfo(String ticket) throws Exception {
        // TODO 验证票据
        JimResponse<LinkedInfo> response = this.linkedService.checkTicket(ticket);
        if (!response.isSuccess()) {
            throw new Exception(response.getMessage());
        }
        return response.getData();
    }

    public void destroy(){
        this.reference.destroy();
    }
}
