package com.jim.framework.common.mvc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JimResponse<T> implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(JimResponse.class);

    public static final String NOT_INITIALIZED = "not-initialized";
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public static final String UNAUTHORIZED_SERVICE_INVOKER = "unauthorized-invoker";
    public static final String VALIDATION_FAIL = "validation-fail";
    public static final String BAD_PARAMETER = "bad-parameter";
    public static final String UNAUTHORIZED = "unauthorized";
    public static final String USER_NOT_LOGIN = "user-not-login";
    public static final String RPC_FAIL = "rpc-fail";

    /**
     * auth code
     */
    public static final String SIGN_INVALID = "sign-invalid";
    public static final String SIGN_AUTHORITY_INVALID = "sign-authority-invalid";

    private String code;

    private String message;

    private T data;

    public JimResponse() {
        this(NOT_INITIALIZED, null, null);
    }

    public JimResponse(String code) {
        this(code, null, null);
    }

    public JimResponse(String code, T data) {
        this(code, null, data);
    }

    public JimResponse(String code, String message, T data) {
        this.code = code;
        if (message == null) {
            this.message = ErrorTable.convertCode2LocaleMessage(code);
        } else {
            this.message = message;
        }
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return SUCCESS.equals(this.getCode());
    }


    public static <T> JimResponse<T> success(T data) {
        return new JimResponse(SUCCESS, data);
    }

    public static JimResponse fail(Throwable t) {
        return fail(t.getMessage());
    }


    public static JimResponse fail(String message) {
        return new JimResponse(FAIL, null, message);
    }

    public static JimResponse fail(BindingResult result) {
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        return new JimResponse(VALIDATION_FAIL, errorMap);
    }
}
