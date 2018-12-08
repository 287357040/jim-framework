package com.jim.framework.core.mvc.service;

import com.jim.framework.core.spring.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by celiang.hu on 2018-11-28.
 */
public class BaseServiceImpl<Mapper, Record> implements BaseService<Record> {

    private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

    public Mapper mapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        MethodExecutor<Integer> methodExecutor;
        methodExecutor = new MethodExecutor<>();
        return methodExecutor.execute("deleteByPrimaryKey", id);
    }

    @Override
    public int insert(Record record) {
        MethodExecutor<Integer> methodExecutor = new MethodExecutor<>();
        return methodExecutor.execute("insert", record);
    }

    @Override
    public int insertSelective(Record record) {
        MethodExecutor<Integer> methodExecutor = new MethodExecutor<>();
        return methodExecutor.execute("insertSelective", record);
    }


    @Override
    public Record selectByPrimaryKey(Integer id) {
        MethodExecutor<Record> methodExecutor = new MethodExecutor<>();
        return methodExecutor.execute("selectByPrimaryKey", id);
    }


    @Override
    public int updateByPrimaryKeySelective(Record record) {
        MethodExecutor<Integer> methodExecutor = new MethodExecutor<>();
        return methodExecutor.execute("updateByPrimaryKeySelective", record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(Record record) {
        MethodExecutor<Integer> methodExecutor = new MethodExecutor<>();
        return methodExecutor.execute("updateByPrimaryKeyWithBLOBs", record);
    }

    @Override
    public int updateByPrimaryKey(Record record) {
        MethodExecutor<Integer> methodExecutor = new MethodExecutor<>();
        return methodExecutor.execute("updateByPrimaryKey", record);
    }

    @Override
    public int deleteByPrimaryKeys(String ids) {
        if (StringUtils.isBlank(ids)) {
            return 0;
        }
        String[] idArray = ids.split("-");
        int count = 0;
        for (String idStr : idArray) {
            if (StringUtils.isBlank(idStr)) {
                continue;
            }
            Integer id = Integer.parseInt(idStr);
            MethodExecutor<Integer> methodExecutor = new MethodExecutor<>();
            Integer result = methodExecutor.execute("deleteByPrimaryKey", id);
            count += result;
        }
        return count;

    }

    @Override
    public void initMapper() {
        this.mapper = SpringContextUtils.getBean(getMapperClass());
    }

    /**
     * 获取类泛型class
     *
     * @return
     */
    public Class<Mapper> getMapperClass() {
        return (Class<Mapper>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }


    public class MethodExecutor<TResult> {
        public TResult execute(String methodName, Object... params) {
            try {
                Class<?>[] clazzs = getParamsClazzs(params);
                Method selectByExampleWithBLOBs = mapper.getClass().getDeclaredMethod(methodName, clazzs);
                Object result = selectByExampleWithBLOBs.invoke(mapper, params);
                return (TResult) result;
            } catch (IllegalAccessException e) {
                // TODO 这个异常是方法内部未捕获的异常，在连接地址错误的情况下 没看到详细的报错；感觉很不好，全部使用反射执行mapper比较难排查。
                logger.error(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                logger.error(e.getMessage(), e);
            } catch (NoSuchMethodException e) {
                logger.error(e.getMessage(), e);
            }
            return null;
        }

        private Class<?>[] getParamsClazzs(Object... params) {
            List<Class<?>> clazzs = new ArrayList<>();
            for (Object param : params) {
                clazzs.add(param.getClass());
            }
            return clazzs.toArray(new Class[0]);
        }

    }
}
