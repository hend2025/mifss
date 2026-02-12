
package com.aeye.mifss.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存实体注解
 * 标记在实体类上，表示该实体需要进行缓存处理
 * save/update/delete 同步更新redis
 * getById 优先从redis读取
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheEntity {

    /**
     * 缓存key前缀，默认使用类名
     */
    String keyPrefix() default "";

    /**
     * 过期时间（秒），默认 -1 不过期
     */
    long expire() default -1;
}
