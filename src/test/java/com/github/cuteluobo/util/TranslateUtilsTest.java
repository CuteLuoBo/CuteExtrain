package com.github.cuteluobo.util;

import com.github.cuteluobo.excepiton.ServiceException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class TranslateUtilsTest {

    @Test
    void autoToEN() throws URISyntaxException, IOException, InterruptedException, ServiceException {
        String result = TranslateUtils.autoToEn("测试");
        assertNotNull(result);
    }
}