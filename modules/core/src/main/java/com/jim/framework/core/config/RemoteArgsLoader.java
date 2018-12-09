package com.jim.framework.core.config;

import com.jim.framework.core.cloud.application.JimApplication;

/**
 * Created by celiang.hu on 2018-11-22.
 */
public abstract class RemoteArgsLoader extends AbstractArgsLoader {

    public RemoteArgsLoader(JimApplication application) {
        super(application);
    }
}
