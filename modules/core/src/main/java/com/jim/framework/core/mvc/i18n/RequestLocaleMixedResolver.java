package com.jim.framework.core.mvc.i18n;

import org.springframework.util.Assert;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 创建自定义的区域解析器,DispatchServlet只能注册一个区域解析器
 * <p>
 * Created by celiang.hu on 2018-11-09.
 */
public class RequestLocaleMixedResolver implements LocaleResolver {

    private List<LocaleResolver> loacleResolverChain = new ArrayList<>();
    private JimRequestParamLocaleResolver requestParamLocaleResolver;
    private JimCookieLocaleResolver cookieLocaleResolver;
    private JimSessionLocaleResolver sessionLocaleResolver;
    private JimAcceptHeaderLocaleResolver acceptHeaderLocaleResolver;
    private Locale defalutLocale;

    public RequestLocaleMixedResolver() {
        this(Locale.SIMPLIFIED_CHINESE);
    }

    public RequestLocaleMixedResolver(Locale defalutLocale) {
        Assert.notNull(defalutLocale,"default locale can not be null");
        this.defalutLocale = defalutLocale;
        initialize();
    }

    private void initialize() {
        requestParamLocaleResolver = new JimRequestParamLocaleResolver();
        requestParamLocaleResolver.setDefaultLocale(null);
        cookieLocaleResolver = new JimCookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(null);
        sessionLocaleResolver = new JimSessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(null);
        acceptHeaderLocaleResolver = new JimAcceptHeaderLocaleResolver();

        loacleResolverChain.add(requestParamLocaleResolver);
        loacleResolverChain.add(cookieLocaleResolver);
        loacleResolverChain.add(sessionLocaleResolver);
        loacleResolverChain.add(acceptHeaderLocaleResolver);
    }


    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        for(LocaleResolver resolver:loacleResolverChain){
           Locale locale = resolver.resolveLocale(httpServletRequest);
           if(locale!=null)
               return locale;
        }
        return defalutLocale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
        sessionLocaleResolver.setLocale(httpServletRequest, httpServletResponse, locale);
    }

    public void setCookieLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        cookieLocaleResolver.setLocale(request,response,locale);
    }
}
