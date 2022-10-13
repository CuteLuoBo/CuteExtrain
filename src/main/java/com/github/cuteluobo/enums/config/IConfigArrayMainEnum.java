package com.github.cuteluobo.enums.config;

/**
 * 配置 和数组相关的数据枚举
 *
 * @author CuteLuoBo
 * @date 2022/10/11 22:24
 */
public interface IConfigArrayMainEnum extends IConfigEnum {

    /**
     * 返回数组单体的类
     * @return 返回数组单体的类
     */
    Class<?> getArrayUnitClass();
}
