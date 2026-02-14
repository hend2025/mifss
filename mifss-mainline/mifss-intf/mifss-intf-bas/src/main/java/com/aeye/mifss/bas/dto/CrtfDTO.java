package com.aeye.mifss.bas.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 认证记录DTO
 */
@Data
public class CrtfDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 认证ID
     */
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
     * 认证时间
     */
    private Date crtfTime;

    /**
     * 认证结果，详见字典【SCEN_CRTF_RSLT】
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
}
