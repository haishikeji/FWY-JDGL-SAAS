package org.jeecg.modules.business.entity;

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
 * @Description: 小程序客房服务设置
 * @@author 品讯科技
 * @Date:   2023-03-11
 * @Version: V1.0
 */
@Data
@TableName("bus_param_xcx_room_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bus_param_xcx_room_info对象", description="小程序客房服务设置")
public class BusParamXcxRoom implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**关联租户*/
	@Excel(name = "关联租户", width = 15)
    @ApiModelProperty(value = "关联租户")
    private String tenantId;
	/**关联酒店*/
	@Excel(name = "关联酒店", width = 15)
    @ApiModelProperty(value = "关联酒店")
    private String hotelId;
	/**退房功能(0-关闭；1-开启)*/
	@Excel(name = "退房功能(0-关闭；1-开启)", width = 15)
    @ApiModelProperty(value = "退房功能(0-关闭；1-开启)")
    private Integer checkOut;
	/**退房方式(0-未结；1-结账)*/
	@Excel(name = "退房方式(0-未结；1-结账)", width = 15)
    @ApiModelProperty(value = "退房方式(0-未结；1-结账)")
    private Integer checkOutType;
	/**续住功能(0-关闭；1-开启)*/
	@Excel(name = "续住功能(0-关闭；1-开启)", width = 15)
    @ApiModelProperty(value = "续住功能(0-关闭；1-开启)")
    private Integer keepLive;
	/**留言功能(0-关闭；1-开启)*/
	@Excel(name = "留言功能(0-关闭；1-开启)", width = 15)
    @ApiModelProperty(value = "留言功能(0-关闭；1-开启)")
    private Integer messageLive;
	/**服务功能(0-关闭；1-开启)*/
	@Excel(name = "服务功能(0-关闭；1-开启)", width = 15)
    @ApiModelProperty(value = "服务功能(0-关闭；1-开启)")
    private Integer serviceLive;
	/**维修功能(0-关闭；1-开启)*/
	@Excel(name = "维修功能(0-关闭；1-开启)", width = 15)
    @ApiModelProperty(value = "维修功能(0-关闭；1-开启)")
    private Integer repairLive;
	/**删除状态(0-正常,1-已删除)*/
	@Excel(name = "删除状态(0-正常,1-已删除)", width = 15)
    @ApiModelProperty(value = "删除状态(0-正常,1-已删除)")
    @TableLogic
    private Integer delFlag;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
