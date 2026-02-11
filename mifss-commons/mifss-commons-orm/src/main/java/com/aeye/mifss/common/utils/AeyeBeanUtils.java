package com.aeye.mifss.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class AeyeBeanUtils {
    public static  <T> T copyBean(Object source, Class<T> target) throws Exception{
        T targetObj = target.newInstance();
        if(source != null) {
            BeanUtils.copyProperties(source, targetObj);
        }
        return targetObj;
    }

    public static void copyProperties(Object source, Object target) throws Exception{
        if(source != null || target != null) {
            BeanUtils.copyProperties(source, target);
        }
    }

    public static <T> List<T> copyBeanList(List<?> source, Class<T> target){
        String result = JSONObject.toJSONString(source);
        return (List<T>) JSONObject.parseArray(result, target);
    }
}
