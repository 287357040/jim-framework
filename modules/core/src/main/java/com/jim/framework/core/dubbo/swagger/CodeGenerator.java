package com.jim.framework.core.dubbo.swagger;

import com.alibaba.dubbo.config.annotation.Service;
import com.jim.framework.common.util.ClasspathPackageScanner;
import com.jim.framework.common.util.PackageScanner;
import com.jim.framework.core.JimUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.Assert;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 后续使用模板的方式生成
 * * Created by celiang.hu on 2018-11-29.
 */
public class CodeGenerator {
    private static final Logger logger = LoggerFactory.getLogger(CodeGenerator.class);


    public static void main(String[] args) {
        String strPackages = args[0];
        Assert.hasText(strPackages, "扫描包路径不能为空！");

        String[] packages = StringUtils.split(strPackages, ",");
        for (String pack : packages) {
            PackageScanner packageScanner = new ClasspathPackageScanner(pack);
            try {
                CodeGenerator generator = new CodeGenerator();
                List<Class<?>> service = new ArrayList<>();
                List<String> classNames = packageScanner.getFullyQualifiedClassNameList();
                for (String name : classNames) {
                    Class<?> apiClass = CodeGenerator.class.getClassLoader().loadClass(name);
                    for (Annotation annotation : apiClass.getAnnotations()) {
                        if (Service.class.isInstance(annotation)) {
                            service.add(apiClass);
                        }
                    }
                    generator.generateDubboController(service);
                }
            } catch (Exception e) {
                logger.error("生成Dubbo Controller失败：" + e.getMessage(), e);
            }
        }
    }

    public void generateDubboController(List<Class<?>> clazzs) throws NoSuchMethodException, ClassNotFoundException, IOException {
        for (Class<?> clazz : clazzs) {
            List<String> pathAndCode = CodeGenerator.generateClassCode(clazz);
            String fileName = pathAndCode.get(0);
            String javaSourcePath = pathAndCode.get(1);
            File createFile = new File(fileName);
            FileUtils.writeStringToFile(new File(fileName), javaSourcePath, "utf-8");
        }
    }

