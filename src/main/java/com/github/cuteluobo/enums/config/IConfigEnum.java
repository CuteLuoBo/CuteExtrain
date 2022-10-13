package com.github.cuteluobo.enums.config;

/**
 * 配置相关枚举接口

 * @author CuteLuoBo
 * @date 2022/7/27 15:31
 */
public interface IConfigEnum extends IBaseEnum {
    /**
     * 配置描述
     * @return 配置描述
     */
    String getDescription();

    /**
     * 获取子节点
     * @return 子节点
     */
    IConfigEnum[] getChildNode();



}