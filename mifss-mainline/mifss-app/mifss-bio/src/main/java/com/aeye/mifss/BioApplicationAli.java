package com.aeye.mifss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

/**
 * 阿里云平台启动类
 */
@SpringBootApplication(scanBasePackages = { "com.aeye.mifss", "cn.hsa.ims" })
@Profile("ali")
@ImportResource({
        "classpath:config/adapt/rpc/ali/rpc.xml",
        "classpath:config/adapt/rpc/ali/rpc-provider.xml",
        "classpath:config/adapt/rpc/ali/rpc-consumer.xml"
})
public class BioApplicationAli {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "ali");
        SpringApplication.run(BioApplicationAli.class, args);
    }
}
