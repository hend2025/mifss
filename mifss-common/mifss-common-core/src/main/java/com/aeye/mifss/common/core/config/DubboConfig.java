package com.aeye.mifss.common.core.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * Dubbo3 配置类
 * 通过配置 mifss.rpc.type=dubbo 启用 Dubbo
 * 仅在 classpath 中有 Dubbo 时生效
 */
@Configuration
@ConditionalOnClass(name = "org.apache.dubbo.config.spring.context.annotation.EnableDubbo")
@ConditionalOnProperty(name = "mifss.rpc.type", havingValue = "dubbo")
@EnableDubbo
public class DubboConfig {
    // Dubbo 配置通过 application.yml 管理
}
