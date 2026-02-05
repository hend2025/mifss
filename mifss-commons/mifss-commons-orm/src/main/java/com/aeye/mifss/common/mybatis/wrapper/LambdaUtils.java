package com.aeye.mifss.common.mybatis.wrapper;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lambda 表达式工具类
 * 用于从 Lambda 表达式中提取列名
 */
public class LambdaUtils {

    /**
     * 缓存已解析的列名，避免重复解析
     */
    private static final Map<Class<?>, SerializedLambda> LAMBDA_CACHE = new ConcurrentHashMap<>();

    /**
     * 从 Lambda 表达式中提取列名
     * 支持 getter 方法引用，如 Entity::getColumnName -> column_name
     *
     * @param func Lambda 表达式
     * @param <T>  实体类型
     * @return 数据库列名（下划线格式）
     */
    public static <T> String getColumn(SFunction<T, ?> func) {
        SerializedLambda lambda = getSerializedLambda(func);
        String methodName = lambda.getImplMethodName();
        return methodToColumn(methodName);
    }

    /**
     * 获取 SerializedLambda 对象
     */
    private static SerializedLambda getSerializedLambda(Serializable func) {
        Class<?> funcClass = func.getClass();
        return LAMBDA_CACHE.computeIfAbsent(funcClass, clazz -> {
            try {
                Method writeReplace = clazz.getDeclaredMethod("writeReplace");
                writeReplace.setAccessible(true);
                return (SerializedLambda) writeReplace.invoke(func);
            } catch (Exception e) {
                throw new RuntimeException("Failed to get SerializedLambda from " + clazz.getName(), e);
            }
        });
    }

    /**
     * 将 getter 方法名转换为数据库列名（驼峰转下划线）
     * getColumnName -> column_name
     * isActive -> active
     */
    private static String methodToColumn(String methodName) {
        String fieldName;
        if (methodName.startsWith("get")) {
            fieldName = methodName.substring(3);
        } else if (methodName.startsWith("is")) {
            fieldName = methodName.substring(2);
        } else {
            throw new IllegalArgumentException("Invalid getter method: " + methodName);
        }
        return camelToUnderline(fieldName);
    }

    /**
     * 驼峰转下划线
     * ColumnName -> column_name
     */
    private static String camelToUnderline(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
