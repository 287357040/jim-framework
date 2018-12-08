package com.jim.framework.core;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.jim.framework.common.util.IPAddressUtils;
import com.jim.framework.core.cloud.application.JimApplication;
import com.jim.framework.core.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.util.Map;

/**
 * 主程序入口
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@DubboComponentScan({"com.jim.framework",
        "${" + AbstractArgsLoader.LAUNCH_ARG_CODE_PACKAGES + ":}" })
@ComponentScan({"com.jim.framework",
        "${" + AbstractArgsLoader.LAUNCH_ARG_CODE_PACKAGES + ":}" })
public class JimApplicationEntry {
    private static final Logger logger = LoggerFactory.getLogger(JimApplicationEntry.class);

    private static ConfigurableApplicationContext applicationContext = null;
    /**
     * 服务启动入口
     * 涉及主要的逻辑
     *
     * @param args
     */
    public static void main(String[] args) {
        JimApplication application = initializeApplication(args);
        RuntimeContext.getInstance().setJimApplication(application);
        SpringApplication springApplication = new SpringApplication(JimApplicationEntry.class);
        applicationContext = springApplication.run(args);
        logger.info("**Jim**应用" + application.toString() + "启动成功");

        Map<String, JimApplicationStartupListener> startupListeners = applicationContext
                .getBeansOfType(JimApplicationStartupListener.class);
        if (startupListeners != null) {
            for (JimApplicationStartupListener jsl : startupListeners.values()) {
                jsl.onLaunched(applicationContext);
            }
        }
    }

    private static JimApplication initializeApplication(String[] args) {
        JimApplication application = null;
        try {
            // 初始化信息，尽量全；方便故障问题排查
            application = new JimApplication.Builder().
                    setAppHost(IPAddressUtils.getLocalIP()).
                    setLaunchTime(System.currentTimeMillis()).
                    setFrameworkVersion(JimUtils.getJimFrameworkCoreVersion()).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        application.jvmArgs = args;
        LaunchArgsLoaderChain loaderChain = new LaunchArgsLoaderChain();
        loaderChain.add(new CommandArgsLoader(application));
        loaderChain.add(new LocalFileArgsLoader(application));
        loaderChain.add(new ModuleArgsLoader(application));
        loaderChain.add(new ApolloArgsLoader(application));
        try {
            loaderChain.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JimUtils.printGloablProperties();
        return application;
    }
}
