package com.github.cuteluobo.Pojo;

import com.github.cuteluobo.enums.YysRoll;

import java.util.List;

/**
 * 抽卡结果
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public class RollResultData {
    private List<RollResultUnit> rollResultUnitList;

    private Integer rollNum;

    /**
     * 打印抽取结果
     * @param showAllLevel 是否显示所有抽卡结果
     * @param hideLowLevelDetail 是否隐藏低阶细节（仅统计数量）
     * @return
     */
    public String printResultText(boolean showAllLevel,boolean hideLowLevelDetail){
        final StringBuffer sb = new StringBuffer(rollNum);
        sb.append("次抽卡结果:").append("\\n");
        if (rollResultUnitList==null || rollResultUnitList.size()==0){
            sb.append("无事发生");
        }else {
            int rLevelNum = 0;
            int srLevelNum = 0;
            if (!showAllLevel) {
                for (RollResultUnit rollResultUnit : rollResultUnitList) {
                    if (YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel()) || YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel())) {
                        sb.append(rollResultUnit).append("\\n");
                    }
                    if (YysRoll.SR.getLevel().equals(rollResultUnit.getLevel())) {
                        srLevelNum++;
                    }
                    if (YysRoll.R.getLevel().equals(rollResultUnit.getLevel())) {
                        rLevelNum++;
                    }
                }
            }else {
                for (RollResultUnit rollResultUnit : rollResultUnitList) {
                    sb.append(rollResultUnit).append("\\n");
                }
            }
            //TODO 增加低阶输出（转换到子对象）
        }
        return sb.toString();
    }

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
}
