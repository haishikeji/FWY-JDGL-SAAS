package org.jeecg.modules.business.mapper;


import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.BusOrderFee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.vo.RoomOrderFeeVo;

import java.util.List;

/**
 * @Description: bus_order_fee
 * @@author 品讯科技
 * @Date:   2023-04-08
 * @Version: V1.0
 */
public interface BusOrderFeeMapper extends BaseMapper<BusOrderFee> {

    IPage<RoomOrderFeeVo> strikeBalancePage(Page<RoomOrderFeeVo> page,@Param("roomName") String roomName,
                                            @Param("preferentialStatus") Integer preferentialStatus,
                                            @Param("livingOrderIds") List<String> livingOrderIds,
                                            @Param("startTime") DateTime startTime,
                                            @Param("endTime") DateTime endTime);
}
