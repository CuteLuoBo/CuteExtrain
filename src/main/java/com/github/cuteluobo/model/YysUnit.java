package com.github.cuteluobo.model;


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