package com.jim.framework.core.mvc.filter.security;

import com.jim.framework.common.util.StringUtils;
import com.jim.framework.core.mvc.filter.FilterAdapter;
import org.springframework.http.HttpMethod;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by celiang.hu on 2018-11-11.
 */
public class CSRFDefenceFilter extends FilterAdapter {

    private static final String SESSION_CSRF_KEY = "__csrf__";
    private static final String HEADER_CSRF_TOKEN = "X-CSRF-TOKEN";
    private static final String BODY_CSRF_PARAMETER = "_csrf";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 如果没有会话则创建
        HttpSession session = request.getSession(true);
        // 对与GET/HEAD/OPTIONS请求不做处理
        String method = request.getMethod();
        if (HttpMethod.GET.matches(method) || HttpMethod.HEAD.matches(method) || HttpMethod.OPTIONS.matches(method)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // 先校验会话中所带的token是否合法
        String csrfToken = (String) session.getAttribute(SESSION_CSRF_KEY);
        if (csrfToken == null || csrfToken.equals(getTokenFromRequest(request))) {
            updateToken(session, request, response);
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        //输出403
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getOutputStream().write(StringUtils.getBytes("Invalid csrf token"));
    }

    private void updateToken(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        // 将请求request和session的attribute增加csrf token，在同一容器内仍然验证有效
        request.setAttribute(BODY_CSRF_PARAMETER, token);
        session.setAttribute(SESSION_CSRF_KEY, token);
        response.setHeader(HEADER_CSRF_TOKEN, token);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String requestToken = request.getParameter(BODY_CSRF_PARAMETER) == null ? request.getHeader(HEADER_CSRF_TOKEN) : "";
        return requestToken;
    }
}
