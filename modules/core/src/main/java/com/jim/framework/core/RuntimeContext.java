package com.jim.framework.core;

import com.jim.framework.core.cloud.application.JimApplication;

/**
 * Created by celiang.hu on 2018-11-23.
 */
public class RuntimeContext {
    private static final RuntimeContext instance = new RuntimeContext();
    private JimApplication jimApplication;

    private RuntimeContext(){

    }
    public static RuntimeContext getInstance() {
        return instance;
    }

    public JimApplication getJimApplication() {
        return jimApplication;
    }

    public void setJimApplication(JimApplication jimApplication) {
        this.jimApplication = jimApplication;
    }
}
