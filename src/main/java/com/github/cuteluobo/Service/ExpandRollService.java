package com.github.cuteluobo.Service;

import com.github.cuteluobo.Pojo.RollResultData;

/**
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public interface ExpandRollService extends RollService {
    /**
     * 抽取指定对象
     * @param rollUnitId 抽取对象ID
     * @param fullBuff 是否启用全图鉴加成
     * @return
     */
    public RollResultData rollTextForSpecifyUnit(Integer rollUnitId,Boolean fullBuff);
}
