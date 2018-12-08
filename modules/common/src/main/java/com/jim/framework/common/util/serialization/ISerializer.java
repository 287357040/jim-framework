package com.jim.framework.common.util.serialization;

import java.util.List;

/**
 * 常用的对象序列化接口抽象
 * Created by celiang.hu on 2018-11-13.
 */
public interface ISerializer {
    byte[] serialize(Object o);

    <T> T deserialize(byte[] data, Class<T> tpl);

    <T> List<T> deserializeList(byte[] data, Class<?> elementClasses);
}
