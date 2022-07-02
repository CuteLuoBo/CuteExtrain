package com.github.cuteluobo.enums;

/**
 * 指令限制时，触发的效果
 * @author CuteLuoBo
 * @date 2021-04-22
 */
public enum TriggerType {
    /***/
    NONE(-1,"",600),IGNORE_TEN_MINUTE(0,"被机器人无视10分钟",600);
    private final Integer value;
    private final String description;
    private final Integer second;

    TriggerType(Integer value, String description, Integer second) {
        this.value = value;
        this.description = description;
        this.second = second;
    }

    /**
     * 根据值匹配枚举
     * @param value 查找值
     * @return 枚举，没有匹配结果时返回null
     */
    public static TriggerType valueOf(int value) {
        for (TriggerType tt :
                TriggerType.values()) {
            if (tt.value.equals(value)) {
                return tt;
            }
        }
        return null;
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
