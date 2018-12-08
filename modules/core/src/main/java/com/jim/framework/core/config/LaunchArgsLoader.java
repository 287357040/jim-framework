package com.jim.framework.core.config;

import java.io.IOException;
import java.util.Properties;

/**
 * 启动参数的加载器接口
 * Created by celiang.
 */
public interface LaunchArgsLoader {
    /**
     * 设置配置加载的优先级，注意不是配置本身的优先级
     * 不要轻易调整这个值
     */
    int getOrder();

    /**
     * 加载配置
     *
     * @return
     */
    void load() throws Exception;
}
