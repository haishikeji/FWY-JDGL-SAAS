package org.jeecg.modules.wxuser.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: wx_user
 * @@author 品讯科技
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Data
@TableName("wx_user")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wx_user对象", description="wx_user")
public class WxUser implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**关联租户*/
	@Excel(name = "关联租户", width = 15)
    @ApiModelProperty(value = "关联租户")
    private String tenantId;
	/**应用ID*/
	@Excel(name = "应用ID", width = 15)
    @ApiModelProperty(value = "应用ID")
    private String appId;
	/**应用类型(1:小程序，2:公众号)*/
	@Excel(name = "应用类型(1:小程序，2:公众号)", width = 15)
    @ApiModelProperty(value = "应用类型(1:小程序，2:公众号)")
    private Integer appType;
	/**用户标识*/
	@Excel(name = "用户标识", width = 15)
    @ApiModelProperty(value = "用户标识")
    private String openId;
	/**会话密钥*/
	@Excel(name = "会话密钥", width = 15)
    @ApiModelProperty(value = "会话密钥")
    private String sessionKey;
	/**unionid*/
	@Excel(name = "unionid", width = 15)
    @ApiModelProperty(value = "unionid")
    private String unionId;
	/**关联用户表*/
	@Excel(name = "关联用户表id", width = 15)
    @ApiModelProperty(value = "关联用户表id")
    private String userId;
	/**头像*/
	@Excel(name = "头像", width = 15)
    @ApiModelProperty(value = "头像")
    private String avatar;
	/**昵称*/
	@Excel(name = "昵称", width = 15)
    @ApiModelProperty(value = "昵称")
    private String nickName;
	/**性别（1：男，2：女，0：未知）*/
	@Excel(name = "性别（1：男，2：女，0：未知）", width = 15)
    @ApiModelProperty(value = "性别（1：男，2：女，0：未知）")
    private Integer gender;
	/**城市*/
	@Excel(name = "城市", width = 15)
    @ApiModelProperty(value = "城市")
    private String city;
	/**省*/
	@Excel(name = "省", width = 15)
    @ApiModelProperty(value = "省")
    private String province;
	/**国家*/
	@Excel(name = "国家", width = 15)
    @ApiModelProperty(value = "国家")
    private String country;
	/**手机号码*/
	@Excel(name = "手机号码", width = 15)
    @ApiModelProperty(value = "手机号码")
    private String mobile;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
