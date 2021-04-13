package com.github.cuteluobo.Pojo;

import com.github.cuteluobo.model.YysUnit;

/**
 * 抽卡对象
 * @author CuteLuoBo
 */
public class RollUnit {

    private Integer id;

    private String level;

    private String name;

    /**
     * 特殊名称（例：抽取时获得SP皮肤）
     */
    private String specialName;

    private Float rollProb;

    public RollUnit(Integer id, String level, String name) {
        this.id = id;
        this.level = level;
        this.name = name;
    }
    public RollUnit(RollUnit rollUnit){
        id = rollUnit.getId();
        level = rollUnit.getLevel();
        name = rollUnit.getName();
        specialName = rollUnit.getSpecialName();
        rollProb = rollUnit.getRollProb();
    }

    public RollUnit(YysUnit yysUnit) {
        if (yysUnit != null) {
            id = yysUnit.getUnitId();
            level = yysUnit.getLevel();
            name = yysUnit.getName();
            specialName = yysUnit.getSpecialName();
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RollUnit{");
        sb.append("id=").append(id);
        sb.append(", level='").append(level).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", specialName='").append(specialName).append('\'');
        sb.append(", rollProb=").append(rollProb);
        sb.append('}');
        return sb.toString();
    }

    public String getSpecialName() {
        return specialName;
    }

    public void setSpecialName(String specialName) {
        this.specialName = specialName;
    }

    public Float getRollProb() {
        return rollProb;
    }

    public void setRollProb(Float rollProb) {
        this.rollProb = rollProb;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
