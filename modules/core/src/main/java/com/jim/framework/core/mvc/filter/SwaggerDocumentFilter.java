package com.jim.framework.core.mvc.filter;

import com.jim.framework.common.util.StringUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by celiang.hu on 2018-11-10.
 */
public class SwaggerDocumentFilter extends FilterAdapter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
        byte[] responseData = StringUtils.getBytes("<h1>Swagger rest documentation is disabled</h1>");
        httpServletResponse.setContentType("text/html;charset=utf-8");
        httpServletResponse.setContentLength(responseData.length);
        httpServletResponse.getOutputStream().write(responseData);
    }
}
