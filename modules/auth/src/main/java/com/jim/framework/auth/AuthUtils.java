package com.jim.framework.auth;

import com.jim.framework.common.mvc.Constants;
import org.apache.shiro.SecurityUtils;

/**
 * Created by celiang.hu on 2018-11-18.
 */
public class AuthUtils {
    public static Object getSessionUser() {
        return getSessionAttribute(Constants.Http.SESSION_USER_KEY);
    }

    private static Object getSessionAttribute(String attribute) {
        org.apache.shiro.session.Session session = SecurityUtils.getSubject().getSession();
        if (session != null) {
            return session.getAttribute(attribute);
        }
        return null;
    }
}
