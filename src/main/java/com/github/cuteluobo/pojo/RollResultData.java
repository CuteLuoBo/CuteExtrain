package com.github.cuteluobo.pojo;

import java.util.*;

/**
 * 抽卡结果
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public abstract class RollResultData {
    /**
     * 抽卡结果数组
     */
    private List<RollResultUnit> rollResultUnitList = new ArrayList<>();

    /**
     * 抽取提示(在多少抽后面插入)
     * <抽取次数，提示语>
     */
    private LinkedHashMap<Integer, String> tipMap = new LinkedHashMap<>();
    private Integer rollNum = 0;

    /**
     * 打印抽取结果
     * @param showAllLevel 是否显示所有抽卡结果(低阶仅统计数量)
     * @param showAllTip 是否显示所有提示
     * @return
     */
    public abstract String printResultText(boolean showAllLevel,boolean showAllTip);

    public String printResultText(){
        return printResultText(false,false);
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

    public LinkedHashMap<Integer, String> getTipMap() {
        return tipMap;
    }

    public void setTipMap(LinkedHashMap<Integer, String> tipMap) {
        this.tipMap = tipMap;
    }
}
