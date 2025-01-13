package org.jeecg.modules.business.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.business.entity.BusDictItem;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.SysUserSysDepartModel;

/**
 * @Description: 业务字典配置表
 * @@author 品讯科技
 * @Date:   2023-03-06
 * @Version: V1.0
 */
public interface BusDictItemMapper extends BaseMapper<BusDictItem> {
//    /**
//     * 根据 dictId 查询字典值
//     *
//     * @param dictId
//     * @param dictItemParams 查询条件，可为空
//     * @param page 分页参数
//     * @return
//     */
//    List<BusDictItem> queryItemByParentId(IPage page, @Param("dictId") String dictId, @Param("dictItemParams") BusDictItem dictItemParams);
//
//    /**
//     * 查询 queryItemByParentId 的Total
//     *
//     * @param dictId
//     * @param dictItemParams 查询条件，可为空
//     * @return
//     */
//    Integer queryItemByParentIdTotal(@Param("dictId") String dictId, @Param("dictItemParams") BusDictItem dictItemParams);
}
