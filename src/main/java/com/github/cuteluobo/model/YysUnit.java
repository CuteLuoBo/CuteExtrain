package com.github.cuteluobo.model;


import org.jetbrains.annotations.PropertyKey;

import javax.management.DescriptorKey;
import java.util.Objects;

/**
*@author CuteLuoBo
*@date 2021-04-08
*/
public class YysUnit {
    private Integer id;

    private Integer unitId;

    private String level;

    private String name;

    private String specialName;

    private Boolean canRoll;

    public static final String COL_ID = "id";

    public static final String COL_UNIT_ID = "unit_id";

    public static final String COL_LEVEL = "level";

    public static final String COL_NAME = "name";

    public static final String COL_SPECIAL_NAME = "special_name";

    public static final String COL_CAN_ROLL = "can_roll";

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return unit_id
     */
    public Integer getUnitId() {
        return unitId;
    }

    /**
     * @param unitId
     */
    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    /**
     * @return level
     */
    public String getLevel() {
        return level;
    }

    /**
     * @param level
     */
    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return special_name
     */
    public String getSpecialName() {
        return specialName;
    }

    /**
     * @param specialName
     */
    public void setSpecialName(String specialName) {
        this.specialName = specialName == null ? null : specialName.trim();
    }

    /**
     * @return can_roll
     */
    public Boolean getCanRoll() {
        return canRoll;
    }

    /**
     * @param canRoll
     */
    public void setCanRoll(Boolean canRoll) {
        this.canRoll = canRoll;
    }

    /**
     * 比较基本信息（排除ID）
     * @param o 用于对比的对象
     * @return 对比结果
     */
    public boolean equalsBaseInfo(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof YysUnit)) {
            return false;
        }
        YysUnit unit = (YysUnit) o;
        return getUnitId().equals(unit.getUnitId()) && getLevel().equals(unit.getLevel()) && getName().equals(unit.getName()) && Objects.equals(getSpecialName(), unit.getSpecialName()) && getCanRoll().equals(unit.getCanRoll());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof YysUnit)) {
            return false;
        }
        YysUnit unit = (YysUnit) o;
        return Objects.equals(getId(), unit.getId()) && getUnitId().equals(unit.getUnitId()) && getLevel().equals(unit.getLevel()) && getName().equals(unit.getName()) && Objects.equals(getSpecialName(), unit.getSpecialName()) && getCanRoll().equals(unit.getCanRoll());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUnitId(), getLevel(), getName(), getSpecialName(), getCanRoll());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", unitId=").append(unitId);
        sb.append(", level=").append(level);
        sb.append(", name=").append(name);
        sb.append(", specialName=").append(specialName);
        sb.append(", canRoll=").append(canRoll);
        sb.append("]");
        return sb.toString();
    }
}