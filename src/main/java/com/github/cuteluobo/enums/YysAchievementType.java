package com.github.cuteluobo.enums;

/**
 * @author CuteLuoBo
 * @date 2021/11/23 0:33
 */
public enum YysAchievementType {
    ;

    YysAchievementType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
