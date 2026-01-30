package com.aeye.mifss.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = { "com.aeye.mifss" })
@MapperScan("com.aeye.mifss.**.mapper")
@EnableFeignClients(basePackages = "com.aeye.mifss")
public class MifssAppIptApplication {
    public static void main(String[] args) {
        SpringApplication.run(MifssAppIptApplication.class, args);
    }
}
