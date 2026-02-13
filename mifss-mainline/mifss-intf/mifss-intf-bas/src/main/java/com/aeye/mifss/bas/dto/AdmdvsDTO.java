package com.aeye.mifss.bas.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 医保区划信息表 DTO
 */
@Data
public class AdmdvsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 医保区划
     */
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

    /**
     * 子节点列表
     */
    private java.util.List<AdmdvsDTO> children;
}
