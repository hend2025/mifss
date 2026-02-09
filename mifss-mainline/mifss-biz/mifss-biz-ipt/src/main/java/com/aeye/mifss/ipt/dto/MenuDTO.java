package com.aeye.mifss.ipt.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单
 * 如果同时想发布到rpc，请拷贝至公共依赖模块，用于rpc之间数据交互
 * @author 沈兴平
 * @date 2021/12/20
 */
@Data
@ApiModel(value = "APP菜单",description = "沈兴平-2022/01/20")
public class MenuDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 菜单ID
	 */
	@ApiModelProperty(value = "菜单ID-主键")
	private String menuId;

	/**
	 * 父菜单ID，一级菜单为0
	 */
	@ApiModelProperty(value = "父菜单ID，一级菜单为0")
	private String parentId;

	/**
	 * 菜单标题
	 */
	@ApiModelProperty(value = "菜单标题")
	private String menuName;

	/**
	 * 菜单URL
	 */
	@ApiModelProperty(value = "菜单URL")
	private String menuUrl;

	/**
	 * 类型   0：模块   1：菜单   2：按钮
	 */
	@ApiModelProperty(value = "类型   0：模块   1：菜单   2：按钮")
	private Integer menuType;

	/**
	 * 菜单图标
	 */
	@ApiModelProperty(value = "菜单图标")
	private String menuIcon;

	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序")
	private Integer orderNum;

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
	 * 有效标志，详见字典【VALI_FLAG】
	 */
	@ApiModelProperty(value = "有效标志，详见字典【VALI_FLAG】")
	private Boolean valiFlag;

	/**
	 * 角色归属类型 1-两定机构用户 2-经办机构用户
	 *
	 */
	@ApiModelProperty(value = "角色归属类型 1-两定机构用户 2-经办机构用户")
	private String roleBlngType;
}