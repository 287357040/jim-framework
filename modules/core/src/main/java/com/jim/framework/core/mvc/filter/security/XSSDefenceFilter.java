package com.jim.framework.core.mvc.filter.security;

import com.jim.framework.core.mvc.filter.FilterAdapter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * Created by celiang.hu on 2018-11-11.
 */
public class XSSDefenceFilter extends FilterAdapter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new XSSDefenceRequestWrapper((HttpServletRequest) servletRequest), servletResponse);
    }
}
