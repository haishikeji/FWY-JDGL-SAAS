package org.jeecg.modules.fw.service;

import org.jeecg.modules.fw.entity.FwRoomExamine;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: fw_room_examine
 * @@author 品讯科技
 * @Date:   2023-05-23
 * @Version: V1.0
 */
public interface IFwRoomExamineService extends IService<FwRoomExamine> {

    List<String> getExamineRoom(String hotel);
}
