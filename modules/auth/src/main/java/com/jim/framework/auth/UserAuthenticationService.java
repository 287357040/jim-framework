package com.jim.framework.auth;

import org.apache.shiro.authc.AuthenticationToken;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 用户认证权限的服务接口，抽象出Shiro所必需的数据
 * * Created by celiang.hu on 2018-11-15.
 */
public interface UserAuthenticationService<T> {

    /**
     * 获取业务系统的登录url地址
     *
     * @return
     */
    String getLoginUrl();

    /**
     * 获取用户的角色
     *
     * @param userId
     * @return
     */
    Set<String> getRoles(T userId);

    /**
     * 获取用户的权限信息
     *
     * @param userId
     * @return
     */
    Set<String> getPermissions(T userId);

    /**
     * 通过用户名密码方式进行登录
     *
     * @param username
     * @param password
     * @return
     */
    T doLogin(String username, String password);

    /**
     * 通过其他的认证方式进行登录
     *
     * @param token
     * @return
     */
    T doLogin(AuthenticationToken token);

    /**
     * 实现为获取系统中的白名单资源URI, 支持Ant规则匹配
     *
     * @return
     */
    String[] getWhitelistResources();

    /**
     * 业务自定义扩展Filter
     * @return
     */
    Map<String,Filter> getExtendFilters();

    /**
     * 实现为返回自定义的pattern匹配和filter处理,
     * 其中: filter name可以为getExtendFilters()返回的filter name
     */
    LinkedHashMap<String,String> getPatternMatchedFilters();

    /**
     * 实现为默认资源(/**)的处理Filter, 当返回ShiroAuthService.DEFAULT_RESOURCE_FILTER_NAME(authc)时, 使用Shiro默认的注解验证规则
     * 如果不使用注解验证, 通常应返回一个在ShiroAuthService#getExtendFilters()中返回的自定义Filter
     */
    String getDefaultResourceFilterName();
}
