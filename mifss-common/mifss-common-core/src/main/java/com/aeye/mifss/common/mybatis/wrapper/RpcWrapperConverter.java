package com.aeye.mifss.common.mybatis.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import java.util.List;
import java.util.Map;

/**
 * RPC Wrapper 转换工具类
 * 将可序列化的 RpcQueryWrapper/RpcUpdateWrapper 转换为 MyBatis-Plus 的
 * QueryWrapper/UpdateWrapper
 */
public class RpcWrapperConverter {

    /**
     * 将 RpcQueryWrapper 转换为 MyBatis-Plus QueryWrapper
     *
     * @param rpcWrapper RPC 查询条件包装器
     * @param <T>        实体类型
     * @return MyBatis-Plus QueryWrapper
     */
    public static <T> QueryWrapper<T> toQueryWrapper(RpcQueryWrapper<?> rpcWrapper) {
        if (rpcWrapper == null) {
            return null;
        }

        QueryWrapper<T> wrapper = new QueryWrapper<>();

        // 应用查询字段
        List<String> selectColumns = rpcWrapper.getSelectColumns();
        if (selectColumns != null && !selectColumns.isEmpty()) {
            wrapper.select(selectColumns.toArray(new String[0]));
        }

        // 应用条件
        applyConditions(wrapper, rpcWrapper.getConditions());

        // 应用排序
        List<OrderBy> orderBys = rpcWrapper.getOrderBys();
        if (orderBys != null) {
            for (OrderBy orderBy : orderBys) {
                if (orderBy.isAsc()) {
                    wrapper.orderByAsc(orderBy.getColumn());
                } else {
                    wrapper.orderByDesc(orderBy.getColumn());
                }
            }
        }

        // 应用分组
        List<String> groupByColumns = rpcWrapper.getGroupByColumns();
        if (groupByColumns != null && !groupByColumns.isEmpty()) {
            // MyBatis-Plus groupBy 签名是 groupBy(String, String...)
            String first = groupByColumns.get(0);
            if (groupByColumns.size() > 1) {
                String[] rest = groupByColumns.subList(1, groupByColumns.size()).toArray(new String[0]);
                wrapper.groupBy(first, rest);
            } else {
                wrapper.groupBy(first);
            }
        }

        // 应用 HAVING
        String havingSql = rpcWrapper.getHavingSql();
        if (havingSql != null && !havingSql.isEmpty()) {
            wrapper.having(havingSql);
        }

        // 应用末尾 SQL
        String lastSql = rpcWrapper.getLastSql();
        if (lastSql != null && !lastSql.isEmpty()) {
            wrapper.last(lastSql);
        }

        return wrapper;
    }

    /**
     * 将 RpcUpdateWrapper 转换为 MyBatis-Plus UpdateWrapper
     *
     * @param rpcWrapper RPC 更新条件包装器
     * @param <T>        实体类型
     * @return MyBatis-Plus UpdateWrapper
     */
    public static <T> UpdateWrapper<T> toUpdateWrapper(RpcUpdateWrapper<?> rpcWrapper) {
        if (rpcWrapper == null) {
            return null;
        }

        UpdateWrapper<T> wrapper = new UpdateWrapper<>();

        // 应用 SET 值
        Map<String, Object> setValues = rpcWrapper.getSetValues();
        if (setValues != null) {
            for (Map.Entry<String, Object> entry : setValues.entrySet()) {
                wrapper.set(entry.getKey(), entry.getValue());
            }
        }

        // 应用 SET SQL 片段
        List<String> setSqls = rpcWrapper.getSetSqls();
        if (setSqls != null) {
            for (String sql : setSqls) {
                wrapper.setSql(sql);
            }
        }

        // 应用条件
        applyConditions(wrapper, rpcWrapper.getConditions());

        return wrapper;
    }

    /**
     * 将条件应用到 Wrapper
     */
    private static <T> void applyConditions(
            com.baomidou.mybatisplus.core.conditions.AbstractWrapper<T, String, ?> wrapper,
            List<RpcCondition> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return;
        }

        for (RpcCondition condition : conditions) {
            String column = condition.getColumn();
            Object value = condition.getValue();
            SqlOperator operator = condition.getOperator();

            switch (operator) {
                case EQ:
                    wrapper.eq(column, value);
                    break;
                case NE:
                    wrapper.ne(column, value);
                    break;
                case GT:
                    wrapper.gt(column, value);
                    break;
                case GE:
                    wrapper.ge(column, value);
                    break;
                case LT:
                    wrapper.lt(column, value);
                    break;
                case LE:
                    wrapper.le(column, value);
                    break;
                case LIKE:
                    wrapper.like(column, value);
                    break;
                case LIKE_LEFT:
                    wrapper.likeLeft(column, value);
                    break;
                case LIKE_RIGHT:
                    wrapper.likeRight(column, value);
                    break;
                case NOT_LIKE:
                    wrapper.notLike(column, value);
                    break;
                case IN:
                    wrapper.in(column, condition.getValues());
                    break;
                case NOT_IN:
                    wrapper.notIn(column, condition.getValues());
                    break;
                case BETWEEN:
                    wrapper.between(column, value, condition.getValueTo());
                    break;
                case NOT_BETWEEN:
                    wrapper.notBetween(column, value, condition.getValueTo());
                    break;
                case IS_NULL:
                    wrapper.isNull(column);
                    break;
                case IS_NOT_NULL:
                    wrapper.isNotNull(column);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported operator: " + operator);
            }
        }
    }
}
