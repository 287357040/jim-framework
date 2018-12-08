package com.jim.framework.sample.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.jim.framework.common.util.IPAddressUtils;
import com.jim.framework.sample.api.DemoService;
import com.jim.framework.sample.provider.service.RestDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by celiang.hu on 2018-12-06.
 */

@Service(interfaceClass = RestDemoService.class)
public class RestDemoServiceImpl implements RestDemoService {
    private static final Logger logger = LoggerFactory.getLogger(RestDemoServiceImpl.class);

    @Override
    public String printHost() throws Exception {
        String ip = IPAddressUtils.getLocalIP();
        logger.info("host is:" + ip);
        return ip;
    }
}
