package com.jim.framework.core.apollo.sdk;

/**
 * Created by celiang.hu on 2018-12-07.
 *
 */
public class ApolloRestRequest extends RestRequest  {
    private String token;
    private String appId;
    private String clusterName;
    private String namespaceName;
    private String url;
    private Object[] urlValiables;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object[] getUrlValiables() {
        return urlValiables;
    }

    public void setUrlValiables(Object[] urlValiables) {
        this.urlValiables = urlValiables;
    }
}