    public static List<String> generateClassCode(Class<?> clazz) throws NoSuchMethodException, ClassNotFoundException {
        List<String> pathAndCodeList = new ArrayList<>();
        StringBuilder codeBuilder = new StringBuilder();
        /**
         * 1 获取接口类和实现类
         */
        final Class<?> dubboInterface = clazz.getAnnotation(Service.class).interfaceClass();
        final Class<?> rawDubboInterfaceImpl = clazz;
        Class<?> dubboInterfaceImpl = rawDubboInterfaceImpl;


        String basePath = JimUtils.getProjectBasePath();
        pathAndCodeList.add(basePath + "/src/main/java/" + rawDubboInterfaceImpl.getPackage().getName().replaceAll("\\.", "/") + "/controller/" + dubboInterfaceImpl.getSimpleName() + "Controller.java");
        String dubboInterfaceImplCanonicalName = rawDubboInterfaceImpl.getCanonicalName();
        // 保护性判断：防止dubbo接口实现类被代理，这里转换为真实类
        if (dubboInterfaceImplCanonicalName.contains("$$")) {
            dubboInterfaceImpl = ClassLoader.getSystemClassLoader()
                    .loadClass(dubboInterfaceImplCanonicalName.substring(0, dubboInterfaceImplCanonicalName.indexOf("$$")));
        }

        /**
         * 2 获取包名
         */
        final String packageName = dubboInterfaceImpl.getPackage().getName();
        // 保护性措施：包名尾部添加DubboApi，防止被意外AOP
        codeBuilder.append("package ").append(packageName).append(".controller;\n");

        /**
         * 4 获取重组@Api注解
         */
        final Api api = dubboInterface.getAnnotation(Api.class);
        if (api != null) {
            codeBuilder.append("@io.swagger.annotations.Api(value = \"" + api.value() + "\",tags = \"" + api.tags()[0] + "\")").append("\n");
        }

        /**
         * 5 创建@RestController @RequestMapping注解
         * TODO RequestMapping这边使用项目的Context即可
         */
        codeBuilder.append("@org.springframework.web.bind.annotation.RestController\n")
                .append("@org.springframework.web.bind.annotation.RequestMapping(\"/\")\n");

        /**
         * 6 创建类定义
         * public class BookServiceImplDubboApi
         */
        String classDefinition = dubboInterfaceImpl.toGenericString();
        codeBuilder.append(classDefinition.substring(0, classDefinition.lastIndexOf(" ") + 1))
                .append(dubboInterfaceImpl.getSimpleName()).append("Controller {\n");

        /**
         * 7 注入实现类
         */
        String dubboInterfaceImplName = dubboInterfaceImpl.getSimpleName().substring(0, 1).toLowerCase()
                + dubboInterfaceImpl.getSimpleName().substring(1);
        codeBuilder.append("@org.springframework.beans.factory.annotation.Autowired\n").append("private ")
                .append(dubboInterfaceImpl.getCanonicalName()).append(" ").append(dubboInterfaceImplName).append(";\n");

        /**
         * 8 获取全部方法
         */
        for (Method method : dubboInterface.getMethods()) {

            /**
             * 8.1 获取重组ApiOperation注解
             */
            final ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            if (apiOperation != null) {
                codeBuilder.append("@io.swagger.annotations.ApiOperation(value = \"" + apiOperation.value() + "\")\n");
            }

            /**
             * 8.2 创建@RequestMapping注解
             * 支持多版本
             * 支持方法重写
             */
            //String serviceVersion = clazz.getVersion() != null && clazz.getVersion().length() > 0 ? clazz.getVersion() + "/" : "";
            String serviceVersion = "";
            codeBuilder.append("@org.springframework.web.bind.annotation.RequestMapping(value = \"/" + serviceVersion
                    + StringUtils.substringAfterLast(dubboInterfaceImpl.getCanonicalName(), ".") + "/" + method.getName());
            if (apiOperation != null && apiOperation.nickname().trim().length() > 0) {
                codeBuilder.append("/").append(apiOperation.nickname().trim());
            }
            codeBuilder.append("\", method = org.springframework.web.bind.annotation.RequestMethod.POST)\n");

            if (method.isAnnotationPresent(Valid.class)) {
                codeBuilder.append("@javax.validation.Valid\n");
            }
            /**
             * 8.3 创建方法定义
             */
            codeBuilder.append("public ").append(method.getGenericReturnType().getTypeName()).append(" ")
                    .append(method.getName()).append("(");

            /**
             * 8.4 组建方法参数
             */
            final LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
            final String[] parameterNames = parameterNameDiscoverer
                    .getParameterNames(dubboInterfaceImpl.getMethod(method.getName(), method.getParameterTypes()));
            final Parameter[] parameters = method.getParameters();
            StringBuilder parameterBuilder = new StringBuilder();

            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                String parameterName = parameterNames[i];
                final ApiParam apiParam = parameter.getAnnotation(ApiParam.class);
                if (apiParam != null) {
                    parameterBuilder.append("@io.swagger.annotations.ApiParam(value = \"").append(apiParam.value())
                            .append("\"").append(", required = ").append(apiParam.required()).append(", defaultValue = \"")
                            .append(apiParam.defaultValue()).append("\") ");
                }

                if (PrimitiveTypeHelper.primitive(parameter.getType())) {
                    parameterBuilder.append("@org.springframework.web.bind.annotation.RequestParam(name = \"")
                            .append(parameterName).append("\"");
                    if (apiParam == null || apiParam.required() == false) {
                        parameterBuilder.append(", required = false");
                    } else {
                        parameterBuilder.append(", required = true");
                    }
                    parameterBuilder.append(")");

                    for (Annotation annotation : parameter.getDeclaredAnnotations()) {
                        String strResult = annotation.toString();
                        Map properties = getAnnotationProperties(annotation);
                        for (Object key : properties.keySet()) {
                            Object value = properties.get(key);
                            if (value instanceof String) {
                                strResult = strResult.replace(value.toString(), "\"" + value + "\"");
                            }
                        }
                        strResult = strResult.replace("[]", "{}");
                        parameterBuilder.append(strResult);
                    }
                } else {
//                    parameterBuilder.append("@springfox.documentation.spi.annotations.RequestModel(value = \"")
//                            .append(parameterName).append("\"");
                    parameterBuilder.append("@org.springframework.web.bind.annotation.RequestBody(");
                    if (apiParam == null || apiParam.required() == false) {
                        parameterBuilder.append("required = false");
                    } else {
                        parameterBuilder.append("required = true");
                    }
                    parameterBuilder.append(")");
                    parameterBuilder.append(" @org.springframework.validation.annotation.Validated ");
                }

                parameterBuilder.append(parameter.getParameterizedType().getTypeName()).append(" ");
                parameterBuilder.append(parameterName).append(",");
            }

            if (parameterBuilder != null && parameterBuilder.length() > 0) {
                codeBuilder.append(parameterBuilder.substring(0, parameterBuilder.length() - 1));
            }

            String parameterNamesStr = "";
            if (parameterNames != null && parameterNames.length > 0) {
                parameterNamesStr = String.join(", ", parameterNames);
            }

            /**
             * 8.5 组建方法异常标识
             */
            codeBuilder.append(") ");
            final Class<?>[] exceptionTypes = method.getExceptionTypes();
            if (exceptionTypes != null && exceptionTypes.length > 0) {
                codeBuilder.append("throws ");
                for (int i = 0; i < exceptionTypes.length - 1; i++) {
                    codeBuilder.append(exceptionTypes[i].getCanonicalName()).append(",");
                }
                codeBuilder.append(exceptionTypes[exceptionTypes.length - 1].getCanonicalName());
            }

            /**
             * 8.6 组建方法内容
             */
            codeBuilder.append(" {");
            if (!method.getGenericReturnType().getTypeName().equalsIgnoreCase("void")) {
                codeBuilder.append("return ");
            }
            codeBuilder.append(dubboInterfaceImplName).append(".").append(method.getName()).append("(")
                    .append(parameterNamesStr != "" ? parameterNamesStr : "").append(");");

            codeBuilder.append("}");
        }

        /**
         * 9 定义类结尾
         */
        codeBuilder.append("}");
        pathAndCodeList.add(codeBuilder.toString());
        return pathAndCodeList;
    }

    //获取该注解对象的属性值
    public static Map getAnnotationProperties(Annotation annotation) {
        Map result = null;
        if (annotation != null) {
            InvocationHandler invo = Proxy.getInvocationHandler(annotation); //获取被代理的对象
            Map map = (Map) getFieldValue(invo, "memberValues");
            result = map;
        }
        return result;
    }

    public static <T> Object getFieldValue(T object, String property) {
        if (object != null && property != null) {
            Class<T> currClass = (Class<T>) object.getClass();

            try {
                Field field = currClass.getDeclaredField(property);
                field.setAccessible(true);
                return field.get(object);
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException(currClass + " has no property: " + property);
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
