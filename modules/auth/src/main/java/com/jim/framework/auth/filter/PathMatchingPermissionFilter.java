package com.jim.framework.auth.filter;

import com.jim.framework.auth.AuthUtils;
import com.jim.framework.auth.JimAuthorizingRealm;
import com.jim.framework.auth.UrlPermissionResolver;
import com.jim.framework.auth.UserAuthenticationService;
import com.jim.framework.common.mvc.JimResponse;
import com.jim.framework.common.util.serialization.JsonSerializer;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by celiang.hu on 2018-11-17.
 */
public class PathMatchingPermissionFilter extends OncePerRequestFilter {
    public static final Logger logger = LoggerFactory.getLogger(PathMatchingPermissionFilter.class);
    protected JsonSerializer jsonSerializer = new JsonSerializer();
    protected PatternMatcher pathMatcher = new AntPathMatcher();


    private UrlPermissionResolver urlPermissionResolver;
    private JimAuthorizingRealm jimAuthorizingRealm;
    private UserAuthenticationService userAuthenticationService;

    public PathMatchingPermissionFilter(UserAuthenticationService userAuthenticationService,
                                        JimAuthorizingRealm jimAuthorizingRealm,
                                        UrlPermissionResolver urlPermissionResolver) {
        this.userAuthenticationService = userAuthenticationService;
        this.jimAuthorizingRealm = jimAuthorizingRealm;
        this.urlPermissionResolver = urlPermissionResolver;
    }

    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (!isLoginRequest(request)) {
            // 如果没有登录
            if (AuthUtils.getSessionUser() == null) {
                //
                notLoginResponse(httpServletRequest, httpServletResponse);
                return;
            }

            if (!hasPermission(SecurityUtils.getSubject(), httpServletRequest)) {
                noPermissionResponse(httpServletRequest, httpServletResponse);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isLoginRequest(ServletRequest request) {
        return this.patchMatch(userAuthenticationService.getLoginUrl(), request);
    }

    private boolean patchMatch(String path, ServletRequest request) {
        String requestURI = this.getPathWithinApplication(request);
        return pathsMatch(path, requestURI);
    }

    private String getPathWithinApplication(ServletRequest request) {
        return WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
    }

    private boolean pathsMatch(String path, String requestURI) {
        return this.pathMatcher.matches(path, requestURI);
    }


    private boolean hasPermission(Subject subject, HttpServletRequest request) {

        PrincipalCollection principalCollection = subject.getPrincipals();
        // 获取用户权限以及角色对应的权限集合
        String url = request.getServletPath();
        String method = request.getMethod();
        boolean hasPermission = false;
        hasPermission = subject.isPermitted(url + ";" + method);

        return hasPermission;
    }

    private void notLoginResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        JimResponse jimResponse = new JimResponse(JimResponse.USER_NOT_LOGIN,userAuthenticationService.getLoginUrl());
        String output = jsonSerializer.serialize2JSON(jimResponse);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(output);
    }

    private void noPermissionResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        JimResponse apiResponse = new JimResponse(JimResponse.UNAUTHORIZED);
        String output = jsonSerializer.serialize2JSON(apiResponse);
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().println(output);
    }
}

