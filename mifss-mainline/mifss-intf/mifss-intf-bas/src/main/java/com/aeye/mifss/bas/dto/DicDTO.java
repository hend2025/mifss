package com.aeye.mifss.bas.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 场景字典代码表 DTO
 */
@Data
public class DicDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据字典ID
     */
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
