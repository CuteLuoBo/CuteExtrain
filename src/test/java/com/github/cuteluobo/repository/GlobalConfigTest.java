package com.github.cuteluobo.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GlobalConfigTest {

    @Test
    @DisplayName("尝试初始化实例")
    void getInstance() {
        GlobalConfig.getInstance();
    }
}