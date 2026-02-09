package com.aeye.mifss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

/**
 * 通用平台（开源云）启动类
 */
@SpringBootApplication(scanBasePackages = { "com.aeye.mifss", "cn.hsa.ims" })
@Profile("generic")
@ImportResource({
        "classpath:config/adapt/rpc/generic/rpc.xml",
        "classpath:config/adapt/rpc/generic/provider.xml",
        "classpath:config/adapt/rpc/generic/consumer.xml"
})
public class BioApplication {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "generic");
        SpringApplication.run(BioApplication.class, args);
    }
}
