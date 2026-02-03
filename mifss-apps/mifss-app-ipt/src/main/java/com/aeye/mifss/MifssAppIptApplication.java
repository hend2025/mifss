package com.aeye.mifss;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.aeye.mifss", "cn.hsa" })
@MapperScan("com.aeye.mifss.**.mapper")
@EnableDubbo
public class MifssAppIptApplication {
    public static void main(String[] args) {
        SpringApplication.run(MifssAppIptApplication.class, args);
    }
}
