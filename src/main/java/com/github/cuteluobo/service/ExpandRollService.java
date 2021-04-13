package com.github.cuteluobo.service;

import com.github.cuteluobo.Pojo.RollResultData;
import com.github.cuteluobo.Pojo.RollUnit;

/**
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public interface ExpandRollService extends RollService {
    /**
     * 抽取指定对象
     * @param rollUnit 指定概率UP对象
     * @param fullBuff 是否启用全图鉴加成
     * @return
     */
    public RollResultData rollTextForSpecifyUnit(RollUnit rollUnit, Boolean fullBuff);
}
