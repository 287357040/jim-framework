package com.jim.framework.common.util.serialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by celiang.hu on 2018-11-13.
 */
public class JsonSerializer implements ISerializer {

    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
        objectMapper.configure(SerializationFeature.FLUSH_AFTER_WRITE_VALUE, true);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private String charset = "utf-8";


    @Override
    public byte[] serialize(Object o) {
        if (o == null) return new byte[0];

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(baos, charset);
            objectMapper.writeValue(writer, o);
            byte[] data = baos.toByteArray();
            return data;
        } catch (Exception e) {
            throw new SerializeException(e.getMessage(), e);
        } finally {
            safeClose(writer);
            safeClose(baos);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> tpl) {
        if (data == null || data.length == 0) {
            return null;
        }

        Object result = null;
        String content = null;
        try {
            content = new String(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new SerializeException(e.getMessage(), e);
        }
        try {
            result = objectMapper.readValue(content, TypeFactory.rawClass(tpl));
        } catch (IOException e) {
            throw new SerializeException("error while parsing json character -> " + content.replaceAll("\\r\\n", ""), e);
        }
        return (T) result;
    }

    @Override
    public <T> List<T> deserializeList(byte[] data, Class<?> elementClasses) {
        if (data == null || data.length == 0) {
            return null;
        }
        Object result = null;
        String content = null;
        try {
            content = new String(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new SerializeException(e.getMessage(), e);
        }
        try {
            JavaType javaType = getCollectionType(ArrayList.class, elementClasses);
            result = objectMapper.readValue(content, javaType);
        } catch (IOException e) {
            throw new SerializeException("error while parsing json character -> " + content.replaceAll("\\r\\n", ""), e);
        }
        return (List<T>) result;
    }

    public String serialize2JSON(Object o){
        try {
            return new String(this.serialize(o), this.charset);
        } catch (UnsupportedEncodingException e) {
            throw new SerializeException(e.getMessage(), e);
        }
    }
    private JavaType getCollectionType(Class<ArrayList> arrayListClass, Class<?> elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(TypeFactory.rawClass(arrayListClass),
                TypeFactory.rawClass(elementClasses));
    }

    private void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
