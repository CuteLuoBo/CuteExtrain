package com.github.cuteluobo.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class TranslateUtilsTest {

    @Test
    void autoToEN() throws URISyntaxException, IOException, InterruptedException {
        String result = TranslateUtils.autoToEN("测试");
        assertNotNull(result);
    }
}