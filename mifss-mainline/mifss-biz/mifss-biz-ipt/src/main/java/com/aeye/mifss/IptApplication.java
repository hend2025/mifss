package com.aeye.mifss;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.aeye.mifss" })
@EnableDubbo
public class IptApplication {
    public static void main(String[] args) {
        SpringApplication.run(IptApplication.class, args);
    }
}
