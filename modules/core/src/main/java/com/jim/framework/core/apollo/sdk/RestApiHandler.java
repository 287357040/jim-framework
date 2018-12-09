package com.jim.framework.core.apollo.sdk;

/**
 * Created by celiang.hu on 2018-12-07.
 */
public interface RestApiHandler<TRequest, TResponse> {
    TResponse handle(TRequest request);
}
