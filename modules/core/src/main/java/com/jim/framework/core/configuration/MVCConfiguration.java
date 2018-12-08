package com.jim.framework.core.configuration;

import com.jim.framework.core.mvc.DateRequestParameterBinder;
import com.jim.framework.core.mvc.valid.ControllerParamterValidationAspect;
import com.jim.framework.core.spring.SpringContextUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.util.Date;

/**
 * Created by celiang.hu on 2018-11-11.
 */
@Configuration
public class MVCConfiguration {


    @Bean
    public SpringContextUtils springApplicationUtils() {
        return new SpringContextUtils();
    }

    @Bean
    public ControllerParamterValidationAspect controllerParamterValidationAspect() {
        ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        ExecutableValidator validator = factory.getValidator().forExecutables();

        return new ControllerParamterValidationAspect(validator);
    }

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Date.class, new DateRequestParameterBinder(false));
    }
}
