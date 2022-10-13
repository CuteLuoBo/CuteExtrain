package com.github.cuteluobo.util;

import com.amihaiemil.eoyaml.*;
import com.github.cuteluobo.common.config.ConfigField;
import com.github.cuteluobo.enums.config.IConfigArrayMainEnum;
import com.github.cuteluobo.enums.config.IConfigEnum;
import com.github.cuteluobo.enums.config.MainConfigEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * 配置文件相关工具类
 * copy on BetterWorld
 * @author CuteLuoBo
 * @date 2022/9/4 21:52
 */
public class ConfigUtils {
    static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);
    /**
     * 创建默认配置文件
     * 使用eo-yaml框架生成
     * @return 配置文件结构
     */
    public static YamlMapping createDefaultConfig() {
        return createChildNode(MainConfigEnum.values()).build();
    }

    /**
     * 创建YamlNode子节点
     *
     * @return 生成的node
     */
    private static YamlMappingBuilder createChildNode(IConfigEnum[] childEnums) {
        //使用递归来获取子枚举节点
        YamlNode yamlNode;
        YamlMappingBuilder yamlMappingBuilder = Yaml.createYamlMappingBuilder();
        for (IConfigEnum en : childEnums) {
            if (en.getChildNode() != null) {
                yamlNode = createChildNode(en.getChildNode()).build(en.getDescription());
            }  else if (en.getValue() != null) {
                yamlNode = Yaml.createYamlScalarBuilder().addLine(String.valueOf(en.getValue())).buildPlainScalar(en.getDescription());
            }  else if (en instanceof IConfigArrayMainEnum) {
                IConfigArrayMainEnum am = (IConfigArrayMainEnum) en;
                //解析对应的
                if (am.getArrayUnitClass() != null) {
                    Class<?> clazz = am.getArrayUnitClass();
                    Field[] fields = clazz.getDeclaredFields();
                    YamlSequenceBuilder mainSequenceBuilder = Yaml.createYamlSequenceBuilder();
                    YamlMappingBuilder nodeMapperBuilder = Yaml.createYamlMappingBuilder();
                    for (Field f :
                            fields) {
                        ConfigField configField = f.getAnnotation(ConfigField.class);
                        //TODO 后续可增加类适用的注解进行批量启用
                        if (configField != null) {
                            f.setAccessible(true);
                            //字段名
                            String fieldName = (configField.label() != null && configField.label().trim().length() != 0) ? configField.label() : f.getName();
                            //字段值
                            String fieldValue = "";
                            try {
                                //TODO 反射获取私有数据失败，尝试找到其他解决方法
//                                Object newInstance = f.getType().getDeclaredConstructor().newInstance();
//                                String value = String.valueOf(f.get(newInstance));
                                fieldValue = (configField.value() != null && configField.value().trim().length() != 0) ? configField.value() : String.valueOf(f.get(f.getType()));
                            } catch (Exception exception) {
                                logger.debug("configField exception",exception);
                            }
                            if (configField.enable()) {
                                nodeMapperBuilder = nodeMapperBuilder.add(fieldName, Yaml
                                        .createYamlScalarBuilder()
                                        .addLine(fieldValue)
                                        .buildPlainScalar(configField.description()));
                            }
                        }
                    }
                    yamlNode = mainSequenceBuilder.add(nodeMapperBuilder.build()).build(am.getDescription());
                } else {
                    yamlNode = Yaml.createYamlScalarBuilder().buildPlainScalar(en.getDescription());
                }
            }else {
                yamlNode = Yaml.createYamlScalarBuilder().buildPlainScalar(en.getDescription());
            }
            yamlMappingBuilder = yamlMappingBuilder.add(en.getLabel(), yamlNode);
        }
        return yamlMappingBuilder;
    }
}
