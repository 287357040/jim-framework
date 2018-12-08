package com.jim.framework.core.mvc.controller;

import com.jim.framework.common.util.StringUtils;
import com.jim.framework.core.mvc.error.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.DispatcherServlet;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

/**
 * Created by celiang.hu on 2018-11-12.
 */
@ConditionalOnExpression("'${jim.framework.core.mvc.error.handle:true}'=='true'")
@Controller
@ApiIgnore
public class JimFrameworkErrorController implements ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(JimFrameworkErrorController.class);

    public static final String ERROR_PATH = "/error";

    @Value("${jim.framework.core.mvc.error.handler.outputStackTrace:false}")
    private boolean outputStackTrace;

    @Autowired(required = false)
    private ErrorHandler errorHandler;


    @RequestMapping(value = ERROR_PATH)
    public String error(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Throwable t = getErrorInfoFromRequest(request);
        if (t != null) {
            if (this.errorHandler != null) {
                try {
                    // 通过反射方式，调用具体的Exceptionc处理方法，部分的Exception定义了BindingResult的错误信息集合类型，采用这种个方式调用，感觉怪怪的
                    Method method = this.errorHandler.getClass().getMethod("handlerError", t.getClass(), HttpServletRequest.class, HttpServletResponse.class);
                    method.invoke(this.errorHandler, t, request, response);
                } catch (NoSuchMethodException e) {
                    this.errorHandler.handleError(t, request, response);
                }
            }else{
                this.outputErrorStack(t,request,response);
            }
        }
        int httpStatus = response.getStatus();
        if(httpStatus == HttpStatus.NOT_FOUND.value()){
            if(this.errorHandler!=null){
                this.errorHandler.handle404(request,response);
            }else{
                this.output404(request,response);
            }
        }
        return null;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    private void output404(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = (String)request.getAttribute("javax.servlet.error.request_uri");
        StringBuffer output = new StringBuffer("<h1>Resource Not Found -> ");
        output.append(uri).append("</h1>");
        byte []data = StringUtils.getBytes(output.toString());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setContentType("text/html;charset=utf-8");
        response.setContentLength(data.length);
        response.getOutputStream().write(data);
    }

    private void outputErrorStack(Throwable t, HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("<h1>ERROR - " + HttpStatus.INTERNAL_SERVER_ERROR.value() + " - " + request.getRequestURI() +"</h1>");
        if(outputStackTrace) {
            pw.println("<pre>");
            t.printStackTrace(pw);
            pw.println("</pre>");
        }
        byte[] data = StringUtils.getBytes(sw.toString());
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setContentLength(data.length);
        response.getOutputStream().write(data);
        sw.close();
    }

    private Throwable getErrorInfoFromRequest(HttpServletRequest request) {
        Throwable t = null;
        Object error = request.getAttribute(DispatcherServlet.EXCEPTION_ATTRIBUTE);
        if (error instanceof Throwable) {
            t = (Throwable) error;
            return t;
        }
        // springboot HandlerExceptionResolver:DefaultErrorAttributes,会将获取到的异常写入到
        // attribute "org.springframework.boot.autoconfigure.web.DefaultErrorAttributes.ERROR"
        error = request.getAttribute(DefaultErrorAttributes.class.getName() + ".ERROR");
        if (error instanceof Throwable) {
            t = (Throwable) error;
            return t;
        }

        error = request.getAttribute("javax.servlet.error.exception");
        if (error instanceof Throwable) {
            t = (Throwable) error;
            return t;
        }
        return t;
    }
}
