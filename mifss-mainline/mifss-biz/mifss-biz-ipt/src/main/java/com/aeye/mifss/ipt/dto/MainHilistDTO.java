package com.aeye.mifss.ipt.dto;

import cn.hsa.hsaf.core.framework.validation.L2;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 重点药品库
 * 如果同时想发布到rpc，请拷贝至公共依赖模块，用于rpc之间数据交互
 * @author 沈兴平
 * @date 2023/07/31
 */
@Data
@ApiModel(value = "重点药品库",description = "沈兴平-2023/07/31")
public class MainHilistDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 医保目录编码
	 */
	@NotNull(message="hilistCode 不能为空", groups = {L2.class})
	@ApiModelProperty(value = "医保目录编码-主键")
	private String hilistCode;

	/**
	 * 医保目录名称
	 */
	@ApiModelProperty(value = "医保目录名称")
	private String hilistName;

	/**
	 * 数据创建时间
	 */
	@ApiModelProperty(value = "数据创建时间")
	private Date crteTime;

	/**
	 * 数据更新时间
	 */
	@ApiModelProperty(value = "数据更新时间")
	private Date updtTime;

	/**
	 * 有效标志
	 */
	@ApiModelProperty(value = "有效标志")
	private String valiFlag;

	/**
	 * 数据唯一记录号
	 */
	@ApiModelProperty(value = "数据唯一记录号")
	private String rid;

	/**
	 * 错误信息
	 */
	@ApiModelProperty(value = "导入失败原因说明")
	private String errorMessage;
}
