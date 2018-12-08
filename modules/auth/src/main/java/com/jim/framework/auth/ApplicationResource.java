package com.jim.framework.auth;

/**
 * Created by celiang.hu on 2018-11-17.
 */
public class ApplicationResource {
    private static String[] whitelistResources = {
            "/swagger**",
            "/configuration/**",
            "/druid/**",
            "/v2/api-docs/**",
            "/webjars/**",
            "/jim-framework/**",
            "/error"
    };

    public static String[] getWhitelistResources() {
        return whitelistResources;
    }
}
