package com.jim.framework.session.configuration;

import com.jim.framework.common.util.RegExps;
import com.jim.framework.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * cookie session方式的sso
 * Created by celiang.hu on 2018-11-19.
 */
@Configuration
public class CrossContextPathCookieConfiguration {

    @Value("${jim.framework.session.sso.enabled:true}")
    private boolean enableSSO;
    @Value("${jim.framework.session.cross-domain.enabled:true}")
    private boolean enableCrossDoamin;
    @Value("${jim.framework.session.parse-request.enabled:true}")
    private boolean enableParseRequest;

    @Value("${jim.framework.session.request-param.name:__ticket__}")
    private String requestParamName;

    @Value("${jim.framework.session.parse-header.enabled:true}")
    private boolean enableParseHeader;

    @Value("${jim.framework.session.header-token.name:sweet-token}")
    private String headerTokenName;

    @Value("${jim.framework.session.cookie-session-disabled:false}")
    private boolean cookieSessionDisabled;

    @Bean
    CookieSerializer cookieSerializer() {
        return new CrossContextPathCookie(enableSSO,
                enableCrossDoamin,
                enableParseRequest,
                requestParamName,
                enableParseHeader,
                headerTokenName,
                cookieSessionDisabled);
    }

    public class CrossContextPathCookie extends DefaultCookieSerializer {

        private final Boolean enableParseRequest;
        private final String requestParamName;
        private final Boolean enableParseHeader;
        private final String headerTokenName;
        private final Boolean cookieSessionDisabled;

        public CrossContextPathCookie(boolean enableSSO, boolean enableCrossDoamin, Boolean enableParseRequest, String requestParamName, Boolean enableParseHeader, String headerTokenName, Boolean cookieSessionDisabled) {
            this.enableParseRequest = enableParseRequest;
            this.requestParamName = requestParamName;
            this.enableParseHeader = enableParseHeader;
            this.headerTokenName = headerTokenName;
            this.cookieSessionDisabled = cookieSessionDisabled;

            if (enableSSO) {
                this.setDomainNamePattern(RegExps.TOP_DOMAIN_PATTERN_STRING);
            }
            // 是否允许同一Web服务器各应用 ookie跨域共享
            if (enableCrossDoamin) {
                this.setCookiePath("/");
            }
        }

        /**
         * 优先使用自定义的方式获取SessionId Request->Header->Cookie
         * @param request
         * @return
         */
        @Override
        public List<String> readCookieValues(HttpServletRequest request) {
            List<String> ret;

            if (!cookieSessionDisabled) {
                ret = super.readCookieValues(request);
            }else {
                ret = new ArrayList<>();
            }

            String sessionId = null;
            if (enableParseRequest) {
                sessionId = request.getParameter(requestParamName);
            }
            if(!StringUtils.hasText(sessionId)&&this.enableParseHeader){
                sessionId = request.getHeader(headerTokenName);
            }
            if(StringUtils.hasText(sessionId)){
                ret.clear();
                ret.add(sessionId);
            }
            return ret;
        }

        @Override
        public void writeCookieValue(CookieValue cookieValue) {
            if(!cookieSessionDisabled) {
                super.writeCookieValue(cookieValue);
            }
        }
    }

}
