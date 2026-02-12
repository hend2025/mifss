package com.aeye.mifss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@SpringBootApplication(scanBasePackages = { "com.aeye.mifss", "cn.hsa.ims" })
@Profile("generic")
@ImportResource({
        "classpath*:config/adapt/generic/*.xml"
})
public class BasApplication {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "generic");
        SpringApplication.run(BasApplication.class, args);
    }
}
