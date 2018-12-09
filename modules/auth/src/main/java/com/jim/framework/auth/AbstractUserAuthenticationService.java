package com.jim.framework.auth;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationException;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by celiang.hu on 2018-11-16.
 */
public abstract class AbstractUserAuthenticationService<T> implements UserAuthenticationService<T> {
    @Override
    public String getLoginUrl() {
        return "";
    }

    @Override
    public Set<String> getRoles(T userId) {
        return null;
    }

    @Override
    public Set<String> getPermissions(T userId) {
        return null;
    }

    @Override
    public T doLogin(String username, String password) {
        return null;
    }

    @Override
    public T doLogin(AuthenticationToken token) {
        return null;
    }

    @Override
    public String[] getWhitelistResources() {
        return new String[0];
    }

    @Override
    public Map<String, Filter> getExtendFilters() {
        return null;
    }

    @Override
    public String getDefaultResourceFilterName() {
        return null;
    }

    @Override
    public LinkedHashMap<String, String> getPatternMatchedFilters() {
        return null;
    }

    public static class Default extends AbstractUserAuthenticationService {
        private Default() {
        }

        private static final Default instance = new Default();

        public static Default getInstance() {
            return instance;
        }

        @Override
        public String getLoginUrl() {
            return "/login";
        }

        @Override
        public Set<String> getRoles(Object userId) {
            throw new AuthorizationException("功能未实现, 请实现ShiroAuthService并注入到Spring容器");
        }

        @Override
        public Set<String> getPermissions(Object userId) {
            throw new AuthorizationException("功能未实现, 请实现ShiroAuthService并注入到Spring容器");
        }

        @Override
        public Object doLogin(String username, String password) {
            throw new AuthorizationException("功能未实现, 请实现ShiroAuthService并注入到Spring容器");
        }

        @Override
        public Object doLogin(AuthenticationToken token) {
            throw new AuthorizationException("功能未实现, 请实现ShiroAuthService并注入到Spring容器");
        }

        @Override
        public String[] getWhitelistResources() {
            throw new AuthorizationException("功能未实现, 请实现ShiroAuthService并注入到Spring容器");
        }
    }
}
