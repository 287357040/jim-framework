package com.jim.framework.core.apollo.sdk;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

/**
 * Created by celiang.hu on 2018-12-07.
 */
public class RestRequest {

   private String baseUrl;
   private HttpEntity<Object> data;
   private HttpMethod httpMethod;


    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public HttpEntity<Object> getData() {
        return data;
    }

    public void setData(HttpEntity<Object> data) {
        this.data = data;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }
}
