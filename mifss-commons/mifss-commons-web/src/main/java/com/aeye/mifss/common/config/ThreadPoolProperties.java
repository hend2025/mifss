package com.aeye.mifss.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties
public class ThreadPoolProperties {
    public Map<String, ThreadPoolAdapter> threadPool = new HashMap<>();
}
