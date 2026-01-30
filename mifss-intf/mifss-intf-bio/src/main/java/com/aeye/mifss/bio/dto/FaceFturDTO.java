package com.aeye.mifss.bio.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 人脸特征 DTO
 */
@Data
public class FaceFturDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 人脸特征ID
     */
    private String faceBosgId;

    /**
     * 人员模板号
     */
    private String psnTmplNo;

    /**
     * 人脸图像URL
     */
    private String faceImgUrl;

    /**
     * 人脸特征数据
     */
    private byte[] faceFturData;

    /**
     * 人脸建模类型，详见字典【BOSG_CRTE_TMPL_TYPE】
     */
    private String faceCrteTmplType;

    /**
     * 算法版本号
     */
    private String algoVerId;

    /**
     * 人脸模板类型，详见字典【BOSG_TMPL_TYPE】
     */
    private String faceTmplType;

    /**
     * 人脸建模方式，详见字典【BOSG_CRTE_TMPL_WAY】
     */
    private String faceCrteTmplWay;

    /**
     * 人脸采集方式，详见字典【SCEN_CLCT_WAY】
     */
    private String faceClctWay;

    /**
     * 人脸采集时间
     */
    private Date faceClctTime;

    /**
     * 人脸采集设备类型
     */
    private String faceClctDevType;

    /**
     * 人脸采集设备编码
     */
    private String faceClctDevCode;

    /**
     * 人脸模板状态，详见字典【BOSG_TMPL_STAS】
     */
    private String faceTmplStas;

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
