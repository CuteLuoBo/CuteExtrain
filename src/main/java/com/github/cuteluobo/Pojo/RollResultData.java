package com.github.cuteluobo.Pojo;

import java.util.List;

/**
 * 抽卡结果
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public class RollResultData {
    private List<RollResultUnit> rollResultUnitList;

    private Integer rollNum;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(rollNum);
        sb.append("次抽卡结果:").append("\\n");
        if (rollResultUnitList==null || rollResultUnitList.size()==0){
            sb.append("无事发生");
        }else {
            for (RollResultUnit rollResultUnit : rollResultUnitList
            ) {
                sb.append(rollResultUnit).append("\\n");
            }
        }
        return sb.toString();
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
