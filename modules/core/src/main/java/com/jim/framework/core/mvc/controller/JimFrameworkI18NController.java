package com.jim.framework.core.mvc.controller;

import com.jim.framework.common.mvc.JimResponse;
import com.jim.framework.core.mvc.i18n.RequestLocaleMixedResolver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Locale;

/**
 * Created by celiang.hu on 2018-11-10.
 */
@RestController
@RequestMapping("/jim-framework/i18n")
@Api(value = "框架国际化接口", tags = {"框架接口"})
public class JimFrameworkI18NController {

    @Autowired
    private LocaleResolver localeResolver;

    @RequestMapping(value = "/locale", method = RequestMethod.POST)
    @ApiOperation("改变后端响应消息的默认语言")
    @Valid
    public JimResponse<Boolean> changeLocale(HttpServletRequest request, HttpServletResponse response,
                                             /**
                                              * Spring 的请求Mapping时, 设置@RequestParam为true时, Spring抛出异常在前,
                                              * 不会再走到ControllerParameterValidationAspect
                                              * */
                                             @RequestParam(required = false)
                                             @NotNull String localeString,
                                             @RequestParam(required = false, defaultValue = "false") Boolean cookie
    ) {
        Locale locale = StringUtils.parseLocaleString(localeString);
        LocaleContextHolder.setLocale(locale);
        if (!cookie) {
            localeResolver.setLocale(request, response, locale);
        } else {
            if (localeResolver instanceof RequestLocaleMixedResolver) {
                ((RequestLocaleMixedResolver) localeResolver).setCookieLocale(request, response, locale);
            } else {
                return JimResponse.success(false);
            }
        }
        return JimResponse.success(true);
    }
}
