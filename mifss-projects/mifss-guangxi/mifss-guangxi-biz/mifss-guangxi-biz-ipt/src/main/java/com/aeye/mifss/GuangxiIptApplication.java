package com.aeye.mifss;

import cn.hsa.ims.common.config.WildcardControllerFilter;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableDubbo
@ComponentScan(basePackages = {"com.aeye.mifss"}, excludeFilters = @ComponentScan.Filter(type = FilterType.CUSTOM, classes = WildcardControllerFilter.class ))
public class GuangxiIptApplication {
    public static void main(String[] args) {
        SpringApplication.run(GuangxiIptApplication.class, args);
    }
}
