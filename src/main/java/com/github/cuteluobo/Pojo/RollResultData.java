package com.github.cuteluobo.Pojo;

import com.github.cuteluobo.enums.YysRoll;

import java.util.List;
import java.util.Map;

/**
 * 抽卡结果
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public abstract class RollResultData {
    /**
     * 抽卡结果数组
     */
    private List<RollResultUnit> rollResultUnitList;

    /**
     * 抽取提示(在多少抽后面插入)
     * <抽取次数，提示语>
     */
    private Map<Integer, String> tipMap;
    private Integer rollNum;

    /**
     * 打印抽取结果
     * @param showAllLevel 是否显示所有抽卡结果
     * @param hideLowLevelDetail 是否隐藏低阶细节（仅统计数量）
     * @return
     */
    public abstract String printResultText(boolean showAllLevel,boolean hideLowLevelDetail);

    public String printResultText(){
        return printResultText(false,true);
    }

    public List<RollResultUnit> getRollUnitList() {
        return rollResultUnitList;
    }

    public void setRollUnitList(List<RollResultUnit> rollUnitList) {
        this.rollResultUnitList = rollUnitList;
    }

    public Integer getRollNum() {
        return rollNum;
    }

    public void setRollNum(Integer rollNum) {
        this.rollNum = rollNum;
    }

    public Map<Integer, String> getTipMap() {
        return tipMap;
    }

    public void setTipMap(Map<Integer, String> tipMap) {
        this.tipMap = tipMap;
    }
}
