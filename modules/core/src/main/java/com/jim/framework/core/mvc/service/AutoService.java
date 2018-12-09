package com.jim.framework.core.mvc.service;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 自动服务开发
 * Created by celiang.hu on 2018-11-28.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface AutoService {
}
