package com.aeye.mifss.bas.biz.entity;

import com.aeye.mifss.common.annotation.CacheEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 医保区划信息表实体
 */
@Data
@TableName("scen_admdvs_info_b")
@CacheEntity(keyPrefix = "scen_admdvs_info_b")
public class AdmdvsDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 医保区划
     */
    @TableId
    private String admdvs;

    /**
     * 医保区划名称
     */
    private String admdvsName;

    /**
     * 上级医保区划
     */
    private String prntAdmdvs;

    /**
     * 医保区划级别
     */
    private String admdvsLv;

    /**
     * 经纬度
     */
    private String latlnt;

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
