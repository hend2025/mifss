package com.aeye.mifss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = { "com.aeye.mifss", "cn.hsa.ims" })
@ImportResource({
        "classpath:config/adapt/rpc/generic/rpc.xml",
        "classpath:config/adapt/rpc/generic/provider.xml",
        "classpath:config/adapt/rpc/generic/consumer.xml"
})
public class IptApplication {
    public static void main(String[] args) {
        SpringApplication.run(IptApplication.class, args);
    }
}
