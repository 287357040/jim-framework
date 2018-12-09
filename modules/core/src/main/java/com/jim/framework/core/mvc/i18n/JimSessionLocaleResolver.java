package com.jim.framework.core.mvc.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by celiang.hu on 2018-11-09.
 */
public class JimSessionLocaleResolver extends SessionLocaleResolver{
    Logger logger = LoggerFactory.getLogger(SessionLocaleResolver.class);

    @Override
    protected Locale determineDefaultLocale(HttpServletRequest request) {
        return getDefaultLocale();
    }
}
