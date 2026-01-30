package com.aeye.mifss.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.aeye.mifss" })
public class MifssAppChrApplication {
    public static void main(String[] args) {
        SpringApplication.run(MifssAppChrApplication.class, args);
    }
}
