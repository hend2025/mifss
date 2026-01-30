package com.aeye.mifss.bas.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 人员信息 DTO
 */
@Data
public class PsnInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 人员模板号
     */
    private String psnTmplNo;

    /**
     * 数据中台人员编号
     */
    private String psnNo;

    /**
     * 人员姓名
     */
    private String psnName;

    /**
     * 个人证件类型
     */
    private String psnCertType;

    /**
     * 证件号码
     */
    private String certno;

    /**
     * 出生日期
     */
    private Date brdy;

    /**
     * 性别
     */
    private String gend;

    /**
     * 个人图像地址
     */
    private String psnImg;

    /**
     * 头像照
     */
    private String headPic;

    /**
     * 手机号码
     */
    private String mob;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 险种
     */
    private String insutype;

    /**
     * 医保区划
     */
    private String admdvs;

    /**
     * 数据创建时间
     */
    private Date crteTime;

    /**
     * 数据更新时间
     */
    private Date updtTime;
}
