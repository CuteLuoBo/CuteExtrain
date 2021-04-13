package com.github.cuteluobo.service;

import com.github.cuteluobo.Pojo.RollImgResult;
import com.github.cuteluobo.Pojo.RollResultData;
import org.jetbrains.annotations.NotNull;

/**
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public interface RollService {
    /**抽取文字结果
     * @param rollNum  抽取次数
     * @param up 是否启用概率up
     * @param upRate   up倍率
     * @param upNum up
     * @param winProb 高阶抽取概率，为null时使用默认概率
     * @return 抽取结果
     * */
    public RollResultData rollText(@NotNull Integer rollNum,@NotNull Boolean up, Float upRate, Integer upNum, Float winProb);

    /**抽取图片结果
     * @param rollNum  抽取次数
     * @param up 是否启用概率up
     * @param upRate   up倍率
     * @param upNum up
     * @param winProb 高阶抽取概率，为null时使用默认概率
     * @return 抽取结果
     * */
    public RollImgResult rollImg(@NotNull Integer rollNum,@NotNull Boolean up, Float upRate, Integer upNum, Float winProb);
}
