package cn.hsa.ims.common.utils;

import cn.hsa.ims.common.config.SFunction;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class AeyeReflectionUtil {

    private static Map<Type, Object> basicTypeMap = new HashMap();

    static {
        basicTypeMap.put(String.class, (Object)null);
        basicTypeMap.put(Number.class, (Object)null);
        basicTypeMap.put(Boolean.class, (Object)null);
        basicTypeMap.put(Character.class, (Object)null);
        basicTypeMap.put(Byte.class, (Object)null);
        basicTypeMap.put(Short.class, (Object)null);
        basicTypeMap.put(Integer.class, (Object)null);
        basicTypeMap.put(Long.class, (Object)null);
        basicTypeMap.put(Float.class, (Object)null);
        basicTypeMap.put(Double.class, (Object)null);
    }

    public static boolean isBasicType(Class typeClass) {
        if (typeClass.isPrimitive()) {
            return true;
        } else {
            return basicTypeMap.containsKey(typeClass);
        }
    }

    public static <T, R> String getFieldName(SFunction<T, R> function) {
        Field field = AeyeReflectionUtil.findField(function);
        return field.getName();
    }

    public static Object invokeMethod(Method method, Object obj) throws  IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        boolean accessible = method.isAccessible();
        method.setAccessible(Boolean.TRUE);
        Object result = method.invoke(obj);
        method.setAccessible(accessible);
        return result;
    }

    public static void setFieldValue(Field field, Object obj, Object val) throws IllegalArgumentException, IllegalAccessException{
        boolean accessible = field.isAccessible();
        field.setAccessible(Boolean.TRUE);
        field.set(obj, val);
        field.setAccessible(accessible);
    }

    public static Object getFieldValue(Field field, Object obj) throws IllegalArgumentException, IllegalAccessException{
        boolean accessible = field.isAccessible();
        field.setAccessible(Boolean.TRUE);
        Object result = field.get(obj);
        field.setAccessible(accessible);
        return result;
    }

    public static Field findField(SFunction<?, ?> function) {
        Field field = null;
        String fieldName = null;
        try {
            // 第1步 获取SerializedLambda
            Method method = function.getClass().getDeclaredMethod("writeReplace");
            SerializedLambda serializedLambda = (SerializedLambda) invokeMethod(method,  function);
            // 第2步 implMethodName 即为Field对应的Getter方法名
            String implMethodName = serializedLambda.getImplMethodName();
            if (implMethodName.startsWith("get") && implMethodName.length() > 3) {
                fieldName = Introspector.decapitalize(implMethodName.substring(3));

            } else if (implMethodName.startsWith("is") && implMethodName.length() > 2) {
                fieldName = Introspector.decapitalize(implMethodName.substring(2));
            } else if (implMethodName.startsWith("lambda$")) {
                throw new IllegalArgumentException("SFunction不能传递lambda表达式,只能使用方法引用");

            } else {
                throw new IllegalArgumentException(implMethodName + "不是Getter方法引用");
            }
            // 第3步 获取的Class是字符串，并且包名是“/”分割，需要替换成“.”，才能获取到对应的Class对象
            String declaredClass = serializedLambda.getImplClass().replace("/", ".");
            Class<?> aClass = Class.forName(declaredClass, false, ClassUtils.getDefaultClassLoader());

            // 第4步  Spring 中的反射工具类获取Class中定义的Field
            field = ReflectionUtils.findField(aClass, fieldName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // 第5步 如果没有找到对应的字段应该抛出异常
        if (field != null) {
            return field;
        }
        throw new NoSuchFieldError(fieldName);
    }

    public static Class<?> getSuperClassGenericType(final Class<?> clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
            if (index < params.length && index >= 0) {
                if (!(params[index] instanceof Class)) {
                    return Object.class;
                } else {
                    return (Class)params[index];
                }
            } else {
                return Object.class;
            }
        }
    }
}
