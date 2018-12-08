package com.jim.framework.sample.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.jim.framework.common.util.IPAddressUtils;
import com.jim.framework.sample.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by celiang.hu on 2018-12-06.
 */
// @Service(interfaceClass = DemoService.class)
public class DemoServiceImpl implements DemoService {
    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Override
    public void printHost() throws Exception {
        String ip = IPAddressUtils.getLocalIP();
        logger.info("host is:" + ip);
    }
}
