package com.jim.framework.core.mvc.error;

import com.jim.framework.common.mvc.JimResponse;
import com.jim.framework.common.util.StringUtils;
import com.jim.framework.common.util.serialization.JsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by celiang.hu on 2018-11-12.
 */
public class DefaultErrorHandler implements ErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(DefaultErrorHandler.class);
    private boolean outputStackTrace = false;

    private JsonSerializer jsonSerializer = new JsonSerializer();

    public DefaultErrorHandler() {
        this(false);
    }

    public DefaultErrorHandler(boolean outputStackTrace) {
        this.outputStackTrace = outputStackTrace;
    }

    @Override
    public void handle404(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = (String) request.getAttribute("javax.servlet.error.request_url");
        StringBuffer output = new StringBuffer("<h1>Resource Not Found -> ");
        output.append(uri).append("</h1>");
        byte[] data = StringUtils.getBytes(output.toString());
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.setContentType("text/html;charset=utf-8");
        response.setContentLength(data.length);
        response.getOutputStream().write(data);
    }

    @Override
    public void handleError(Throwable t, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JimResponse jimResponse = new JimResponse(JimResponse.FAIL, t.getClass().getName(), getErrorMessage(t));
        outputApiResponse(request, response, jimResponse);
    }

    public void handleError(MethodArgumentNotValidException t, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BindingResult bindingResult = t.getBindingResult();
        JimResponse apiResponse = JimResponse.fail(bindingResult);
        this.outputApiResponse(apiResponse, request, response);
    }

    public void handleError(BindException bindException, HttpServletRequest request, HttpServletResponse response) throws Exception {
        BindingResult bindingResult = bindException.getBindingResult();
        JimResponse apiResponse = JimResponse.fail(bindingResult);
        this.outputApiResponse(apiResponse, request, response);
    }

    public void handleError(MissingServletRequestParameterException missingServletRequestParameterException, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JimResponse apiResponse = new JimResponse(JimResponse.BAD_PARAMETER, null, this.getErrorMessage(missingServletRequestParameterException));
        this.outputApiResponse(apiResponse, request, response);
    }

    private void outputApiResponse(JimResponse apiResponse, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.outputApiResponse(apiResponse, request, response);
    }

    private void outputApiResponse(HttpServletRequest request, HttpServletResponse response, JimResponse jimResponse) throws IOException {
        logger.debug("output request [{}] error with serialization representation ", request.getRequestURI());
        byte[] data = jsonSerializer.serialize(jimResponse);
        response.setContentType("application/serialization;charset=utf-8");
        response.setContentLength(data.length);
        response.getOutputStream().write(data);
    }

    private String getErrorMessage(Throwable t) throws IOException {
        String message = t.getMessage();
        if (outputStackTrace) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            message = sw.toString();
            sw.close();
        }
        return message;
    }
}
