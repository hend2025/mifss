package cn.hsa.ims.common.cache;

import cn.hsa.ims.common.utils.AeyeSpringContextUtils;
import cn.hutool.core.util.StrUtil;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AeyeCacheManager {

    public static final String DEFAULT_CACHE_NAME = "defaultCache";

    public static final long EXPIRE = 1440 * 7;

    private static String applicationName;

    private static CacheManager cacheManager;

    private static RedisTemplate hsafRedisTemplate;

    static {
        cacheManager = AeyeSpringContextUtils.getBean("cacheManager");
        hsafRedisTemplate = AeyeSpringContextUtils.getBean("hsafRedisTemplate");
        applicationName = AeyeSpringContextUtils.getBean(Environment.class).getProperty("spring.application.name");
    }

    public static CacheManager getCacheManager() {
        return cacheManager;
    }

    public static RedisTemplate getRedisTemplate() {
        return hsafRedisTemplate;
    }

    public static void put(String cachePrex, String key, Object valueObj, Long minutes) {
        Assert.hasText(key, "缓存key不能为空!!!");
        hsafRedisTemplate.opsForValue().set(buildKey(cachePrex, key), valueObj, minutes, TimeUnit.MINUTES);
    }

    public static void put(String cachePrex, String key, Object valueObj) {
        put(cachePrex, key, valueObj, EXPIRE);
    }

    public static void put(String key, Object valueObj, Long minutes) {
        put(null, key, valueObj, minutes);
    }

    public static void put(String key, Object valueObj) {
        put(null, key, valueObj, EXPIRE);
    }

    public static boolean putList(String cachePrex, String key, List valueObj, Long minutes) {
        String objectKey = buildKey(cachePrex, key);
        hsafRedisTemplate.delete(objectKey);
        Long index = hsafRedisTemplate.opsForList().leftPushAll(objectKey, valueObj);
        if (index.intValue() == valueObj.size()) {
            return refresh(cachePrex, key, minutes);
        }
        return false;
    }

    public static boolean putList(String cachePrex, String key, List valueObj) {
        return putList(cachePrex, key, valueObj, EXPIRE);
    }

    public static boolean putList(String key, List valueObj, Long minutes) {
        return putList(null, key, valueObj, minutes);
    }

    public static boolean putList(String key, List valueObj) {
        return putList(null, key, valueObj, EXPIRE);
    }

    public static <T> T get(String cachePrex, String key, Class<T> clazz) {
        Assert.hasText(key, "缓存key不能为空!!!");
        Object obj = hsafRedisTemplate.opsForValue().get(buildKey(cachePrex, key));
        return (T) obj;
    }

    public static <T> T get(String key, Class<T> clazz) {
        return get(null, key, clazz);
    }

    public static <T> List<T> getList(String cachePrex, String key, Class<T> clazz) {
        List<T> list = (List<T>) getRedisTemplate().opsForList().range(buildKey(cachePrex, key), 0L, -1L);
        return list;
    }

    public static <T> List<T> getList(String key, Class<T> clazz) {
        return getList(null, key, clazz);
    }

    public static void remove(String cachePrex, String key) {
        Assert.hasText(key, "缓存key不能为空!!!");
        hsafRedisTemplate.delete(buildKey(cachePrex, key));
    }

    public static void remove(String key) {
        remove(null, key);
    }

    public static Boolean refresh(String cachePrex, String key, Long minutes) {
        Assert.hasText(key, "缓存key不能为空!!!");
        return hsafRedisTemplate.expire(buildKey(cachePrex, key), minutes, TimeUnit.MINUTES);
    }

    public static Boolean refresh(String key, Long minutes) {
        return refresh(null, key, minutes);
    }

    public static Boolean refresh(String key) {
        return refresh(null, key, EXPIRE);
    }

    private static String buildKey(String cachePrex, String key) {
        StringBuilder vk = new StringBuilder();
        if (StrUtil.isNotBlank(applicationName)) {
            vk.append(applicationName + ":");
        }
        if (StrUtil.isNotBlank(cachePrex)) {
            vk.append(cachePrex + ":");
        }
        vk.append(key);
        return vk.toString();
    }

}
