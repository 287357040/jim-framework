package com.jim.framework.auth;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Set;

/**
 * 自定义的AuthorizingRealm
 * Created by celiang.hu on 2018-11-16.
 */
public class JimAuthorizingRealm extends AuthorizingRealm {
    public static final Logger logger = LoggerFactory.getLogger(JimAuthorizingRealm.class);
    private final UserAuthenticationService userAuthenticationService;

    public JimAuthorizingRealm(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    /**
     * Principals 包含了Realm验证成功的身份信息的集合对象
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Object principal = this.getAvailablePrincipal(principals);
        if (principal == null) {
            return null;
        }

        SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();
        authenticationInfo.setRoles(userAuthenticationService.getRoles(principal));
        authenticationInfo.setStringPermissions(userAuthenticationService.getPermissions(principal));
        return authenticationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        Assert.notNull(token, "logintoken can not be null");

        if (token.getClass().equals(UsernamePasswordToken.class)) {
            UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
            String userName = usernamePasswordToken.getUsername();
            String password = String.valueOf(usernamePasswordToken.getPassword());
            Object principal = userAuthenticationService.doLogin(userName, password);
            return new SimpleAuthenticationInfo(principal, password, this.getName());
        }

        Object principal = userAuthenticationService.doLogin(token);
        return new SimpleAuthenticationInfo(principal, token.getCredentials(), this.getName());
    }

    public Set<String> getPermissions(PrincipalCollection principalCollection) {
        return null;
    }
}
