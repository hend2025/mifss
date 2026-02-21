package com.aeye.mifss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

/**
 * 华为云平台启动类
 */
@SpringBootApplication(scanBasePackages = { "com.aeye.mifss", "cn.hsa" })
@Profile("huawei")
@ImportResource({
        "classpath*:config/adapt/huawei/*.xml"
})
public class BioApplicationHuawei {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "huawei");
        SpringApplication.run(BioApplicationHuawei.class, args);
    }
}
