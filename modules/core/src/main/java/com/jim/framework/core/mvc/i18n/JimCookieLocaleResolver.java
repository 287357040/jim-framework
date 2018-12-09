package com.jim.framework.core.mvc.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * 从用户浏览器中的Cookie中获取当前语言区域,如果cookie不存在 则使用默认的http header中获取
 * Created by celiang.hu on 2018-11-09.
 */
public class JimCookieLocaleResolver extends CookieLocaleResolver {
    Logger logger = LoggerFactory.getLogger(SessionLocaleResolver.class);

    @Override
    protected Locale determineDefaultLocale(HttpServletRequest request) {
        return getDefaultLocale();
    }
}
