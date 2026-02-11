package com.aeye.mifss.ipt.mock.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单按钮
 * 如果同时想发布到rpc，请拷贝至公共依赖模块，用于rpc之间数据交互
 * @author 沈兴平
 * @date 2022/07/04
 */
@Data
@ApiModel(value = "菜单按钮",description = "沈兴平-2022/07/04")
public class MenuBtnDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 按钮ID
	 */
	@ApiModelProperty(value = "按钮ID-主键")
	private String menuBtnId;

	/**
	 * 按钮名称
	 */
	@ApiModelProperty(value = "按钮名称")
	private String menuBtnName;

	/**
	 * 按钮KEY
	 */
	@ApiModelProperty(value = "按钮KEY")
	private String menuBtnKey;

	/**
	 * 按钮URL
	 */
	@ApiModelProperty(value = "按钮URL")
	private String menuBtnUrl;

	/**
	 * 按钮是否显示
	 */
	@ApiModelProperty(value = "按钮是否显示")
	private String menuBtnShow;

	/**
	 * 菜单标题
	 */
	@ApiModelProperty(value = "菜单标题")
	private String menuName;

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


}
