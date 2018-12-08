package com.jim.framework.core.mvc.controller;

import com.jim.framework.common.mvc.ErrorTable;
import com.jim.framework.common.mvc.JimResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by celiang.hu on 2018-11-09.
 */
@RestController
@RequestMapping(value = "/jim-framework")
@Api(value = "框架错误码查询接口", tags = {"框架接口"})
public class JimFrameworkErrorTableController {
    private Logger logger = LoggerFactory.getLogger(JimFrameworkErrorTableController.class);

    @RequestMapping(value="/errors/json",method = RequestMethod.GET)
    @ApiOperation("显示应用的错误码")
    @ApiIgnore
    public JimResponse<SortedMap<String, String>> getErrors() {
        Properties errorProps = ErrorTable.getErrorTable();
        SortedMap<String, String> sortProps = new TreeMap<>();
        Set<String> keys = errorProps.stringPropertyNames();
        for (String key : keys) {
            sortProps.put(key, errorProps.getProperty(key));
        }
        return JimResponse.success(sortProps);
    }
}
