package com.aeye.mifss.common.mybatis.wrapper;

/**
 * SQL 操作符枚举，用于 RPC 传输时指定查询条件类型
 */
public enum SqlOperator {
    /**
     * 等于 =
     */
    EQ,

    /**
     * 不等于 <>
     */
    NE,

    /**
     * 大于 >
     */
    GT,

    /**
     * 大于等于 >=
     */
    GE,

    /**
     * 小于 <
     */
    LT,

    /**
     * 小于等于 <=
     */
    LE,

    /**
     * 模糊匹配 LIKE '%value%'
     */
    LIKE,

    /**
     * 左模糊 LIKE '%value'
     */
    LIKE_LEFT,

    /**
     * 右模糊 LIKE 'value%'
     */
    LIKE_RIGHT,

    /**
     * 不模糊匹配 NOT LIKE '%value%'
     */
    NOT_LIKE,

    /**
     * IN (value1, value2, ...)
     */
    IN,

    /**
     * NOT IN (value1, value2, ...)
     */
    NOT_IN,

    /**
     * BETWEEN value1 AND value2
     */
    BETWEEN,

    /**
     * NOT BETWEEN value1 AND value2
     */
    NOT_BETWEEN,

    /**
     * IS NULL
     */
    IS_NULL,

    /**
     * IS NOT NULL
     */
    IS_NOT_NULL
}
