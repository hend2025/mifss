package com.aeye.mifss.common.mybatis.wrapper;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 支持序列化的 Function 接口
 * 用于 Lambda 表达式从而自动推断列名
 * 
 * @param <T> 实体类型
 * @param <R> 返回值类型
 */
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {
}
