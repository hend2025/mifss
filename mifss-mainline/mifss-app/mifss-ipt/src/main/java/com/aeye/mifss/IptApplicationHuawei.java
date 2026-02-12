package com.aeye.mifss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@SpringBootApplication(scanBasePackages = { "com.aeye.mifss", "cn.hsa.ims" })
@Profile("huawei")
@ImportResource({
        "classpath*:config/adapt/huawei/*.xml"
})
public class IptApplicationHuawei {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "huawei");
        SpringApplication.run(IptApplicationHuawei.class, args);
    }
}
