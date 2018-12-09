package com.jim.framework.auth;

import com.jim.framework.common.util.StringUtils;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.util.PatternMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by celiang.hu on 2018-11-18.
 */
public class UrlPermission implements Permission {
    private static final Logger logger = LoggerFactory.getLogger(UrlPermission.class);

    private String url;
    private String method;



    public UrlPermission(String url) {
        this(url,"");
    }

    public UrlPermission(String url, String method) {
        this.url = url;
        this.method = method;
    }

    @Override
    public boolean implies(Permission p) {
        if (!(p instanceof UrlPermission)) {
            return false;
        }
        UrlPermission urlPermission = (UrlPermission) p;
        PatternMatcher patternMatcher = new AntPathMatcher();
        boolean matches = patternMatcher.matches(this.url, urlPermission.url);
        if (matches) {
            if (StringUtils.hasText(urlPermission.method)) {
                return urlPermission.method.contains(this.method);
            }
        }
        logger.debug("matches => " + matches);
        return matches;
    }
}

