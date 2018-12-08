package com.jim.framework.sample.provider.service;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by celiang.hu on 2018-12-06.
 */
@Path("demo")
@Produces({ContentType.APPLICATION_JSON_UTF_8})
public interface RestDemoService {

    @GET
    @Path("print")
    String printHost() throws Exception;
}
