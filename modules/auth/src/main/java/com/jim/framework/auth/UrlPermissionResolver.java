package com.jim.framework.auth;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by celiang.hu on 2018-11-18.
 */
public class UrlPermissionResolver implements PermissionResolver {

    private static final Logger logger = LoggerFactory.getLogger(UrlPermissionResolver.class);
    private static final String PERSMISSION_METHOD_SPLIT = ";";
    @Override
    public Permission resolvePermission(String permissionString) {

        if (permissionString.startsWith("/")) {
            if(permissionString.contains(PERSMISSION_METHOD_SPLIT)){
                String urlPermission = permissionString.split(PERSMISSION_METHOD_SPLIT)[0];
                String methodPermission = permissionString.split(PERSMISSION_METHOD_SPLIT)[1];
                return new UrlPermission(urlPermission,methodPermission);
            }
            return new UrlPermission(permissionString);
        }
        return new WildcardPermission(permissionString);
    }
}
