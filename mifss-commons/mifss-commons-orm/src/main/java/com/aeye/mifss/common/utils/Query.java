package com.aeye.mifss.common.utils;

import cn.hsa.ims.common.utils.AeyePageInfo;
import cn.hutool.core.util.StrUtil;
import com.aeye.mifss.common.xss.SqlFilter;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 沈兴平
 * @date 2020/12/21
 */
public class Query<T> {

    public static final String COL_SPLIT_KEY = "_";

    public IPage<T> getPage(AeyePageInfo pageInfo)throws Exception{
        return getPage(pageInfo, null);
    }

    public IPage<T> getPage(AeyePageInfo pageInfo, String countId)throws Exception{
        //分页参数
        long curPage = 1;
        long limit = 10;

        if(pageInfo.getPageNum() > 0){
            curPage = pageInfo.getPageNum();
        }
        if(pageInfo.getPageSize() > 0){
            limit = pageInfo.getPageSize();
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);
        page.setSearchCount(pageInfo.isSearchCount());
        page.setCountId(countId);
        List<OrderItem> orders = new ArrayList<>();
        page.setOrders(orders);
        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String orderField = SqlFilter.sqlInject(pageInfo.getOrderField());
        String order = pageInfo.getOrderType();

        //前端字段排序
        if(StrUtil.isNotEmpty(orderField) && StrUtil.isNotEmpty(order)){
            if(Constant.ASC.equalsIgnoreCase(order)) {
                orders.add(new OrderItem(orderField, true));
                return page;
            }else {
                orders.add(new OrderItem(orderField, false));
                return page;
            }
        }

        return page;
    }

    /**
     * 根据对象属性名转换列名，避免硬编码列名
     * @param fieldName
     * @return
     */
    public static String fieldToColumn(Class classType, final String fieldName){
        List<Field> fields = ReflectionKit.getFieldList(classType);
        for(Field field : fields){
            if(field.getName().equals(fieldName)){
                TableField tableField = field.getAnnotation(TableField.class);
                if(tableField != null){
                    //实体属性存在注解，可能存在映射差异化
                    String column = tableField.value();
                    if(StrUtil.isNotBlank(column)){
                        //存在映射差异化
                        return column;
                    }
                }
                return StringUtils.camelToUnderline(fieldName);
            }
        }
        return fieldName;
    }

    /**
     * 转换分页对象中分页字段名为列名
     * @param pageInfo
     * @return
     */
    public static AeyePageInfo convertSortFieldToCol(Class classType,AeyePageInfo pageInfo){
        if(StrUtil.isNotBlank(pageInfo.getOrderField()) && !pageInfo.getOrderField().contains(COL_SPLIT_KEY)){
            pageInfo.setOrderField(fieldToColumn(classType, pageInfo.getOrderField()));
        }
        return pageInfo;
    }



    /**
     * 获取对象主键值，用到了mybatisPlus工具，取了主键再反射取值
     * @param entity
     * @return
     */
    public static Object getPrimaryKeyVal(Object entity){
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "错误: 不能够执行. 因为不能找到主键配置在实体类!", new Object[0]);
        Object idVal = ReflectionKit.getFieldValue(entity, keyProperty);
        return idVal;
    }
    /**
     * 获取对象主键字段
     * @param cla
     * @return
     */
    public static String getPrimaryKeyField(Class cla){
        TableInfo tableInfo = TableInfoHelper.getTableInfo(cla);
        return tableInfo.getKeyProperty();
    }

    /**
     * 获取对象指定字段值，用到了mybatisPlus工具
     * @param entity
     * @return
     */
    public static String getAppointValue(String field,Object entity){
        String keyProperty = field;
        Assert.notEmpty(keyProperty, "错误: 不能够执行. 属性字段未定义!", new Object[0]);
        Object val = ReflectionKit.getFieldValue(entity, keyProperty);
        return val != null ? val.toString() : null;
    }

    /**
     * 计算分页总数
     * @param total 总条目
     * @param pageSize 每页显示数
     * @return
     */
    public static int countPageNum(int total, int pageSize){
        int totalPages = total / pageSize;
        if (total % pageSize != 0){
            totalPages ++;
        }
        return totalPages;
    }
    public static long countPageNum(long total, long pageSize){
        long totalPages = total / pageSize;
        if (total % pageSize != 0){
            totalPages ++;
        }
        return totalPages;
    }
}
