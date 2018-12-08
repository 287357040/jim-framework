package com.jim.framework.common.mvc;

import com.jim.framework.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 *
 */
public class ErrorTable {
    private static final Logger logger = LoggerFactory.getLogger(ErrorTable.class);

    private static Locale DEFAULT_LOCAL = Locale.SIMPLIFIED_CHINESE;
    private static Properties DEFAULT_ERROR_MESSAGES = new Properties();
    private final static PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private static String i18nNames;
    private static volatile boolean initialized = false;
    private static final Map<Locale, Properties> localeMessagesMap = new HashMap<>();

    static {
        DEFAULT_ERROR_MESSAGES.setProperty(JimResponse.SUCCESS, "API调用成功");
        DEFAULT_ERROR_MESSAGES.setProperty(JimResponse.FAIL, "API调用失败");
        DEFAULT_ERROR_MESSAGES.setProperty(JimResponse.UNAUTHORIZED_SERVICE_INVOKER, "拒绝访问, 未授权的服务调用者");
        DEFAULT_ERROR_MESSAGES.setProperty(JimResponse.VALIDATION_FAIL, "请求参数验证失败");
        DEFAULT_ERROR_MESSAGES.setProperty(JimResponse.BAD_PARAMETER, "拒绝访问, 请求参数错误");
        DEFAULT_ERROR_MESSAGES.setProperty(JimResponse.UNAUTHORIZED, "拒绝访问, 您没有权限请求该资源");
        DEFAULT_ERROR_MESSAGES.setProperty(JimResponse.NOT_INITIALIZED, "返回值未初始化");
        DEFAULT_ERROR_MESSAGES.setProperty(JimResponse.USER_NOT_LOGIN, "用户未登录");
        DEFAULT_ERROR_MESSAGES.setProperty(JimResponse.RPC_FAIL, "远程调用失败【{0}】");

        DEFAULT_ERROR_MESSAGES.setProperty(JimResponse.SIGN_INVALID, "签名验证未通过");
        DEFAULT_ERROR_MESSAGES.setProperty(JimResponse.SIGN_AUTHORITY_INVALID, "签名访问权限不足");
    }

    // 从默认的配置Locale加载语言资源，也支持
    public static void init(String i18nNames, String defaultLocale) throws IOException {
        ErrorTable.i18nNames = i18nNames;
        if (StringUtils.hasText(defaultLocale)) {
            DEFAULT_LOCAL = new Locale(defaultLocale);
        }
        initialized = true;
        init(DEFAULT_LOCAL);
    }

    private static void init(Locale locale) throws IOException {
        if (!initialized) {
            return;
        }
        Properties localeMessages = new Properties();
        localeMessagesMap.put(locale, localeMessages);
        String[] resourceNames = i18nNames.split(",");
        for (String resourceName : resourceNames) {
            Resource[] resources = resourcePatternResolver.getResources(resourceName + "*_" + String.valueOf(locale) + ".properties");
            Properties properties = new Properties();
            for (Resource resource : resources) {
                String resourcePath;
                URL path = resource.getURL();
                if (path == null) {
                    resourcePath = resource.getFilename();
                } else {
                    resourcePath = path.getPath();
                }
                InputStreamReader reader = null;
                try {
                    reader = new InputStreamReader(resource.getInputStream(), "utf-8");
                    properties.load(reader);
                    Set<String> keys = properties.stringPropertyNames();
                    for (String key : keys) {
                        if (localeMessages.containsKey(key)) {
                            logger.warn("duplicated response message key => " + key);
                        }
                        localeMessages.setProperty(key, properties.getProperty(key));
                    }
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e) {
                            ; // handled
                        }
                    }
                }
            }
        }
    }

    public static Properties getErrorTable() {
        Locale locale = LocaleContextHolder.getLocale();
        Properties defaultLocaleMessages = localeMessagesMap.get(locale);

        if (defaultLocaleMessages == null) {
            defaultLocaleMessages = new Properties();
        }
        Properties result = (Properties) defaultLocaleMessages.clone();

        // 没必要再提取一个方法出来
        for (String key : DEFAULT_ERROR_MESSAGES.stringPropertyNames()) {
            if (!result.containsKey(key)) {
                result.put(key, DEFAULT_ERROR_MESSAGES.getProperty(key));
            }
        }
        return result;
    }

    ///
    // 1、先从默认的错误码查找
    // 2、再从项目的约定的资源文件加载查找
    public static String convertCode2LocaleMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return convertCode2LocaleMessage(code, locale);
    }

    public static String convertCode2LocaleMessage(String code, Locale locale) {
        Properties localeMessage = localeMessagesMap.get(locale);
        if (localeMessage == null) {
            try {
                init(locale);
                localeMessage = localeMessagesMap.get(locale);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return convertCode2LocaleMessage(localeMessage, code);
    }

    private static String convertCode2LocaleMessage(Properties localeMessage, String code) {
        // 先从当前的localeMessage查找
        String message = localeMessage == null ? null : localeMessage.getProperty(code);
        if (message == null) {
            message = DEFAULT_ERROR_MESSAGES.getProperty(code);
        }
        if (!StringUtils.hasText(message)) {
            message = code;
        }
        return message;
    }

}
