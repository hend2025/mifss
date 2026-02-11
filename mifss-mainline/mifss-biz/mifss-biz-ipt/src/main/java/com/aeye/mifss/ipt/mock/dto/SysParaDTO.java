package com.aeye.mifss.ipt.mock.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author shenxingping
 * @Date 2022/2/16
 */
@Data
@ApiModel(value = "场景监管参数",description = "沈兴平-2020/12/21")
public class SysParaDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 参数名称
	 */
	@ApiModelProperty(value = "参数名称")
	private String paraName;

	/**
	 * 参数值
	 */
	@ApiModelProperty(value = "参数值")
	private String paraval;

	/**
	 * 参数说明
	 */
	@ApiModelProperty(value = "参数说明")
	private String paraDscr;

}
