
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
     * 过期时间（秒），默认 -1 不过期
     */
    long expire() default -1;

    /**
     * 是否缓存全量列表，默认不缓存
     * 对于数据量较大的表（如设备、机构），建议关闭列表缓存，只缓存单条记录
     */
    boolean cacheList() default false;

    /**
     * 分组字段，如果不为空，则按该字段值分组缓存列表
     * 例如：字典表按字典类型（dicTypeCode）分组
     */
    String groupField() default "";

}
