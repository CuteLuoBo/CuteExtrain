package com.github.cuteluobo.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebUiServerRepositoryTest {

    @Test
    @DisplayName("初始化服务器缓存")
    void getInstance() {
        WebUiServerRepository.getInstance();
    }
}