package org.jeecg.modules.rooms.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.rooms.entity.CesHydropowerCoal;
import org.jeecg.modules.rooms.mapper.CesHydropowerCoalMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 水电煤设置 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-03-16
 */
@Service
public class CesHydropowerCoalServiceImpl extends ServiceImpl<CesHydropowerCoalMapper, CesHydropowerCoal> implements IService<CesHydropowerCoal> {

    @Resource
    private CesHydropowerCoalMapper cesHydropowerCoalMapper;


    public Result fetch(String hotelId){
        List<CesHydropowerCoal> list = cesHydropowerCoalMapper.selectList(Wrappers.<CesHydropowerCoal>lambdaQuery().eq(CesHydropowerCoal::getHotelId,hotelId).eq(CesHydropowerCoal::getInvalid,false));
        return Result.ok(list);
    }

    /**
     * 修改水电煤设置
     * @param hydropowerCoals
     * @return
     */
    public Result modify(List<CesHydropowerCoal> hydropowerCoals){
            saveOrUpdateBatch(hydropowerCoals);
            return Result.ok("成功!");
    }


}
