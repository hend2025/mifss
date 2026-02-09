package com.aeye.mifss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

/**
 * 腾讯云平台启动类
 */
@SpringBootApplication(scanBasePackages = { "com.aeye.mifss", "cn.hsa.ims" })
@Profile("tencent")
@ImportResource({
        "classpath:config/adapt/rpc/tencent/rpc.xml"
})
public class BioApplicationTencent {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "tencent");
        SpringApplication.run(BioApplicationTencent.class, args);
    }
}
