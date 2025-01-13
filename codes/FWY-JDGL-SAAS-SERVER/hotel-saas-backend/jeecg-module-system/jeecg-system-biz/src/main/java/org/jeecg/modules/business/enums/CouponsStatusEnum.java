package org.jeecg.modules.business.enums;

import org.jeecg.common.system.annotation.EnumDict;
import org.jeecg.common.system.vo.DictModel;

import java.util.ArrayList;
import java.util.List;


@EnumDict("couponsStatusEnum")
public enum CouponsStatusEnum {

    notClaimed(0, "未领取"),
    received(1, "已领取"),
    used(2, "已使用"),
    voided(3, "已作废");

    Integer key;

    String title;

    CouponsStatusEnum(Integer key, String title){
        this.key = key;
        this.title = title;
    }
    public Integer getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    /**
     * 获取字典数据
     * @return
     */
    public static List<DictModel> getDictList(){
        List<DictModel> list = new ArrayList<>();
        DictModel dictModel = null;
        for(CouponsStatusEnum e: CouponsStatusEnum.values()){
            dictModel = new DictModel();
            dictModel.setValue(e.key.toString());
            dictModel.setText(e.title);
            list.add(dictModel);
        }
        return list;
    }

    public static CouponsStatusEnum val(Integer key){
        for(CouponsStatusEnum bld: values()){
            if(bld.key == key){
                return bld;
            }
        }
        return null;
    }

}
