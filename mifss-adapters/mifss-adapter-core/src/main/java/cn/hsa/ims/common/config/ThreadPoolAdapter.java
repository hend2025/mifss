package cn.hsa.ims.common.config;

import lombok.Data;

@Data
public class ThreadPoolAdapter {
    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
    private int keepAliveSeconds;
    private String threadNamePrefix;
    private String desc;
}
