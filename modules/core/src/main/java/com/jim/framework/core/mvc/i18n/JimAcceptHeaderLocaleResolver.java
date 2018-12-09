package com.jim.framework.core.mvc.i18n;

import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by celiang.hu on 2018-11-09.
 */
public class JimAcceptHeaderLocaleResolver extends AcceptHeaderLocaleResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        return super.resolveLocale(request);
    }
}
