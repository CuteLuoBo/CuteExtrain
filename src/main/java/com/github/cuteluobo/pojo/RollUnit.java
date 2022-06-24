package com.github.cuteluobo.pojo;

import com.github.cuteluobo.model.YysUnit;

/**
 * 抽卡对象
 * @author CuteLuoBo
 */
public class RollUnit {

    private Integer id;

    /**
     * 官方ID
     */
    private Long officialId;

    private String level;

    private String name;

    /**
     * 特殊名称（例：抽取时获得SP皮肤）
     */
    private String specialName;

    private Float rollProb;

    public RollUnit(Integer id,Long officialId, String level, String name) {
        this.id = id;
        this.officialId = officialId;
        this.level = level;
        this.name = name;
    }
    public RollUnit(RollUnit rollUnit){
        id = rollUnit.getId();
        officialId = rollUnit.getOfficialId();
        level = rollUnit.getLevel();
        name = rollUnit.getName();
        specialName = rollUnit.getSpecialName();
        rollProb = rollUnit.getRollProb();
    }

    public RollUnit(YysUnit yysUnit) {
        if (yysUnit != null) {
            id = yysUnit.getUnitId();
            officialId = (long) yysUnit.getUnitId();
            level = yysUnit.getLevel();
            name = yysUnit.getName();
            specialName = yysUnit.getSpecialName();
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RollUnit{");
        sb.append("id=").append(id);
        sb.append(", officialId=").append(officialId);
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

    public Long getOfficialId() {
        return officialId;
    }

    public void setOfficialId(Long officialId) {
        this.officialId = officialId;
    }
}
