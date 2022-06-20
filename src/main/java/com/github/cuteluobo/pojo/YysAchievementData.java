package com.github.cuteluobo.pojo;

import com.github.cuteluobo.enums.YysAchievementType;
import com.github.cuteluobo.model.YysUnit;

import java.util.List;

/**
 * 储存阴阳师抽空成就和相关关联式神
 * @author CuteLuoBo
 * @date 2021/11/23 0:32
 */
public class YysAchievementData {
    /**触发成就类型*/

    private YysAchievementType type;
    /**成就关联式神列表*/
    private List<YysUnit> unitList;
    /**关联抽卡开始数*/
    private Integer startRollNumber;
    /**关联抽卡结束数*/
    private Integer endRollNumber;

    public Integer getStartRollNumber() {
        return startRollNumber;
    }

    public void setStartRollNumber(Integer startRollNumber) {
        this.startRollNumber = startRollNumber;
    }

    public Integer getEndRollNumber() {
        return endRollNumber;
    }

    public void setEndRollNumber(Integer endRollNumber) {
        this.endRollNumber = endRollNumber;
    }

    public YysAchievementType getType() {
        return type;
    }

    public void setType(YysAchievementType type) {
        this.type = type;
    }

    public List<YysUnit> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<YysUnit> unitList) {
        this.unitList = unitList;
    }
}
