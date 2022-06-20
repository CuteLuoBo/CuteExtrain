package com.github.cuteluobo.enums;

/**
 * @author CuteLuoBo
 * @date 2021-04-22
 */
public enum TriggerType {
    /***/
    IGNORE_TEN_MINUTE(0,"被机器人无视10分钟",600);
    private final Integer value;
    private final String description;
    private final Integer second;

    TriggerType(Integer value, String description, Integer second) {
        this.value = value;
        this.description = description;
        this.second = second;
    }

    public Integer getSecond() {
        return second;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
