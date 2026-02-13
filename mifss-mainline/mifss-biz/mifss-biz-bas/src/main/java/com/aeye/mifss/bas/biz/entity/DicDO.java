package com.aeye.mifss.bas.biz.entity;

import com.aeye.mifss.common.annotation.CacheEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 场景字典代码表实体
 */
@Data
@TableName("scen_dic_a")
@CacheEntity(cacheList = true, groupField = "dicTypeCode")
public class DicDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据字典ID
     */
    @TableId
    private String dicId;

    /**
     * 字典值代码
     */
    private String dicCode;

    /**
     * 字典值名称
     */
    private String dicName;

    /**
     * 父级字典值代码
     */
    private String prntDicCode;

    /**
     * 字典类型代码
     */
    private String dicTypeCode;

    /**
     * 字典类型名称
     */
    private String dicTypeName;

    /**
     * 顺序号
     */
    private Integer seq;

    /**
     * 有效标志
     */
    private String valiFlag;

    /**
     * 数据唯一记录号
     */
    private String rid;

    /**
     * 数据创建时间
     */
    private Date crteTime;

    /**
     * 数据更新时间
     */
    private Date updtTime;
}
