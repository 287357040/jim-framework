package com.jim.framework.core.mvc.controller;


import com.jim.framework.common.mvc.JimResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//该Controller作为确认服务状态的回调接口
@RestController
@RequestMapping("/jim-framework")
@Api(value = "框架日志配置接口", tags = {"框架接口"})
public class JimFrameworkTouchController {
    private static final Logger logger = LoggerFactory.getLogger(JimFrameworkTouchController.class);

    @RequestMapping(value = "/touch", method = {RequestMethod.GET, RequestMethod.HEAD})
    @ApiOperation("应用活动检测")
    public JimResponse<Boolean> touch() {
        return JimResponse.success(true);

    }
}
