package com.jim.framework.auth.configuration;

import com.jim.framework.auth.*;
import com.jim.framework.auth.aop.AuthorizationAttributeExtentionSourceAdvisor;
import com.jim.framework.auth.filter.PathMatchingPermissionFilter;
import com.jim.framework.common.web.RequestUtils;
import net.sf.ehcache.config.CacheConfiguration;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by celiang.hu on 2018-11-16.
 */
@Configuration
public class ShiroAuthConfiguration {

    private Logger logger = LoggerFactory.getLogger(ShiroAuthConfiguration.class);
    private static final String JIM_INTRANET_ANON = "jim_intranet_anon";
    public static final String JIM_URL_MODE = "jim_url_mode";


    @Value("${sweet.framework.auth.mode:MIXED}")
    private String authMode;

    @Bean
    @ConditionalOnMissingBean(ShiroFilterBuilder.class)
    public ShiroFilterBuilder shiroFilterBuilder() {
        ShiroFilterBuilder builder = ShiroFilterBuilder.Default.getInstance();
        return builder;
    }


/**
 * 注解访问授权动态拦截，不然不会执行doGetAuthenticationInfo
 *
 * @param defaultWebSecurityManager
 * @return
 */
    @Bean
    @ConditionalOnExpression("'ANNOTATION'.equalsIgnoreCase('${sweet.framework.auth.mode:MIXED}') || 'MIXED'.equalsIgnoreCase('${sweet.framework.auth.mode:MIXED}')")
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager defaultWebSecurityManager) {
        AuthorizationAttributeExtentionSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeExtentionSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager);
        return authorizationAttributeSourceAdvisor;
    }


    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager shiroCacheManager() {
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setMaxEntriesLocalHeap(10000);
        cacheConfiguration.setEternal(false);
        cacheConfiguration.setTimeToIdleSeconds(3600);
        cacheConfiguration.setTimeToLiveSeconds(0);
        cacheConfiguration.setOverflowToDisk(false);
        cacheConfiguration.setStatistics(true);
        net.sf.ehcache.config.Configuration configuration = new net.sf.ehcache.config.Configuration();
        configuration.setDefaultCacheConfiguration(cacheConfiguration);
        net.sf.ehcache.CacheManager cacheManager = new net.sf.ehcache.CacheManager(configuration);
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(cacheManager);
        return ehCacheManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public UserAuthenticationService userAuthenticationService() {
        return AbstractUserAuthenticationService.Default.getInstance();
    }

    @Bean
    public PermissionResolver permissionResolver(){
        return new UrlPermissionResolver();
    }

    @Bean
    public JimAuthorizingRealm authorizingRealm(UserAuthenticationService userAuthenticationService) {
        return new JimAuthorizingRealm(userAuthenticationService);
    }

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(JimAuthorizingRealm jimAuthorizingRealm, CacheManager cacheManager) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager(jimAuthorizingRealm);
        defaultWebSecurityManager.setCacheManager(cacheManager);
        return defaultWebSecurityManager;
    }


    @Bean
    @ConditionalOnMissingBean(ShiroFilterFactoryBean.class)
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager,
                                                         JimAuthorizingRealm jimAuthorizingRealm,
                                                         UrlPermissionResolver urlPermissionResolver,
                                                         UserAuthenticationService userAuthenticationService) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 设置filter
        // 业务自定义的filters
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.putAll(userAuthenticationService.getExtendFilters());
        // 框架资源限定内部访问
        filterMap.put(JIM_INTRANET_ANON, new AnonymousFilter() {
            @Override
            protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) {
                if (RequestUtils.isRequestFromIntranet((HttpServletRequest) request)) {
                    return super.onPreHandle(request, response, mappedValue);
                } else {
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                    httpServletResponse.setContentType("text/html;charset=utf-8");
                    try {
                        httpServletResponse.getWriter().println("<h1>框架资源只能从内网访问, 请使用\"127.0.0.1\"替代\"localhost\"</h1>");
                    } catch (IOException e) {
                        //
                    }
                    return false;
                }
            }
        });
        PathMatchingPermissionFilter pathMatchingPermissionFilter = new PathMatchingPermissionFilter(userAuthenticationService, jimAuthorizingRealm,urlPermissionResolver);
        filterMap.put(JIM_URL_MODE, pathMatchingPermissionFilter);
        shiroFilterFactoryBean.setFilters(filterMap);

        Map<String, String> filterChainDefinition = new LinkedHashMap<>();
        for (String resource : ApplicationResource.getWhitelistResources()) {
            filterChainDefinition.put(resource, JIM_INTRANET_ANON);
        }
        // 跨站登录白名单

        // 开放资源白名单
        filterChainDefinition.put("/anon/**", "anon");
        logFilterInfo("/anon/**", "anon");
        // 应用白名单
        if (userAuthenticationService.getWhitelistResources() != null) {
            for (String resource : userAuthenticationService.getWhitelistResources()) {
                filterChainDefinition.put(resource, "anon");
                logFilterInfo(resource, "anon");
            }
        }

        LinkedHashMap<String, String> patternMatchedFiltersMap = userAuthenticationService.getPatternMatchedFilters();
        if (patternMatchedFiltersMap != null) {
            for (String pattern : patternMatchedFiltersMap.keySet()) {
                String filterName = patternMatchedFiltersMap.get(pattern);
                filterChainDefinition.put(pattern, filterName);
                logFilterInfo(pattern, filterName);
            }
        }
        AuthorizationMode authorizationMode = AuthorizationMode.parse(this.authMode);
        if (authorizationMode.isURLMode() || authorizationMode.isMixedMode()) {
            filterChainDefinition.put("/**", JIM_URL_MODE);
            logFilterInfo("/**", JIM_URL_MODE);
        }else {
            filterChainDefinition.put("/**",userAuthenticationService.getDefaultResourceFilterName());
            logFilterInfo("/**", userAuthenticationService.getDefaultResourceFilterName());
        }

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinition);
        shiroFilterFactoryBean.setLoginUrl(userAuthenticationService.getLoginUrl());
        return shiroFilterFactoryBean;
    }

    private void logFilterInfo(String pattern, String filterName) {
        logger.info("资源【" + pattern + "】使用【" + filterName + "】过滤规则过滤, 资源匹配规则【Ant】");
    }
}

