package com.aeye.mifss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@SpringBootApplication(scanBasePackages = { "com.aeye.mifss", "cn.hsa.ims" })
@Profile("ali")
@ImportResource({
        "classpath:config/adapt/rpc/ali/rpc.xml",
        "classpath:config/adapt/rpc/ali/provider.xml",
        "classpath:config/adapt/rpc/ali/consumer.xml"
})
public class IptApplicationAli {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "ali");
        SpringApplication.run(IptApplicationAli.class, args);
    }
}
