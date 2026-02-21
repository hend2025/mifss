package com.aeye.mifss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@SpringBootApplication(scanBasePackages = { "com.aeye.mifss", "cn.hsa" })
@Profile("tencent")
@ImportResource({
        "classpath*:config/adapt/tencent/*.xml"
})
public class IptApplicationTencent {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "tencent");
        SpringApplication.run(IptApplicationTencent.class, args);
    }
}
