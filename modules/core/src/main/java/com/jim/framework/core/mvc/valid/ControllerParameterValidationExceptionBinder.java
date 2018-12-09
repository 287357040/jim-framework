package com.jim.framework.core.mvc.valid;

import com.jim.framework.common.mvc.JimResponse;
import com.jim.framework.common.util.serialization.JsonSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by celiang.hu on 2018-12-02.
 */
@ControllerAdvice
public class ControllerParameterValidationExceptionBinder {

    private static JsonSerializer jsonSerializer = new JsonSerializer();

    @ExceptionHandler(ControllerParameterValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String processControllerParameterValidationException(NativeWebRequest request, ControllerParameterValidationException validationException) throws Exception {
        JimResponse apiResponse = new JimResponse(JimResponse.VALIDATION_FAIL, validationException.getErrorInfo());
        HttpServletRequest httpServletRequest = (HttpServletRequest) (request.getNativeRequest());
        HttpServletResponse httpServletResponse = (HttpServletResponse) (request.getNativeResponse());
        this.outputAPIResponse(apiResponse, httpServletResponse, HttpStatus.BAD_REQUEST.value());
        return null;
    }

    protected void outputAPIResponse(JimResponse apiResponse, HttpServletResponse response, int httpStatus) throws Exception {
        byte[] data = jsonSerializer.serialize(apiResponse);
        response.setStatus(httpStatus);
        response.setContentType("application/json;charset=utf-8");
        response.setContentLength(data.length);
        response.getOutputStream().write(data);
    }
}
