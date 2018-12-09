package com.jim.framework.core.mvc.i18n;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Created by celiang.hu on 2018-11-09.
 */
public class JimRequestParamLocaleResolver extends AbstractLocaleResolver {

    public static final String LOACLE_PARAM_NAME = "__loacle__";
    private static final Logger logger = LoggerFactory.getLogger(JimRequestParamLocaleResolver.class);

    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        String parameter = httpServletRequest.getParameter(LOACLE_PARAM_NAME);
        if(!StringUtils.hasText(parameter)){
            return getDefaultLocale();
        }

        try {
            StringUtils.parseLocaleString(parameter);
        }catch (IllegalArgumentException e){
            logger.error(e.getMessage(), e);
        }

        return getDefaultLocale();
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
        throw new UnsupportedOperationException("Cannot change HTTP locale parameter - use a different locale resolution strategy");
    }
}
