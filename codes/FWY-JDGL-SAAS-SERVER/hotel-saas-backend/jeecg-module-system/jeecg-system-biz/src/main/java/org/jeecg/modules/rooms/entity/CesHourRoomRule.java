package org.jeecg.modules.rooms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 钟点房计费规则
 * </p>
 *
 * @author 品讯科技
 * @since 2023-03-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ces_hour_room_rule")
public class CesHourRoomRule extends Model<CesHourRoomRule> {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 酒店ID
     */
    private String hotelId;


    /**
     * 钟点房名称
     */
    private String hourRoomName;


    /**
     * 房型id
     */
    private String layoutIds;

    /**
     * 开房后时间（分钟）
     */
    private Integer afterOpenRoom;

    /**
     * 开房后多少分钟内按多少元收取
     */
    private BigDecimal afterOpenRoomPay;

    /**
     * 超时时间（分钟）周期
     */
    private Integer exceedTime;

    /**
     * 每到一个超时周期收取费用（元）
     */
    private BigDecimal exceedPay;

    /**
     * 不到超时周期的超时时间（分钟）
     */
    private Integer notReachExceedTime;

    /**
     * 不到超时周期的超时收费（元）
     */
    private BigDecimal notReachExceedTimePay;

    /**
     * 最多封顶花费（元）如果为0则不封顶
     */
    private BigDecimal maxExtraPay;

    /**
     * 超过多少分钟转为全天房（0分钟则视为不转为全天房）
     */
    private Integer limitTimeTransferAllDay;

    /**
     * 创建时间
     */
    @TableField("createAt")
    private LocalDateTime createAt;

    /**
     * 修改时间
     */
    @TableField("updateAt")
    private LocalDateTime updateAt;

    /**
     * 逻辑删除字段
     */
    private Boolean invalid;


    public static final String ID = "id";

    public static final String HOTEL_ID = "hotel_id";

    public static final String HOUR_ROOM_NAME = "hour_room_name";

    public static final String LAYOUT_IDS = "layout_ids";

    public static final String AFTER_OPEN_ROOM = "after_open_room";

    public static final String AFTER_OPEN_ROOM_PAY = "after_open_room_pay";

    public static final String EXCEED_TIME = "exceed_time";

    public static final String EXCEED_PAY = "exceed_pay";

    public static final String NOT_REACH_EXCEED_TIME = "not_reach_exceed_time";

    public static final String NOT_REACH_EXCEED_TIME_PAY = "not_reach_exceed_time_pay";

    public static final String MAX_EXTRA_PAY = "max_extra_pay";

    public static final String LIMIT_TIME_TRANSFER_ALL_DAY = "limit_time_transfer_all_day";

    public static final String CREATEAT = "createAt";

    public static final String UPDATEAT = "updateAt";

    public static final String INVALID = "invalid";

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
