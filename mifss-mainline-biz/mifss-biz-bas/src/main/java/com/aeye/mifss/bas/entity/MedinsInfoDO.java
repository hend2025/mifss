package com.aeye.mifss.bas.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 机构信息实体
 */
@Data
@TableName("scen_medins_set_c")
public class MedinsInfoDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 医疗机构编码
     */
    @TableId
    private String medinsCode;

    /**
     * 医疗机构名称
     */
    private String medinsName;

    /**
     * 医疗机构简称
     */
    private String medinsAbbr;

    /**
     * 医疗机构管理码
     */
    private String medinsMgtcode;

    /**
     * 医疗机构分类代码
     */
    private String medinsTypeCode;

    /**
     * 上级医疗机构编码
     */
    private String prntMedinsCode;

    /**
     * 定点医疗服务机构类型
     */
    private String fixmedinsType;

    /**
     * 医疗机构等级
     */
    private String medinslv;

    /**
     * 医疗机构性质
     */
    private String medinsNatu;

    /**
     * 联系地址
     */
    private String addr;

    /**
     * 经度
     */
    private String lnt;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 医保区划
     */
    private String admdvs;

    /**
     * 有效标志
     */
    private String valiFlag;

    /**
     * 数据创建时间
     */
    private Date crteTime;

    /**
     * 数据更新时间
     */
    private Date updtTime;
}
