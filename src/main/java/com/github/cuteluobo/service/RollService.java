package com.github.cuteluobo.service;

import com.github.cuteluobo.pojo.RollResultData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public interface RollService {

    /**
     * 普通抽取
     *
     * @param rollNum 抽取次数
     * @param winProb 高阶抽取概率，为null时使用默认概率
     * @return 抽取结果
     */
    public RollResultData roll(@NotNull Integer rollNum, @Nullable Float winProb);

    /**
     * 抽取结果-启用特定概率提升
     *
     * @param rollNum 抽取次数
     * @param up      是否启用概率up
     * @param upNum   up
     * @param upRate  up倍率
     * @return 抽取结果
     */
    public RollResultData rollUp(@NotNull Integer rollNum, @NotNull Boolean up, Integer upNum, Float upRate);

    /**
     * 重载式神信息
     */
    public void reloadData();

}
