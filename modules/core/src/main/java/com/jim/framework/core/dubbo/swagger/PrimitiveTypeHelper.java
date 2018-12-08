package com.jim.framework.core.dubbo.swagger;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by celiang.hu on 2018-11-29.
 */
public class PrimitiveTypeHelper {
    private static final List<Class<?>> PRIMITIVES = new ArrayList<>();

    static {
        PRIMITIVES.add(Byte.class);
        PRIMITIVES.add(byte.class);

        PRIMITIVES.add(Short.class);
        PRIMITIVES.add(short.class);

        PRIMITIVES.add(Integer.class);
        PRIMITIVES.add(int.class);

        PRIMITIVES.add(Long.class);
        PRIMITIVES.add(long.class);

        PRIMITIVES.add(Float.class);
        PRIMITIVES.add(float.class);

        PRIMITIVES.add(Double.class);
        PRIMITIVES.add(double.class);

        PRIMITIVES.add(Boolean.class);
        PRIMITIVES.add(boolean.class);

        PRIMITIVES.add(char.class);
        PRIMITIVES.add(String.class);

        PRIMITIVES.add(BigInteger.class);
        PRIMITIVES.add(BigDecimal.class);

        PRIMITIVES.add(Date.class);
        PRIMITIVES.add(File.class);
        PRIMITIVES.add(URI.class);
        PRIMITIVES.add(URL.class);
        PRIMITIVES.add(UUID.class);
    }

    public static boolean primitive(Class<?> type) {
        return PRIMITIVES.contains(type);
    }
}
