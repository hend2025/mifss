package com.aeye.mifss.bas.biz.entity;

import com.aeye.mifss.common.annotation.CacheEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 场景监管参数实体
 */
@Data
@TableName("scen_para_a")
@CacheEntity(keyPrefix = "scen_para_a", cacheList = true)
public class ParaDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 参数名称
     */
    @TableId
    private String paraName;

    /**
     * 参数值
     */
    private String paraval;

    /**
     * 参数说明
     */
    private String paraDscr;

    /**
     * 归属系统
     */
    private String blngSys;

    /**
     * 参数中文名称
     */
    private String paraChnName;

    /**
     * 参数备注信息
     */
    private String paraRemarks;

    /**
     * 参数校验
     */
    private String paraChk;

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
