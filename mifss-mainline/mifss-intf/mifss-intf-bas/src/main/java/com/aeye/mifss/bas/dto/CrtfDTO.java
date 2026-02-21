package com.aeye.mifss.bas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
     * 证件号码
     */
    private String certno;

    /**
     * 认证时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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

    // -----------------------------------------------------------------------------------------------------------------
    // 以下是业务扩展字段
    // -----------------------------------------------------------------------------------------------------------------

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 认证照Base64数据（仅用于认证接口传参，不存储）
     */
    private String imageData;

}
