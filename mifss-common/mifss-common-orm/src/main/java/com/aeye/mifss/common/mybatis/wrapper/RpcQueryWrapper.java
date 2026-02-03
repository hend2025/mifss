package com.aeye.mifss.common.mybatis.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 可序列化的 RPC 查询条件包装器
 * 用于替代 MyBatis-Plus 的 QueryWrapper/LambdaQueryWrapper 在 RPC 传输中使用
 * 支持 Lambda 表达式风格的列名引用
 * 
 * @param <T> 实体类型
 */
public class RpcQueryWrapper<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 查询条件列表
     */
    private List<RpcCondition> conditions = new ArrayList<>();

    /**
     * 查询字段（SELECT 后的列名），为空表示查询所有字段
     */
    private List<String> selectColumns;

    /**
     * 排序条件
     */
    private List<OrderBy> orderBys;

    /**
     * 追加在 SQL 末尾的语句（如 LIMIT）
     */
    private String lastSql;

    /**
     * 分组字段
     */
    private List<String> groupByColumns;

    /**
     * HAVING 条件
     */
    private String havingSql;

    public RpcQueryWrapper() {
    }

    // ==================== 查询字段操作 ====================

    /**
     * 指定查询字段（字符串列名）
     */
    public RpcQueryWrapper<T> select(String... columns) {
        if (this.selectColumns == null) {
            this.selectColumns = new ArrayList<>();
        }
        this.selectColumns.addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * 指定查询字段（Lambda 表达式）
     */
    @SafeVarargs
    public final RpcQueryWrapper<T> select(SFunction<T, ?>... columns) {
        if (this.selectColumns == null) {
            this.selectColumns = new ArrayList<>();
        }
        for (SFunction<T, ?> column : columns) {
            this.selectColumns.add(LambdaUtils.getColumn(column));
        }
        return this;
    }

    // ==================== Lambda 条件操作 ====================

    /**
     * 等于 = (Lambda 版本)
     */
    public RpcQueryWrapper<T> eq(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.eq(LambdaUtils.getColumn(column), value));
        return this;
    }

    /**
     * 条件等于（Lambda 版本，当 condition 为 true 时才添加条件）
     */
    public RpcQueryWrapper<T> eq(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.eq(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * 不等于 <> (Lambda 版本)
     */
    public RpcQueryWrapper<T> ne(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.ne(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcQueryWrapper<T> ne(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.ne(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * 大于 > (Lambda 版本)
     */
    public RpcQueryWrapper<T> gt(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.gt(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcQueryWrapper<T> gt(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.gt(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * 大于等于 >= (Lambda 版本)
     */
    public RpcQueryWrapper<T> ge(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.ge(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcQueryWrapper<T> ge(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.ge(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * 小于 < (Lambda 版本)
     */
    public RpcQueryWrapper<T> lt(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.lt(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcQueryWrapper<T> lt(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.lt(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * 小于等于 <= (Lambda 版本)
     */
    public RpcQueryWrapper<T> le(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.le(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcQueryWrapper<T> le(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.le(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * 模糊匹配 LIKE '%value%' (Lambda 版本)
     */
    public RpcQueryWrapper<T> like(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.like(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcQueryWrapper<T> like(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.like(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * 左模糊 LIKE '%value' (Lambda 版本)
     */
    public RpcQueryWrapper<T> likeLeft(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.likeLeft(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcQueryWrapper<T> likeLeft(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.likeLeft(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * 右模糊 LIKE 'value%' (Lambda 版本)
     */
    public RpcQueryWrapper<T> likeRight(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.likeRight(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcQueryWrapper<T> likeRight(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.likeRight(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * 不模糊匹配 NOT LIKE '%value%' (Lambda 版本)
     */
    public RpcQueryWrapper<T> notLike(SFunction<T, ?> column, Object value) {
        this.conditions.add(RpcCondition.notLike(LambdaUtils.getColumn(column), value));
        return this;
    }

    public RpcQueryWrapper<T> notLike(boolean condition, SFunction<T, ?> column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.notLike(LambdaUtils.getColumn(column), value));
        }
        return this;
    }

    /**
     * IN (value1, value2, ...) (Lambda 版本)
     */
    public RpcQueryWrapper<T> in(SFunction<T, ?> column, Collection<?> values) {
        this.conditions.add(RpcCondition.in(LambdaUtils.getColumn(column), values));
        return this;
    }

    public RpcQueryWrapper<T> in(boolean condition, SFunction<T, ?> column, Collection<?> values) {
        if (condition) {
            this.conditions.add(RpcCondition.in(LambdaUtils.getColumn(column), values));
        }
        return this;
    }

    /**
     * NOT IN (value1, value2, ...) (Lambda 版本)
     */
    public RpcQueryWrapper<T> notIn(SFunction<T, ?> column, Collection<?> values) {
        this.conditions.add(RpcCondition.notIn(LambdaUtils.getColumn(column), values));
        return this;
    }

    public RpcQueryWrapper<T> notIn(boolean condition, SFunction<T, ?> column, Collection<?> values) {
        if (condition) {
            this.conditions.add(RpcCondition.notIn(LambdaUtils.getColumn(column), values));
        }
        return this;
    }

    /**
     * BETWEEN value1 AND value2 (Lambda 版本)
     */
    public RpcQueryWrapper<T> between(SFunction<T, ?> column, Object valueFrom, Object valueTo) {
        this.conditions.add(RpcCondition.between(LambdaUtils.getColumn(column), valueFrom, valueTo));
        return this;
    }

    public RpcQueryWrapper<T> between(boolean condition, SFunction<T, ?> column, Object valueFrom, Object valueTo) {
        if (condition) {
            this.conditions.add(RpcCondition.between(LambdaUtils.getColumn(column), valueFrom, valueTo));
        }
        return this;
    }

    /**
     * NOT BETWEEN value1 AND value2 (Lambda 版本)
     */
    public RpcQueryWrapper<T> notBetween(SFunction<T, ?> column, Object valueFrom, Object valueTo) {
        this.conditions.add(RpcCondition.notBetween(LambdaUtils.getColumn(column), valueFrom, valueTo));
        return this;
    }

    public RpcQueryWrapper<T> notBetween(boolean condition, SFunction<T, ?> column, Object valueFrom, Object valueTo) {
        if (condition) {
            this.conditions.add(RpcCondition.notBetween(LambdaUtils.getColumn(column), valueFrom, valueTo));
        }
        return this;
    }

    /**
     * IS NULL (Lambda 版本)
     */
    public RpcQueryWrapper<T> isNull(SFunction<T, ?> column) {
        this.conditions.add(RpcCondition.isNull(LambdaUtils.getColumn(column)));
        return this;
    }

    public RpcQueryWrapper<T> isNull(boolean condition, SFunction<T, ?> column) {
        if (condition) {
            this.conditions.add(RpcCondition.isNull(LambdaUtils.getColumn(column)));
        }
        return this;
    }

    /**
     * IS NOT NULL (Lambda 版本)
     */
    public RpcQueryWrapper<T> isNotNull(SFunction<T, ?> column) {
        this.conditions.add(RpcCondition.isNotNull(LambdaUtils.getColumn(column)));
        return this;
    }

    public RpcQueryWrapper<T> isNotNull(boolean condition, SFunction<T, ?> column) {
        if (condition) {
            this.conditions.add(RpcCondition.isNotNull(LambdaUtils.getColumn(column)));
        }
        return this;
    }

    // ==================== Lambda 排序操作 ====================

    /**
     * 升序排序 (Lambda 版本)
     */
    @SafeVarargs
    public final RpcQueryWrapper<T> orderByAsc(SFunction<T, ?>... columns) {
        if (this.orderBys == null) {
            this.orderBys = new ArrayList<>();
        }
        for (SFunction<T, ?> column : columns) {
            this.orderBys.add(OrderBy.asc(LambdaUtils.getColumn(column)));
        }
        return this;
    }

    /**
     * 降序排序 (Lambda 版本)
     */
    @SafeVarargs
    public final RpcQueryWrapper<T> orderByDesc(SFunction<T, ?>... columns) {
        if (this.orderBys == null) {
            this.orderBys = new ArrayList<>();
        }
        for (SFunction<T, ?> column : columns) {
            this.orderBys.add(OrderBy.desc(LambdaUtils.getColumn(column)));
        }
        return this;
    }

    // ==================== Lambda 分组操作 ====================

    /**
     * 分组 (Lambda 版本)
     */
    @SafeVarargs
    public final RpcQueryWrapper<T> groupBy(SFunction<T, ?>... columns) {
        if (this.groupByColumns == null) {
            this.groupByColumns = new ArrayList<>();
        }
        for (SFunction<T, ?> column : columns) {
            this.groupByColumns.add(LambdaUtils.getColumn(column));
        }
        return this;
    }

    // ==================== 字符串列名条件操作（保留向后兼容） ====================

    /**
     * 等于 = (字符串列名版本)
     */
    public RpcQueryWrapper<T> eq(String column, Object value) {
        this.conditions.add(RpcCondition.eq(column, value));
        return this;
    }

    public RpcQueryWrapper<T> eq(boolean condition, String column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.eq(column, value));
        }
        return this;
    }

    public RpcQueryWrapper<T> ne(String column, Object value) {
        this.conditions.add(RpcCondition.ne(column, value));
        return this;
    }

    public RpcQueryWrapper<T> ne(boolean condition, String column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.ne(column, value));
        }
        return this;
    }

    public RpcQueryWrapper<T> gt(String column, Object value) {
        this.conditions.add(RpcCondition.gt(column, value));
        return this;
    }

    public RpcQueryWrapper<T> gt(boolean condition, String column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.gt(column, value));
        }
        return this;
    }

    public RpcQueryWrapper<T> ge(String column, Object value) {
        this.conditions.add(RpcCondition.ge(column, value));
        return this;
    }

    public RpcQueryWrapper<T> ge(boolean condition, String column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.ge(column, value));
        }
        return this;
    }

    public RpcQueryWrapper<T> lt(String column, Object value) {
        this.conditions.add(RpcCondition.lt(column, value));
        return this;
    }

    public RpcQueryWrapper<T> lt(boolean condition, String column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.lt(column, value));
        }
        return this;
    }

    public RpcQueryWrapper<T> le(String column, Object value) {
        this.conditions.add(RpcCondition.le(column, value));
        return this;
    }

    public RpcQueryWrapper<T> le(boolean condition, String column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.le(column, value));
        }
        return this;
    }

    public RpcQueryWrapper<T> like(String column, Object value) {
        this.conditions.add(RpcCondition.like(column, value));
        return this;
    }

    public RpcQueryWrapper<T> like(boolean condition, String column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.like(column, value));
        }
        return this;
    }

    public RpcQueryWrapper<T> likeLeft(String column, Object value) {
        this.conditions.add(RpcCondition.likeLeft(column, value));
        return this;
    }

    public RpcQueryWrapper<T> likeLeft(boolean condition, String column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.likeLeft(column, value));
        }
        return this;
    }

    public RpcQueryWrapper<T> likeRight(String column, Object value) {
        this.conditions.add(RpcCondition.likeRight(column, value));
        return this;
    }

    public RpcQueryWrapper<T> likeRight(boolean condition, String column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.likeRight(column, value));
        }
        return this;
    }

    public RpcQueryWrapper<T> notLike(String column, Object value) {
        this.conditions.add(RpcCondition.notLike(column, value));
        return this;
    }

    public RpcQueryWrapper<T> notLike(boolean condition, String column, Object value) {
        if (condition) {
            this.conditions.add(RpcCondition.notLike(column, value));
        }
        return this;
    }

    public RpcQueryWrapper<T> in(String column, Collection<?> values) {
        this.conditions.add(RpcCondition.in(column, values));
        return this;
    }

    public RpcQueryWrapper<T> in(boolean condition, String column, Collection<?> values) {
        if (condition) {
            this.conditions.add(RpcCondition.in(column, values));
        }
        return this;
    }

    public RpcQueryWrapper<T> notIn(String column, Collection<?> values) {
        this.conditions.add(RpcCondition.notIn(column, values));
        return this;
    }

    public RpcQueryWrapper<T> notIn(boolean condition, String column, Collection<?> values) {
        if (condition) {
            this.conditions.add(RpcCondition.notIn(column, values));
        }
        return this;
    }

    public RpcQueryWrapper<T> between(String column, Object valueFrom, Object valueTo) {
        this.conditions.add(RpcCondition.between(column, valueFrom, valueTo));
        return this;
    }

    public RpcQueryWrapper<T> between(boolean condition, String column, Object valueFrom, Object valueTo) {
        if (condition) {
            this.conditions.add(RpcCondition.between(column, valueFrom, valueTo));
        }
        return this;
    }

    public RpcQueryWrapper<T> notBetween(String column, Object valueFrom, Object valueTo) {
        this.conditions.add(RpcCondition.notBetween(column, valueFrom, valueTo));
        return this;
    }

    public RpcQueryWrapper<T> notBetween(boolean condition, String column, Object valueFrom, Object valueTo) {
        if (condition) {
            this.conditions.add(RpcCondition.notBetween(column, valueFrom, valueTo));
        }
        return this;
    }

    public RpcQueryWrapper<T> isNull(String column) {
        this.conditions.add(RpcCondition.isNull(column));
        return this;
    }

    public RpcQueryWrapper<T> isNull(boolean condition, String column) {
        if (condition) {
            this.conditions.add(RpcCondition.isNull(column));
        }
        return this;
    }

    public RpcQueryWrapper<T> isNotNull(String column) {
        this.conditions.add(RpcCondition.isNotNull(column));
        return this;
    }

    public RpcQueryWrapper<T> isNotNull(boolean condition, String column) {
        if (condition) {
            this.conditions.add(RpcCondition.isNotNull(column));
        }
        return this;
    }

    // ==================== 字符串列名排序操作 ====================

    public RpcQueryWrapper<T> orderByAsc(String... columns) {
        if (this.orderBys == null) {
            this.orderBys = new ArrayList<>();
        }
        for (String column : columns) {
            this.orderBys.add(OrderBy.asc(column));
        }
        return this;
    }

    public RpcQueryWrapper<T> orderByDesc(String... columns) {
        if (this.orderBys == null) {
            this.orderBys = new ArrayList<>();
        }
        for (String column : columns) {
            this.orderBys.add(OrderBy.desc(column));
        }
        return this;
    }

    // ==================== 字符串列名分组操作 ====================

    public RpcQueryWrapper<T> groupBy(String... columns) {
        if (this.groupByColumns == null) {
            this.groupByColumns = new ArrayList<>();
        }
        this.groupByColumns.addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * HAVING 条件
     */
    public RpcQueryWrapper<T> having(String sql) {
        this.havingSql = sql;
        return this;
    }

    // ==================== 其他操作 ====================

    /**
     * 在 SQL 末尾追加语句
     */
    public RpcQueryWrapper<T> last(String lastSql) {
        this.lastSql = lastSql;
        return this;
    }

    // ==================== Getters and Setters ====================

    public List<RpcCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<RpcCondition> conditions) {
        this.conditions = conditions;
    }

    public List<String> getSelectColumns() {
        return selectColumns;
    }

    public void setSelectColumns(List<String> selectColumns) {
        this.selectColumns = selectColumns;
    }

    public List<OrderBy> getOrderBys() {
        return orderBys;
    }

    public void setOrderBys(List<OrderBy> orderBys) {
        this.orderBys = orderBys;
    }

    public String getLastSql() {
        return lastSql;
    }

    public void setLastSql(String lastSql) {
        this.lastSql = lastSql;
    }

    public List<String> getGroupByColumns() {
        return groupByColumns;
    }

    public void setGroupByColumns(List<String> groupByColumns) {
        this.groupByColumns = groupByColumns;
    }

    public String getHavingSql() {
        return havingSql;
    }

    public void setHavingSql(String havingSql) {
        this.havingSql = havingSql;
    }
}
