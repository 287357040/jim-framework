package com.jim.framework.core.config;

import com.jim.framework.common.util.StringUtils;
import com.jim.framework.core.cloud.application.JimApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 包扫描路径配置的定义
 * Created by celiang.hu on 2018-11-22.
 */
public class ModuleArgsLoader extends AbstractArgsLoader {
    private static final Logger logger = LoggerFactory.getLogger(ModuleArgsLoader.class);

    public static final String MODULE_DEFINITION_RESOURCES = "classpath*:META-INF/jim-module*.properties";

    private static final String CONFIG_ITEM_DAO_PACKAGE = "jim.module.dao.basePackages";
    private static final String CONFIG_ITEM_CODE_PACKAGE = "jim.module.code.basePackages";
    private List<String> daoPackages;

    public ModuleArgsLoader(JimApplication application) {
        super(application);
        this.daoPackages = new ArrayList<>();
    }


    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public Properties doLoad() throws IOException {
        logger.info("开始加载模块配置...");
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources(MODULE_DEFINITION_RESOURCES);
        StringBuilder sbCodePackage = new StringBuilder();
        StringBuilder sbDaoPackage = new StringBuilder();

        for (Resource resource : resources) {
            String resourcePath;
            URL path = resource.getURL();
            if (path == null) {
                resourcePath = resource.getFilename();
            } else {
                resourcePath = path.getPath();
            }
            logger.info("正在加载模块配置:" + resourcePath);
            Properties properties = new Properties();
            InputStream inputStream = null;
            inputStream = resource.getInputStream();
            if (inputStream != null) {
                properties.load(inputStream);
            }
            String codeProperty = properties.getProperty(CONFIG_ITEM_CODE_PACKAGE);
            String daoProperty = properties.getProperty(CONFIG_ITEM_DAO_PACKAGE);

            if (StringUtils.hasText(codeProperty)) {
                sbCodePackage.append(codeProperty).append(",");
            }
            if (StringUtils.hasText(daoProperty)) {
                sbDaoPackage.append(daoProperty).append(",");
            }
        }

        Properties result = new Properties();
        result.setProperty(LAUNCH_ARG_DAO_PACKAGES, StringUtils.trimTrailingCharacter(sbDaoPackage.toString(), ','));
        result.setProperty(LAUNCH_ARG_CODE_PACKAGES, StringUtils.trimTrailingCharacter(sbCodePackage.toString(), ','));
        return result;
    }
}
