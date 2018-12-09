package com.jim.framework.common.util.serialization;

/**
 * Created by celiang.hu on 2018-11-13.
 */
public class SerializeException extends RuntimeException {

    public SerializeException(String message) {
        super(message);
    }

    public SerializeException(String message, Exception e) {
        super(message, e);
    }
}
