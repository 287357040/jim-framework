package com.jim.framework.auth;

/**
 * Created by celiang.hu on 2018-11-18.
 */
public enum AuthorizationMode {
    URL,ANNOTATION,MIXED;

    public static AuthorizationMode parse(String authorizationMode){
        if("ANNOTATION".equalsIgnoreCase(authorizationMode)){
            return ANNOTATION;
        }
        if("URL".equalsIgnoreCase(authorizationMode)){
            return URL;
        }
        if("MIXED".equalsIgnoreCase(authorizationMode)){
            return MIXED;
        }
        throw new IllegalArgumentException("错误的权限工作模式, 只支持URL/ANNOTATION/MIXED");
    }
    public boolean isURLMode(){
        return this.equals(URL);
    }

    public boolean isAnnotiationMode(){
        return this.equals(ANNOTATION);
    }

    public boolean isMixedMode(){
        return this.equals(MIXED);
    }
}
