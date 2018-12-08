package com.jim.framework.core.mvc.filter.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.regex.Pattern;

/**
 * Created by celiang.hu on 2018-11-11.
 */
public class XSSDefenceRequestWrapper extends HttpServletRequestWrapper {
    public XSSDefenceRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String parameter) {
        String value = super.getHeader(parameter);
        return cleanXSSCode(value);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        for (int i = 0; i < values.length; i++) {
            values[i] = cleanXSSCode(values[i]);
        }
        return values;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return cleanXSSCode(value);
    }

    private String cleanXSSCode(String value) {
        if(value !=null){
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
            // value = ESAPI.encoder().canonicalize(value);
            // Avoid null characters
            value = value.replaceAll("","");
            // Avoid anything between script tags
            Pattern scriptPattern =Pattern.compile("(.*?)",Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid anything in a src="http://www.yihaomen.com/article/java/..." type of e­xpression
            scriptPattern =Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",Pattern.CASE_INSENSITIVE |Pattern.MULTILINE |Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            scriptPattern =Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",Pattern.CASE_INSENSITIVE |Pattern.MULTILINE |Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Remove any lonesome  tag
            scriptPattern =Pattern.compile("",Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Remove any lonesome  tag
            scriptPattern =Pattern.compile("",Pattern.CASE_INSENSITIVE |Pattern.MULTILINE |Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid eval(...) e­xpressions
            scriptPattern =Pattern.compile("eval\\((.*?)\\)",Pattern.CASE_INSENSITIVE |Pattern.MULTILINE |Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid e­xpression(...) e­xpressions
            scriptPattern =Pattern.compile("e­xpression\\((.*?)\\)",Pattern.CASE_INSENSITIVE |Pattern.MULTILINE |Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid javascript:... e­xpressions
            scriptPattern =Pattern.compile("javascript:",Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid vbscript:... e­xpressions
            scriptPattern =Pattern.compile("vbscript:",Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
            // Avoid onload= e­xpressions
            scriptPattern =Pattern.compile("onload(.*?)=",Pattern.CASE_INSENSITIVE |Pattern.MULTILINE |Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");
        }
        return value;
    }
}
