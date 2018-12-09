package com.jim.framework.core.apollo.sdk;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by celiang.hu on 2018-12-07.
 * 可以不做sdk，由apollo提供对应的sdk即可，
 */
public class ApolloApiHandler implements RestApiHandler<ApolloRestRequest, ApolloRestResponse> {
    static RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory() {
        {
            setConnectTimeout(3000);
            setReadTimeout(3000);
        }
    });

    @Override
    public ApolloRestResponse handle(ApolloRestRequest apolloRestRequest) {
        ResponseEntity<Void> responseEntity = restTemplate.exchange(apolloRestRequest.getBaseUrl(), apolloRestRequest.getHttpMethod(), apolloRestRequest.getData(), Void.class, apolloRestRequest.getUrlValiables());
        ApolloRestResponse restResponse = new ApolloRestResponse();
        restResponse.setSucess(responseEntity.getStatusCode() == HttpStatus.OK);
        restResponse.setErrorMessage("Code:" + responseEntity.getStatusCode() + " Headers:" + responseEntity.getHeaders());
        return restResponse;
    }
}
