package com.aeye.mifss.common.utils;

import cn.hutool.core.bean.BeanUtil;

import java.util.List;

public class AeyeBeanUtils {

    public static  <T> T copyBean(Object source, Class<T> target) throws Exception{
        if(source != null || target != null) {
          return  BeanUtil.toBean(source, target);
        }else {
            return null;
        }
    }

    public static void copyProperties(Object source, Object target) throws Exception{
        if(source != null || target != null) {
            BeanUtil.copyProperties(source, target);
        }
    }

    public static <T> List<T> copyBeanList(List<?> source, Class<T> target){
        if(source != null || target != null) {
            return BeanUtil.copyToList(source, target);
        }else{
            return null;
        }
    }

}
