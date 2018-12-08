package com.jim.framework.core.apollo.sdk;

/**
 * Created by celiang.hu on 2018-12-07.
 */
public class RestResponse {
    private boolean isSucess;
    private String errorMessage;

    public boolean isSucess() {
        return isSucess;
    }

    public void setSucess(boolean sucess) {
        isSucess = sucess;
    }

    public String isErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
