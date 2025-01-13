package org.jeecg.modules.rooms.DTO;

import lombok.Data;
import org.jeecg.modules.rooms.entity.CesRooms;

import java.util.List;

@Data
public class BatchBuildFloorRoomDto {

       private String floorName;

       private List<CesRooms> children;
}
