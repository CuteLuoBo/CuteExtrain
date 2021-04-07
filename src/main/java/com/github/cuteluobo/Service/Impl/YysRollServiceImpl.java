package com.github.cuteluobo.Service.Impl;

import com.github.cuteluobo.Pojo.RollImgResult;
import com.github.cuteluobo.Pojo.RollResultData;
import com.github.cuteluobo.Service.ExpandRollService;
import com.github.cuteluobo.Service.RollService;

import java.util.Random;

/**
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public class YysRollServiceImpl implements ExpandRollService {

    /**设置各抽卡概率，见官方:https://yys.163.com/news/notice/2017/05/01/25369_665793.html*/
    private final Float upRate = 3.0f;

    //TODO 设计式神列表格式

    /**
     * 抽取文字结果
     *
     * @param rollNum  抽取次数
     * @param up       是否启用概率up
     * @param upRate   up倍率
     * @param upNum    up
     * @param rollProb 指定抽取概率，为null时使用默认概率
     * @return 抽取结果
     */
    @Override
    public RollResultData rollText(Integer rollNum, Boolean up, Float upRate, Integer upNum, Float rollProb) {
        return null;
    }

    /**内部抽卡类，
     * TODO 返回抽取阶级，通过抽卡阶级在<阶级，式神列表>map内进行抽取式神*/
    private String roll(Boolean up, Float upRate, Float rollProb){
        return null;
    }

    /**
     * 抽取SSR皮肤(10%)
     * @return
     */
    private Boolean rollSsrSkin() {
        Random random = new Random();
        int rollNum = random.nextInt(9);
        if (rollNum == 0) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 抽取图片结果
     *
     * @param rollNum  抽取次数
     * @param up       是否启用概率up
     * @param upRate   up倍率
     * @param upNum    up
     * @param rollProb 指定抽取概率，为null时使用默认概率
     * @return 抽取结果
     */
    @Override
    public RollImgResult rollImg(Integer rollNum, Boolean up, Float upRate, Integer upNum, Float rollProb) {
        return null;
    }


    /**
     * 抽取指定对象
     *
     * @param rollUnitId 抽取对象ID
     * @param fullBuff   是否启用全图鉴加成
     * @return
     */
    @Override
    public RollResultData rollTextForSpecifyUnit(Integer rollUnitId, Boolean fullBuff) {
        return null;
    }
}
