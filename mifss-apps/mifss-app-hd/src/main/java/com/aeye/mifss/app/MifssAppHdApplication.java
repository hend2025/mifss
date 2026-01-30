package com.aeye.mifss.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.aeye.mifss" })
public class MifssAppHdApplication {
    public static void main(String[] args) {
        SpringApplication.run(MifssAppHdApplication.class, args);
    }
}
