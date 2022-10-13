package com.github.cuteluobo.enums.config.impl;

import com.github.cuteluobo.enums.config.IConfigArrayMainEnum;
import com.github.cuteluobo.enums.config.IConfigEnum;
import com.github.cuteluobo.pojo.aidraw.ConfigWebUiServer;

/**
 * @author CuteLuoBo
 * @date 2022/10/11 22:27
 */
public enum WebUiConfig implements IConfigArrayMainEnum {
    /***/
    ENABLE("enable","false","是否启用（总开关）"),
    SERVER_LIST("server-list",null,"服务器列表", ConfigWebUiServer.class);
    private String label;
    private String value;
    private String description;
    private IConfigEnum[] childNode;
    private Class<?> arrayUnitClass;

    WebUiConfig(String label, String value, String description) {
        this.label = label;
        this.value = value;
        this.description = description;
    }

    WebUiConfig(String label, String value, String description, IConfigEnum[] childNode) {
        this.label = label;
        this.value = value;
        this.description = description;
        this.childNode = childNode;
    }

    WebUiConfig(String label, String value, String description, Class<?> arrayUnitClass) {
        this.label = label;
        this.value = value;
        this.description = description;
        this.arrayUnitClass = arrayUnitClass;
    }

    @Override
    public Class<?> getArrayUnitClass() {
        return arrayUnitClass;
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
