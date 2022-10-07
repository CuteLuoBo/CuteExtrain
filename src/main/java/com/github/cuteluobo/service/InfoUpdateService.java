package com.github.cuteluobo.service;

import com.github.cuteluobo.pojo.RollUnit;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 信息更新接口
 * @author CuteLuoBo
 * @date 2022/9/17 16:13
 */
public interface InfoUpdateService{

    /**
     * 更新全部单位信息
     */
    void updateAllUnitInfo();

    /**
     * 更新指定单位信息
     *
     * @param unitIdList 式神ID列表
     */
    void updateAssignUnitInfo(List<Integer> unitIdList);

}
