package com.aeye.mifss;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication(scanBasePackages = { "com.aeye.mifss", "cn.hsa.ims" })
public class IptApplication {
    public static void main(String[] args) {
        SpringApplication.run(IptApplication.class, args);
    }
}
