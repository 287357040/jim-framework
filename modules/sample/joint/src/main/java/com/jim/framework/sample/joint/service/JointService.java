package com.jim.framework.sample.joint.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jim.framework.common.mvc.JimResponse;
import com.jim.framework.core.cloud.api.LinkedService;
import com.jim.framework.core.cloud.registry.LinkedInfo;

/**
 * Created by celiang.hu on 2018-12-06.
 */
@Service(interfaceClass = LinkedService.class)
public class JointService implements LinkedService {
    @Override
    public JimResponse<Boolean> touch() {
        return null;
    }

    @Override
    public JimResponse<LinkedInfo> checkTicket(String ticket) {
        if (!ticket.equals("123455"))
            return JimResponse.fail("invalid ticket!");

        LinkedInfo linkedInfo = new LinkedInfo();
        linkedInfo.setProjectId("jim-dubbo-provider");

        LinkedInfo.ApolloInfo apolloInfo = new LinkedInfo.ApolloInfo();
        apolloInfo.setEnv("dev");
        apolloInfo.setClusterMode("default");
        apolloInfo.setEnvMeta("http://111.231.198.119:8070");
        linkedInfo.getApolloInfo();

        linkedInfo.setApolloInfo(apolloInfo);
        LinkedInfo.RegistryInfo registryInfo = new LinkedInfo.RegistryInfo();
        registryInfo.setConnectionString("zookeeper://111.231.198.119:2181");
        linkedInfo.setRegistryInfo(registryInfo);
        return JimResponse.success(linkedInfo);
    }
}
