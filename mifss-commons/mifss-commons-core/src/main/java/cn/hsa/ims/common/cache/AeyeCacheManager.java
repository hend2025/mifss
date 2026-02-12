package cn.hsa.ims.common.cache;

import cn.hsa.ims.common.utils.AeyeSpringContextUtils;
import cn.hsa.ims.common.utils.AeyeStringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class AeyeCacheManager {

    private static String cacheLevel;

    static {
        cacheLevel = AeyeSpringContextUtils.getBean(Environment.class).getProperty("cacheLevel");
        hsafRedisTemplate = AeyeSpringContextUtils.getBean("hsafRedisTemplate");
    }

    public static final String DEFAULT_CACHE_NAME = "defaultCache";

    public static final String LIKE_KEYWORD = "*";

    private static RedisTemplate hsafRedisTemplate;

    public AeyeCacheManager(){

    }

    /**
     * 新增TTL缓存对象
     * @param cacheName
     * @param cachePrex
     * @param valueKey
     * @param valueObj
     * @param minutes
     */
    public static void putTTL(String cacheName, String cachePrex, String valueKey, Object valueObj, Long minutes){
        Assert.hasText(valueKey, "缓存key不能为空!!!");
        cacheName = AeyeStringUtils.isNotEmpty(cacheName) ? cacheName : DEFAULT_CACHE_NAME;

        getRedisTemplate().opsForValue().set(buildKey(cacheName, cachePrex, valueKey), valueObj, minutes, TimeUnit.MINUTES);

    }

    /**
     * 新增TTL缓存对象
     * @param cachePrex
     * @param valueKey
     * @param valueObj
     * @param minutes
     */
    public static void putTTL(String cachePrex, String valueKey, Object valueObj, Long minutes){
        putTTL(DEFAULT_CACHE_NAME, cachePrex, valueKey, valueObj, minutes);
    }

    /**
     * 新增TTL缓存对象
     * @param valueKey
     * @param valueObj
     * @param minutes
     */
    public static void putTTL(String valueKey, Object valueObj, Long minutes){
        putTTL("", valueKey, valueObj, minutes);
    }

    public static void putTTLOfSecond(String cachePrex, String valueKey, Object valueObj, Long second){
        Assert.hasText(valueKey, "缓存key不能为空!!!");
        hsafRedisTemplate.opsForValue().set(buildKey(DEFAULT_CACHE_NAME, cachePrex, valueKey), valueObj, second, TimeUnit.SECONDS);
    }

    /**
     * 新增缓存对象
     * @param cacheName
     * @param cachePrex
     * @param valueKey
     * @param valueObj
     */
    public static void put(String cacheName, String cachePrex,String valueKey, Object valueObj){
        Assert.hasText(valueKey, "缓存key不能为空!!!");
        cacheName = AeyeStringUtils.isNotEmpty(cacheName) ? cacheName : DEFAULT_CACHE_NAME;
        getRedisTemplate().opsForValue().set(buildKey(cacheName, cachePrex, valueKey), valueObj);
    }

    private static final int BATCH_SIZE = 1000; // 每批处理量
    public static void put(String cacheName, String cachePrex, Map<String, Object> valueObj){
        Pipeline pipeline = getJedis().pipelined();
        int counter = 0;
        for (String valueKey : valueObj.keySet()) {
            Assert.hasText(valueKey, "缓存key不能为空!!!");
            cacheName = AeyeStringUtils.isNotEmpty(cacheName) ? cacheName : DEFAULT_CACHE_NAME;
            String key = buildKey(cacheName, cachePrex, valueKey);
            Object value = valueObj.get(valueKey);
            pipeline.set(key.getBytes(), getRedisTemplate().getValueSerializer().serialize(value));

            // 分批次提交
            if (++counter % BATCH_SIZE == 0) {
                pipeline.sync(); // 发送并清空Pipeline
                counter = 0;
            }
        }
        if (counter > 0) {
            pipeline.sync(); // 处理剩余数据
        }
    }

    /**
     * 新增缓存对象
     * @param cachePrex
     * @param valueKey
     * @param valueObj
     */
    public static void put(String cachePrex,String valueKey, Object valueObj){
        put(DEFAULT_CACHE_NAME, cachePrex, valueKey, valueObj);
    }

    public static void put(String cachePrex, Map<String, Object> valueObj){
        put(DEFAULT_CACHE_NAME, cachePrex, valueObj);
    }

    /**
     * 新增缓存对象
     * @param valueKey
     * @param valueObj
     */
    public static void put(String valueKey, Object valueObj){
        put("", valueKey, valueObj);
    }

    /**
     * 获取缓存过期时间-返回秒单位
     * @param cachePrex
     * @param valueKey
     */
    public static Long getExpire(String cachePrex, String valueKey){
        return getExpire(DEFAULT_CACHE_NAME, cachePrex, valueKey);
    }

    /**
     * 获取缓存过期时间-返回秒单位
     * @param cacheName
     * @param cachePrex
     * @param valueKey
     */
    public static Long getExpire(String cacheName, String cachePrex,String valueKey){
        String key = buildKey(cacheName, cachePrex, valueKey);
        return getRedisTemplate().getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 新增数组缓存
     * @param valueKey
     * @param valueObj
     */
    public static boolean putListTTL(String cacheName, String cachePrex,String valueKey, Object[] valueObj, Long minutes){
        String key = buildKey(cacheName, cachePrex, valueKey);
        getRedisTemplate().delete(key);
        Long index = getRedisTemplate().opsForList().leftPushAll(key, valueObj);
        if(index.intValue() == valueObj.length){
            return refreshTTL(cachePrex, valueKey, minutes);
        }
        return false;
    }

    /**
     * 新增数组缓存
     * @param valueKey
     * @param valueObj
     */
    public static boolean putListTTL(String cachePrex,String valueKey, Object[] valueObj, Long minutes){
        return putListTTL(DEFAULT_CACHE_NAME, cachePrex, valueKey, valueObj, minutes);
    }

    /**
     * 新增数组缓存
     * @param valueKey
     * @param valueObj
     */
    public static boolean putList(String cacheName, String cachePrex,String valueKey, Object[] valueObj){
        String key = buildKey(cacheName, cachePrex, valueKey);
        getRedisTemplate().delete(key);
        Long index = getRedisTemplate().opsForList().leftPushAll(key, valueObj);
        if(index.intValue() == valueObj.length){
            return true;
        }
        return false;
    }

    /**
     * 新增数组缓存
     * @param valueKey
     * @param valueObj
     */
    public static boolean putList(String cachePrex,String valueKey, Object[] valueObj){
        return putList(DEFAULT_CACHE_NAME, cachePrex, valueKey, valueObj);
    }

    /**
     * 获取数组缓存
     * @param cacheName
     * @param cachePrex
     * @param valueKey
     * @return
     */
    public static <T> List<T> getList(String cacheName, String cachePrex, String valueKey, Class<T> dataType){
        String key = buildKey(cacheName, cachePrex, valueKey);
        List data = getRedisTemplate().opsForList().range(key, 0, -1);
        if(dataType != null){
            return JSON.parseArray(JSON.toJSONString(data), dataType);
        }
        return data;
    }

    /**
     * 获取数组缓存
     * @param cachePrex
     * @param valueKey
     * @return
     */
    public static <T> List<T> getList(String cachePrex, String valueKey, Class<T> dataType){
        return getList(DEFAULT_CACHE_NAME, cachePrex, valueKey, dataType);
    }

    /**
     * 获取数组缓存
     * @param cachePrex
     * @param valueKey
     * @return
     */
    public static List getList(String cachePrex, String valueKey){
        return getList(DEFAULT_CACHE_NAME, cachePrex, valueKey, null);
    }

    /**
     * 模糊匹配key
     * @param pattern
     * @return 返回key集合
     */
    public static Set<String> likeKeys(String cacheName, String cachePrex, String pattern){
        String key = buildKeyNormal(cacheName, cachePrex, pattern);
        if(!key.endsWith(LIKE_KEYWORD)){
            key = key + LIKE_KEYWORD;
        }
        return hsafRedisTemplate.keys(key);
    }

    /**
     * 模糊匹配key
     * @param pattern
     * @return 返回key集合
     */
    public static Set<String> likeKeys(String cachePrex, String pattern){
        return likeKeys(DEFAULT_CACHE_NAME, cachePrex, pattern);
    }

    /**
     * 模糊匹配key
     * @param pattern
     * @return 返回key集合
     */
    public static Set<String> likeKeys(String pattern){
        return likeKeys("", pattern);
    }

    /**
     * 删除缓存对象
     * @param cacheName
     * @param cachePrex
     * @param valueKey
     */
    public static void remove(String cacheName, String cachePrex, String valueKey){
        cacheName = AeyeStringUtils.isNotEmpty(cacheName) ? cacheName : DEFAULT_CACHE_NAME;
        getRedisTemplate().delete(buildKey(cacheName, cachePrex, valueKey));
    }

    /**
     * 删除缓存对象
     * @param cachePrex
     * @param valueKey
     */
    public static void remove(String cachePrex, String valueKey){
        remove(DEFAULT_CACHE_NAME, cachePrex, valueKey);
    }

    /**
     * 删除缓存对象
     * @param valueKey
     */
    public static void remove(String valueKey){
        remove("", valueKey);
    }

    public static void clear(Set<String> keySet){
        // 将Set转换为List以便分批处理
        List<String> keyList = new ArrayList<>(keySet);

        // 每批处理1000个key
        int batchSize = 1000;
        for (int i = 0; i < keyList.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, keyList.size());
            List<String> subKeyList = keyList.subList(i, endIndex);
            hsafRedisTemplate.delete(subKeyList);
        }
    }

    /**
     * 获取缓存对象
     * @param cacheName
     * @param cachePrex
     * @param valueKey
     * @param classType
     * @param <T>
     * @return
     */
    public static <T> T get(String cacheName, String cachePrex, String valueKey, Class<T> classType){
        cacheName = AeyeStringUtils.isNotEmpty(cacheName) ? cacheName : DEFAULT_CACHE_NAME;
        Map obj = (Map)getRedisTemplate().opsForValue().get(buildKey(cacheName, cachePrex, valueKey));
        if(obj != null){
            return BeanUtil.mapToBean(obj, classType, true);
        }
        return null;
    }

    /**
     * 获取缓存对象
     * @param cachePrex
     * @param valueKey
     * @param classType
     * @param <T>
     * @return
     */
    public static <T> T get(String cachePrex, String valueKey, Class<T> classType){
        return get(DEFAULT_CACHE_NAME, cachePrex, valueKey, classType);
    }

    /**
     * 获取缓存对象
     * @param valueKey
     * @param classType
     * @param <T>
     * @return
     */
    public static <T> T get(String valueKey, Class<T> classType){
        return get("", valueKey, classType);
    }

    /**
     * 更新缓存失效时间
     * @param cachePrex
     * @param valueKey
     * @param minutes
     * @return
     */
    public static Boolean refreshTTL(String cachePrex, String valueKey, Long minutes){
        return getRedisTemplate().expire(buildKey("", cachePrex, valueKey), minutes, TimeUnit.MINUTES);
    }

    public static Boolean refreshTTLPub(String valueKey, Long minutes){
        return getRedisTemplate().expire(valueKey, minutes, TimeUnit.MINUTES);
    }


    /**
     * 更新缓存失效时间
     * @param valueKey
     * @param minutes
     * @return
     */
    public static Boolean refreshTTL(String valueKey, Long minutes){
        return refreshTTL("", valueKey, minutes);
    }

    /**
     * 获取redis缓存模板操作对象，操作针对redis特定的api
     * @return
     */
    public static RedisTemplate getRedisTemplate(){
        return hsafRedisTemplate;
    }
    public static JedisConnectionFactory getRedisFactory(){
        return (JedisConnectionFactory)getRedisTemplate().getConnectionFactory();
    }

    public static Jedis getJedis(){
        JedisConnection connection = (JedisConnection)getRedisFactory().getConnection();
        return connection.getJedis();
    }

    private static String buildKey(String cacheName, String cachePrex,String valueKey){
        StringBuilder vk = new StringBuilder();
        if(AeyeStringUtils.isNotBlank(cacheLevel)){
            vk.append(cacheLevel);
        }
        if(AeyeStringUtils.isNotBlank(cachePrex)){
            vk.append(cachePrex);
        }
        vk.append(valueKey);
        return vk.toString();
    }

    private static String buildKeyNormal(String cacheName, String cachePrex,String valueKey){
        StringBuilder vk = new StringBuilder();
        if(AeyeStringUtils.isNotBlank(cacheLevel)){
            vk.append(cacheLevel);
        }
        if(AeyeStringUtils.isNotBlank(cachePrex)){
            vk.append(cachePrex);
        }
        vk.append(valueKey);
        return vk.toString();
    }

    public static int cacheCount(String pattern){
        return cacheCount("", pattern);
    }

    public static int cacheCount(String cachePrex, String pattern){
        return cacheCount(DEFAULT_CACHE_NAME, cachePrex, pattern);
    }

    public static int cacheCount(String cacheName, String cachePrex, String pattern){
        String key = buildKeyNormal(cacheName, cachePrex, pattern);
        if(!key.endsWith(LIKE_KEYWORD)){
            key = key + LIKE_KEYWORD;
        }
        return getRedisTemplate().keys(key).size();
    }
}
