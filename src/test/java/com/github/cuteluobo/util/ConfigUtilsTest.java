package com.github.cuteluobo.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigUtilsTest {

    @Test
    @DisplayName("检查生成默认配置文件")
    void createDefaultConfig() {
        System.out.println(ConfigUtils.createDefaultConfig());
    }
}