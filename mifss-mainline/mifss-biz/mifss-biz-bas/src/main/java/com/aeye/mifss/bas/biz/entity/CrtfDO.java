package com.aeye.mifss.bas.biz.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 认证记录实体
 */
@Data
@TableName("scen_crtf_d")
public class CrtfDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 认证ID
     */
    @TableId
    private String crtfId;

    /**
     * 人员模板号
     */
    private String psnTmplNo;

    /**
     * 人员姓名
     */
    private String psnName;

    /**
     * 证件号码
     */
    private String certno;

    /**
     * 认证时间
     */
    private Date crtfTime;

    /**
     * 认证结果
     */
    private String crtfRslt;

    /**
     * 医疗机构编码
     */
    private String medinsCode;

    /**
     * 医疗机构名称
     */
    private String medinsName;

    /**
     * 相似度
     */
    private BigDecimal crtfRate;

    /**
     * 认证照URL
     */
    private String crtfUrl;

    /**
     * 人脸模板照URL
     */
    private String faceImgUrl;

    /**
     * 数据创建时间
     */
    private Date crteTime;

    /**
     * 数据更新时间
     */
    private Date updtTime;

}
