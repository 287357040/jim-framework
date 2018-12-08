package com.jim.framework.auth.filter;

import com.jim.framework.common.mvc.JimResponse;
import com.jim.framework.common.util.serialization.JsonSerializer;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by celiang.hu on 2018-11-17.
 */
public class JimStandardResourceFilter extends FormAuthenticationFilter {
    public static final Logger logger = LoggerFactory.getLogger(JimStandardResourceFilter.class);

    JsonSerializer jsonSerializer = new JsonSerializer();
    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        JimResponse jimResponse = new JimResponse(JimResponse.USER_NOT_LOGIN,this.getLoginUrl());
        this.outputApiResponse((HttpServletResponse)response,jimResponse);
        super.redirectToLogin(request, response);
    }

   // 原来FormAuthenticationFilter的实现是响应了302, 然后跳转到登录页
    private void outputApiResponse(HttpServletResponse response, JimResponse jimResponse) throws IOException {
        String output = jsonSerializer.serialize2JSON(jimResponse);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(output);
    }
}
