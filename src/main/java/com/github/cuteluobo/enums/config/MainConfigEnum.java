package com.github.cuteluobo.enums.config;

import com.github.cuteluobo.enums.config.impl.AiDrawConfigEnum;
import com.github.cuteluobo.enums.config.impl.WebUiConfig;

/**
 * 主配置枚举类
 *
 * @author CuteLuoBo
 * @date 2022/7/27 15:23
 */
public enum MainConfigEnum implements IConfigEnum {
    /***/
    VERSION("version", "1.0", "配置文件版本号"),
    BOT_ADMIN("bot-admin", "635419450", "机器人管理员"),
    AI_DRAW("ai-draw", null, "AI绘图功能", AiDrawConfigEnum.values()),

    ;
    private String label;
    private String value;
    private String description;
    private IConfigEnum[] childNode;

    MainConfigEnum(String label, String value, String description) {
        this.label = label;
        this.value = value;
        this.description = description;
        this.childNode = null;
    }

    MainConfigEnum(String label, String value, String description, IConfigEnum[] childNode) {
        this.label = label;
        this.value = value;
        this.description = description;
        this.childNode = childNode;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public IConfigEnum[] getChildNode() {
        return childNode;
    }

}
