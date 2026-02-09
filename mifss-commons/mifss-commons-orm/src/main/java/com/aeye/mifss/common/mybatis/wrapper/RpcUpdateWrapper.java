package com.aeye.mifss.common.mybatis.wrapper;

import java.io.Serializable;
import java.util.*;

/**
 * 可序列化的 RPC 更新条件包装器
 * 用于替代 MyBatis-Plus 的 UpdateWrapper/LambdaUpdateWrapper 在 RPC 传输中使用
 * 支持 Lambda 表达式风格的列名引用
 * 
 * @param <T> 实体类型
 */
public class RpcUpdateWrapper<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * WHERE 条件列表
     */
    private List<RpcCondition> conditions = new ArrayList<>();

    /**
     * SET 字段值映射
     */
    private Map<String, Object> setValues = new LinkedHashMap<>();

    /**
     * SET SQL 片段（用于复杂的 SET 语句，如 SET count = count + 1）
     */
    private List<String> setSqls;

    public RpcUpdateWrapper() {
    }

    // ==================== Lambda SET 操作 ====================

    /**
     * SET 字段值 (Lambda 版本)
     */
    public RpcUpdateWrapper<T> set(SFunction<T, ?> column, Object value) {
        this.setValues.put(LambdaUtils.getColumn(column), value);
        return this;
    }

    /**
     * 条件 SET 字段值 (Lambda 版本)
     */
    public RpcUpdateWrapper<T> set(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.setValues.put(LambdaUtils.getColumn(column), value);
        }
        return this;
    }

    // ==================== Lambda 条件操作 ====================

    /**
     * 等于 = (Lambda 版本)
     */
    public RpcUpdateWrapper<T> eq(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.eq(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcUpdateWrapper<T> eq(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.eq(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * 不等于 <> (Lambda 版本)
     */
    public RpcUpdateWrapper<T> ne(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.ne(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcUpdateWrapper<T> ne(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.ne(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * 大于 > (Lambda 版本)
     */
    public RpcUpdateWrapper<T> gt(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.gt(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcUpdateWrapper<T> gt(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.gt(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * 大于等于 >= (Lambda 版本)
     */
    public RpcUpdateWrapper<T> ge(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.ge(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcUpdateWrapper<T> ge(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.ge(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * 小于 < (Lambda 版本)
     */
    public RpcUpdateWrapper<T> lt(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.lt(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcUpdateWrapper<T> lt(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.lt(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * 小于等于 <= (Lambda 版本)
     */
    public RpcUpdateWrapper<T> le(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.le(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcUpdateWrapper<T> le(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.le(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * 模糊匹配 LIKE '%value%' (Lambda 版本)
     */
    public RpcUpdateWrapper<T> like(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.like(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcUpdateWrapper<T> like(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.like(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * 左模糊 LIKE '%value' (Lambda 版本)
     */
    public RpcUpdateWrapper<T> likeLeft(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.likeLeft(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcUpdateWrapper<T> likeLeft(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.likeLeft(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * 右模糊 LIKE 'value%' (Lambda 版本)
     */
    public RpcUpdateWrapper<T> likeRight(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.likeRight(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcUpdateWrapper<T> likeRight(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.likeRight(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * IN (value1, value2, ...) (Lambda 版本)
     */
    public RpcUpdateWrapper<T> in(SFunction<T, ?> column, Collection<?> values) {
        this.conditions.add(RpcCondition.in(LambdaUtils.getColumn(column), values));
        return this;
    }

    public RpcUpdateWrapper<T> in(boolean condition, SFunction<T, ?> column, Collection<?> values) {
        if (condition) {
            this.conditions.add(RpcCondition.in(LambdaUtils.getColumn(column), values));
        }
        return this;
    }

    /**
     * NOT IN (value1, value2, ...) (Lambda 版本)
     */
    public RpcUpdateWrapper<T> notIn(SFunction<T, ?> column, Collection<?> values) {
        this.conditions.add(RpcCondition.notIn(LambdaUtils.getColumn(column), values));
        return this;
    }

    public RpcUpdateWrapper<T> notIn(boolean condition, SFunction<T, ?> column, Collection<?> values) {
        if (condition) {
            this.conditions.add(RpcCondition.notIn(LambdaUtils.getColumn(column), values));
        }
        return this;
    }

    /**
     * BETWEEN value1 AND value2 (Lambda 版本)
     */
    public RpcUpdateWrapper<T> between(SFunction<T, ?> column, Object valueFrom, Object valueTo) {
        this.conditions.add(RpcCondition.between(LambdaUtils.getColumn(column), valueFrom, valueTo));
        return this;
    }

    public RpcUpdateWrapper<T> between(boolean condition, SFunction<T, ?> column, Object valueFrom, Object valueTo) {
        if (condition) {
            this.conditions.add(RpcCondition.between(LambdaUtils.getColumn(column), valueFrom, valueTo));
        }
        return this;
    }

    /**
     * IS NULL (Lambda 版本)
     */
    public RpcUpdateWrapper<T> isNull(SFunction<T, ?> column) {
        this.conditions.add(RpcCondition.isNull(LambdaUtils.getColumn(column)));
        return this;
    }

    public RpcUpdateWrapper<T> isNull(boolean condition, SFunction<T, ?> column) {
        if (condition) {
            this.conditions.add(RpcCondition.isNull(LambdaUtils.getColumn(column)));
        }
        return this;
    }

    /**
     * IS NOT NULL (Lambda 版本)
     */
    public RpcUpdateWrapper<T> isNotNull(SFunction<T, ?> column) {
        this.conditions.add(RpcCondition.isNotNull(LambdaUtils.getColumn(column)));
        return this;
    }

    public RpcUpdateWrapper<T> isNotNull(boolean condition, SFunction<T, ?> column) {
        if (condition) {
            this.conditions.add(RpcCondition.isNotNull(LambdaUtils.getColumn(column)));
        }
        return this;
    }

    // ==================== 字符串列名 SET 操作（保留向后兼容） ====================

    /**
     * SET 字段值 (字符串列名版本)
     */
    public RpcUpdateWrapper<T> set(String column, Object value) {
        this.setValues.put(column, value);
        return this;
    }

    public RpcUpdateWrapper<T> set(boolean condition, String column, Object value) {
        if (condition) {
            this.setValues.put(column, value);
        }
        return this;
    }

    /**
     * SET SQL 片段（用于复杂表达式，如 count = count + 1）
     */
    public RpcUpdateWrapper<T> setSql(String sql) {
        if (this.setSqls == null) {
            this.setSqls = new ArrayList<>();
        }
        this.setSqls.add(sql);
        return this;
    }

    public RpcUpdateWrapper<T> setSql(boolean condition, String sql) {
        if (condition) {
            if (this.setSqls == null) {
                this.setSqls = new ArrayList<>();
            }
            this.setSqls.add(sql);
        }
        return this;
    }

    // ==================== 字符串列名条件操作（保留向后兼容） ====================

    public RpcUpdateWrapper<T> eq(String column, Object value) {
        this.conditions.add(RpcCondition.eq(column, value));
        return this;
    }

    public RpcUpdateWrapper<T> eq(boolean condition, String column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.eq(column, value));
        }
        return this;
    }

    public RpcUpdateWrapper<T> ne(String column, Object value) {
        this.conditions.add(RpcCondition.ne(column, value));
        return this;
    }

    public RpcUpdateWrapper<T> ne(boolean condition, String column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.ne(column, value));
        }
        return this;
    }

    public RpcUpdateWrapper<T> gt(String column, Object value) {
        this.conditions.add(RpcCondition.gt(column, value));
        return this;
    }

    public RpcUpdateWrapper<T> gt(boolean condition, String column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.gt(column, value));
        }
        return this;
    }

    public RpcUpdateWrapper<T> ge(String column, Object value) {
        this.conditions.add(RpcCondition.ge(column, value));
        return this;
    }

    public RpcUpdateWrapper<T> ge(boolean condition, String column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.ge(column, value));
        }
        return this;
    }

    public RpcUpdateWrapper<T> lt(String column, Object value) {
        this.conditions.add(RpcCondition.lt(column, value));
        return this;
    }

    public RpcUpdateWrapper<T> lt(boolean condition, String column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.lt(column, value));
        }
        return this;
    }

    public RpcUpdateWrapper<T> le(String column, Object value) {
        this.conditions.add(RpcCondition.le(column, value));
        return this;
    }

    public RpcUpdateWrapper<T> le(boolean condition, String column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.le(column, value));
        }
        return this;
    }

    public RpcUpdateWrapper<T> like(String column, Object value) {
        this.conditions.add(RpcCondition.like(column, value));
        return this;
    }

    public RpcUpdateWrapper<T> like(boolean condition, String column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.like(column, value));
        }
        return this;
    }

    public RpcUpdateWrapper<T> in(String column, Collection<?> values) {
        this.conditions.add(RpcCondition.in(column, values));
        return this;
    }

    public RpcUpdateWrapper<T> in(boolean condition, String column, Collection<?> values) {
        if (condition) {
            this.conditions.add(RpcCondition.in(column, values));
        }
        return this;
    }

    public RpcUpdateWrapper<T> notIn(String column, Collection<?> values) {
        this.conditions.add(RpcCondition.notIn(column, values));
        return this;
    }

    public RpcUpdateWrapper<T> notIn(boolean condition, String column, Collection<?> values) {
        if (condition) {
            this.conditions.add(RpcCondition.notIn(column, values));
        }
        return this;
    }

    public RpcUpdateWrapper<T> between(String column, Object valueFrom, Object valueTo) {
        this.conditions.add(RpcCondition.between(column, valueFrom, valueTo));
        return this;
    }

    public RpcUpdateWrapper<T> between(boolean condition, String column, Object valueFrom, Object valueTo) {
        if (condition) {
            this.conditions.add(RpcCondition.between(column, valueFrom, valueTo));
        }
        return this;
    }

    public RpcUpdateWrapper<T> isNull(String column) {
        this.conditions.add(RpcCondition.isNull(column));
        return this;
    }

    public RpcUpdateWrapper<T> isNull(boolean condition, String column) {
        if (condition) {
            this.conditions.add(RpcCondition.isNull(column));
        }
        return this;
    }

    public RpcUpdateWrapper<T> isNotNull(String column) {
        this.conditions.add(RpcCondition.isNotNull(column));
        return this;
    }

    public RpcUpdateWrapper<T> isNotNull(boolean condition, String column) {
        if (condition) {
            this.conditions.add(RpcCondition.isNotNull(column));
        }
        return this;
    }

    // ==================== Getters and Setters ====================

    public List<RpcCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<RpcCondition> conditions) {
        this.conditions = conditions;
    }

    public Map<String, Object> getSetValues() {
        return setValues;
    }

    public void setSetValues(Map<String, Object> setValues) {
        this.setValues = setValues;
    }

    public List<String> getSetSqls() {
        return setSqls;
    }

    public void setSetSqls(List<String> setSqls) {
        this.setSqls = setSqls;
    }
}
