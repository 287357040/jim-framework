package com.jim.framework.sample.joint.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Hello world!
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JointApplication {
    public static void main(String[] args) {
        SpringApplication.run(JointApplication.class, args);
    }
}
