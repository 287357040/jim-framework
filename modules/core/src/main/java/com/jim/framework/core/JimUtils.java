package com.jim.framework.core;

import com.jim.framework.core.dubbo.swagger.CodeGenerator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by celiang.hu on 2018-11-23.
 */
public class JimUtils {
    public static String getJimFrameworkCoreVersion() throws IOException {
        String version = "unknown";
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = pathMatchingResourcePatternResolver.getResources("classpath*:META-INF/maven/com.jim.framework/jim-framework-core/pom.properties");
        if (resources.length == 1) {
            Properties properties = new Properties();
            properties.load(resources[0].getInputStream());
            version = properties.getProperty("version");
        }
        return version;
    }

    public static void printGloablProperties() {
        if (RuntimeContext.getInstance().getJimApplication() != null) {
            SortedMap<String, String> sortedProperties = new TreeMap<>();
            Properties properties = RuntimeContext.getInstance().getJimApplication().getGlobalProperties();
            if (properties != null) {
                for (Object key : properties.keySet()) {
                    sortedProperties.put(key.toString(), properties.getProperty(key.toString()));
                }
            }
            StringBuffer sb = new StringBuffer("应用配置列表\n");
            for (Object key : sortedProperties.keySet()) {
                sb.append(key.toString()).append("=").append(properties.getProperty(key.toString())).append("\n");
            }
        }
    }


    public static String getCurrentClassPath() {
        // 获取路径，只针对标准的mavn工程
        String classPath = getProjectBasePath() + "/target/classes/";
        return classPath;
    }

    public static String getProjectBasePath() {
        // 获取路径，只针对标准的mavn工程
        String os = System.getProperty("os.name");

        String classPath = JimUtils.class.getResource("/").getPath().replace("/target/classes/", "");
        // 如果是windows
        if (os.toLowerCase().startsWith("win")) {
            classPath = classPath.replaceFirst("/", "");
        }
        return classPath;
    }


}
