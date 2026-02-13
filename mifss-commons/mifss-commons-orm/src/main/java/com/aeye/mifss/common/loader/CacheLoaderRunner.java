package com.aeye.mifss.common.loader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 系统启动后异步加载缓存
 */
@Slf4j
@Component
public class CacheLoaderRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info(">>>>>>>>> [CacheLoaderRunner] Attempting to load cache asynchronously...");
        CacheRefreshUtil.refreshAllAsync();
    }
}
