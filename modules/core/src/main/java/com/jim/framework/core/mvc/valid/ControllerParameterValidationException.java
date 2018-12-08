package com.jim.framework.core.mvc.valid;

import java.util.Map;

/**
 * Created by celiang.hu on 2018-12-02.
 */
public class ControllerParameterValidationException extends RuntimeException {
    private Map<String, String> errorInfo;

    public ControllerParameterValidationException(Map<String, String> errorInfo) {
        super("Request parameter validation failed!");
        this.errorInfo = errorInfo;
    }

    /**
     * 获取所有的校验结果
     * @return
     */
    public Map<String, String> getErrorInfo() {
        return this.errorInfo;
    }
}
