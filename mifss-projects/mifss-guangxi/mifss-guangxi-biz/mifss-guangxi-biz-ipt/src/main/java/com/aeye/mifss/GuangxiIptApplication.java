package com.aeye.mifss;

import cn.hsa.ims.common.config.WildcardControllerFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({
        "classpath:config/adapt/rpc/generic/rpc.xml",
        "classpath:config/adapt/rpc/generic/provider.xml",
        "classpath:config/adapt/rpc/generic/consumer.xml"
})
@ComponentScan(basePackages = {"com.aeye.mifss", "cn.hsa.ims" }, excludeFilters = @ComponentScan.Filter(type = FilterType.CUSTOM, classes = WildcardControllerFilter.class ))
public class GuangxiIptApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuangxiIptApplication.class, args);
    }
}
