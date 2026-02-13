package com.aeye.mifss.common.loader;

import com.aeye.mifss.common.mybatis.service.LocalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 缓存刷新工具类
 */
@Slf4j
@Component
public class CacheRefreshUtil {

    private static RedisTemplate<String, Object> redisTemplate;
    private static ApplicationContext applicationContext;

    @Resource
    private RedisTemplate<String, Object> redisTemplateBean;

    @Resource
    private ApplicationContext applicationContextBean;

    @PostConstruct
    public void init() {
        redisTemplate = redisTemplateBean;
        applicationContext = applicationContextBean;
    }

    /**
     * 异步刷新所有缓存
     */
    public static void refreshAllAsync() {
        CompletableFuture.runAsync(CacheRefreshUtil::refreshAll);
    }

    /**
     * 同步刷新所有缓存（带分布式锁）
     */
    public static void refreshAll() {
        // 缓存加载锁Key
        String lockKey = "cache:loading:lock";
        try {
            // 尝试获取分布式锁，过期时间5分钟，使用RedisCallback以兼容旧版Spring Data Redis
            Boolean success = redisTemplate
                    .execute((org.springframework.data.redis.core.RedisCallback<Boolean>) connection -> {
                        byte[] keyBytes = redisTemplate.getStringSerializer().serialize(lockKey);
                        byte[] valueBytes = redisTemplate.getStringSerializer().serialize("LOCKED");
                        // 原子操作: SET key value EX 300 NX
                        return connection.set(keyBytes, valueBytes,
                                org.springframework.data.redis.core.types.Expiration.from(5, TimeUnit.MINUTES),
                                org.springframework.data.redis.connection.RedisStringCommands.SetOption.ifAbsent());
                    });

            if (Boolean.TRUE.equals(success)) {
                log.info(">>>>>>>>> [CacheRefreshUtil] 获取分布式锁成功，开始加载缓存...");
                try {
                    Map<String, LocalService> serviceMap = applicationContext.getBeansOfType(LocalService.class);
                    for (LocalService service : serviceMap.values()) {
                        try {
                            service.reloadCache();
                            log.info(">>>>>>>>> [CacheRefreshUtil] 加载服务缓存成功: {}", service.getClass().getSimpleName());
                        } catch (Exception e) {
                            log.error(">>>>>>>>> [CacheRefreshUtil] 加载服务缓存失败: {}",
                                    service.getClass().getSimpleName(), e);
                        }
                    }
                } finally {
                    // 释放锁
                    redisTemplate.delete(lockKey);
                }
                log.info(">>>>>>>>> [CacheRefreshUtil] 缓存加载完成。");
            } else {
                log.info(">>>>>>>>> [CacheRefreshUtil] 锁已被其他节点持有，跳过缓存加载。");
            }
        } catch (Exception e) {
            log.error(">>>>>>>>> [CacheRefreshUtil] 获取锁或加载缓存异常", e);
        }
    }
}
