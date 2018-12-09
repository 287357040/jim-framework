package com.jim.framework.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;

public abstract class RuntimeUtils {
    private static final Logger logger = LoggerFactory.getLogger(RuntimeUtils.class);

    private static int PID = 0;

    public static int getCurrentPID() {
        if(PID == 0) {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            String pid = name.split("@")[0];
            PID = Integer.parseInt(pid);
        }
        return PID;
    }
}
