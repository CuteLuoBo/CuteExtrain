package com.github.cuteluobo.enums.config.impl;

import com.github.cuteluobo.enums.config.IConfigEnum;

/**
 * @author CuteLuoBo
 * @date 2022/10/12 0:34
 */
public enum AiDrawConfigEnum implements IConfigEnum {
    /***/
    ENABLE("enable","false","功能开关"),
    NOVELAI_TOKEN("novelai-token", "", "novelai的访问令牌"),
    WEB_UI_CONFIG("web-ui-config",null,"webui配置", WebUiConfig.values()),
    MAX_STEPS("max-steps","40","最大步数"),
    MAX_WIDTH("max-width","512","最大宽度(px)"),
    MAX_HEIGHT("max-height","768","最大高度(px)"),
    MAX_BATCH_COUNT("max-batch_count","2","最多一次提交多少批量生成数据（仅webui生效）"),
    MAX_BATCH_SIZE("max-batch_size","2","最多单批量包含多少张图片（仅webui生效）"),
    ;
    private String label;
    private String value;
    private String description;
    private IConfigEnum[] childNode;

    AiDrawConfigEnum(String label, String value, String description) {
        this.label = label;
        this.value = value;
        this.description = description;
    }

    AiDrawConfigEnum(String label, String value, String description, IConfigEnum[] childNode) {
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
