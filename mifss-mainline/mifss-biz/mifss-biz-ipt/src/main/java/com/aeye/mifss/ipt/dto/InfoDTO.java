package com.aeye.mifss.ipt.dto;

import cn.hsa.hsaf.core.framework.validation.L2;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 消息中心表
 * 如果同时想发布到rpc，请拷贝至公共依赖模块，用于rpc之间数据交互
 * @author 沈兴平
 * @date 2023/04/03
 */
@Data
@ApiModel(value = "消息中心表",description = "沈兴平-2023/04/03")
public class InfoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@NotNull(message="id 不能为空", groups = {L2.class})
	@ApiModelProperty(value = "主键-主键")
	private String id;

	/**
	 * 标题
	 */
	@ApiModelProperty(value = "标题")
	private String title;

	/**
	 * 内容
	 */
	@ApiModelProperty(value = "内容")
	private String content;

	/**
	 * 消息类型
	 */
	@ApiModelProperty(value = "消息类型")
	private String msgType;

	/**
	 * 消息等级
	 */
	@ApiModelProperty(value = "消息等级")
	private String msgLevel;

	/**
	 * 处理页面URL
	 */
	@ApiModelProperty(value = "处理页面URL")
	private String handleUrl;

	/**
	 * 处理状态（1.已处理，0.未处理）
	 */
	@ApiModelProperty(value = "处理状态（1.已处理，0.未处理）")
	private Boolean handleStats;

	/**
	 * 处理人
	 */
	@ApiModelProperty(value = "处理人")
	private String handleUser;

	/**
	 * 已读标记（1.已读，0.未读）
	 */
	@ApiModelProperty(value = "已读标记（1.已读，0.未读）")
	private String isRead;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date crteTime;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	private Date updtTime;

	/**
	 * 医保区划
	 */
	@ApiModelProperty(value = "医保区划")
	private String admdvs;

	/**
	 * 关联表名
	 */
	@ApiModelProperty(value = "关联表名")
	private String asocTabname;

	/**
	 * 关联表主键ID
	 */
	@ApiModelProperty(value = "关联表主键ID")
	private String asocTabPkId;


	@ApiModelProperty(value = "开始时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date beginTime;
	@ApiModelProperty(value = "结束时间")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date endTime;
}
