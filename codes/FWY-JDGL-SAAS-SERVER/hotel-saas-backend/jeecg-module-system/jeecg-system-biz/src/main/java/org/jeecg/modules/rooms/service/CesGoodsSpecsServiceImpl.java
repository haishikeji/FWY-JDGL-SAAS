package org.jeecg.modules.rooms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.rooms.entity.CesGoodsSpecs;
import org.jeecg.modules.rooms.mapper.CesGoodsSpecsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品规格存储表 服务实现类
 * </p>
 *
 * @author 品讯科技
 * @since 2023-03-13
 */
@Service
public class CesGoodsSpecsServiceImpl extends ServiceImpl<CesGoodsSpecsMapper, CesGoodsSpecs> implements IService<CesGoodsSpecs> {

    public boolean saveBatchSpecs(List<CesGoodsSpecs> specsList){

        return false;
    }
}
