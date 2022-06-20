package com.github.cuteluobo.service;

import com.github.cuteluobo.pojo.RollResultData;
import com.github.cuteluobo.pojo.RollUnit;

/**
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public interface ExpandRollService extends RollService {
    /**
     * 抽取指定对象(新式神活动)
     * @param rollUnit 指定概率UP对象
     * @param fullBuff 是否启用全图鉴加成
     * @return 抽取结果
     */
    public RollResultData rollTextForSpecifyUnit(RollUnit rollUnit, Boolean fullBuff);


    /**
     * 抽取指定式神 （普通抽卡）
     * @param rollUnit 指定抽卡对象
     * @param up 是否启用UP加成
     * @return 抽取结果
     */
    public RollResultData rollTextForAssignUnit(RollUnit rollUnit, Boolean up);
}
