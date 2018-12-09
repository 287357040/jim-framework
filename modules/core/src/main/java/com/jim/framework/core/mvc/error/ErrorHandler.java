package com.jim.framework.core.mvc.error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by celiang.hu on 2018-11-12.
 */
public interface ErrorHandler {
    void handle404(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void handleError(Throwable t,HttpServletRequest request, HttpServletResponse response) throws IOException;
}
