package com.aeye.mifss.common.mybatis.wrapper;

import java.io.Serializable;
import java.util.Collection;

/**
 * 单个查询条件，可通过 RPC 序列化传输
 */
public class RpcCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字段名（数据库列名）
     */
    private String column;

    /**
     * SQL 操作符
     */
    private SqlOperator operator;

    /**
     * 单个值（用于 EQ, NE, GT, GE, LT, LE, LIKE 等）
     */
    private Object value;

    /**
     * 多个值（用于 IN, NOT_IN）
     */
    private Collection<?> values;

    /**
     * BETWEEN 的第二个值
     */
    private Object valueTo;

    public RpcCondition() {
    }

    public RpcCondition(String column, SqlOperator operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    public RpcCondition(String column, SqlOperator operator, Collection<?> values) {
        this.column = column;
        this.operator = operator;
        this.values = values;
    }

    public RpcCondition(String column, SqlOperator operator, Object valueFrom, Object valueTo) {
        this.column = column;
        this.operator = operator;
        this.value = valueFrom;
        this.valueTo = valueTo;
    }

    // ==================== Static Factory Methods ====================

    public static RpcCondition eq(String column, Object value) {
        return new RpcCondition(column, SqlOperator.EQ, value);
    }

    public static RpcCondition ne(String column, Object value) {
        return new RpcCondition(column, SqlOperator.NE, value);
    }

    public static RpcCondition gt(String column, Object value) {
        return new RpcCondition(column, SqlOperator.GT, value);
    }

    public static RpcCondition ge(String column, Object value) {
        return new RpcCondition(column, SqlOperator.GE, value);
    }

    public static RpcCondition lt(String column, Object value) {
        return new RpcCondition(column, SqlOperator.LT, value);
    }

    public static RpcCondition le(String column, Object value) {
        return new RpcCondition(column, SqlOperator.LE, value);
    }

    public static RpcCondition like(String column, Object value) {
        return new RpcCondition(column, SqlOperator.LIKE, value);
    }

    public static RpcCondition likeLeft(String column, Object value) {
        return new RpcCondition(column, SqlOperator.LIKE_LEFT, value);
    }

    public static RpcCondition likeRight(String column, Object value) {
        return new RpcCondition(column, SqlOperator.LIKE_RIGHT, value);
    }

    public static RpcCondition notLike(String column, Object value) {
        return new RpcCondition(column, SqlOperator.NOT_LIKE, value);
    }

    public static RpcCondition in(String column, Collection<?> values) {
        return new RpcCondition(column, SqlOperator.IN, values);
    }

    public static RpcCondition notIn(String column, Collection<?> values) {
        return new RpcCondition(column, SqlOperator.NOT_IN, values);
    }

    public static RpcCondition between(String column, Object valueFrom, Object valueTo) {
        return new RpcCondition(column, SqlOperator.BETWEEN, valueFrom, valueTo);
    }

    public static RpcCondition notBetween(String column, Object valueFrom, Object valueTo) {
        return new RpcCondition(column, SqlOperator.NOT_BETWEEN, valueFrom, valueTo);
    }

    public static RpcCondition isNull(String column) {
        return new RpcCondition(column, SqlOperator.IS_NULL, null);
    }

    public static RpcCondition isNotNull(String column) {
        return new RpcCondition(column, SqlOperator.IS_NOT_NULL, null);
    }

    // ==================== Getters and Setters ====================

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public SqlOperator getOperator() {
        return operator;
    }

    public void setOperator(SqlOperator operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Collection<?> getValues() {
        return values;
    }

    public void setValues(Collection<?> values) {
        this.values = values;
    }

    public Object getValueTo() {
        return valueTo;
    }

    public void setValueTo(Object valueTo) {
        this.valueTo = valueTo;
    }
}
