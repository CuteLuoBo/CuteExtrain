package com.github.cuteluobo.service.Impl;

import com.github.cuteluobo.excepiton.ServiceException;
import com.github.cuteluobo.pojo.aidraw.AiImageCreateParameter;
import com.github.cuteluobo.util.AiImgUtils;
import com.github.cuteluobo.util.FileIoUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.*;

class WebUiAiDrawServiceImplTest {

    @Test
    void login() throws URISyntaxException, IOException, InterruptedException, ServiceException {
        WebUiAiDrawServiceImpl webUiAiDrawService = new WebUiAiDrawServiceImpl("http://127.0.0.1:7860", "test","test");
        assertTrue(webUiAiDrawService.login());
    }

    @Test
    void txt2img() throws URISyntaxException, IOException, InterruptedException, ServiceException {
        WebUiAiDrawServiceImpl webUiAiDrawService = new WebUiAiDrawServiceImpl("http://127.0.0.1:7860", "Qk4sipt0UW2CfyA_Tpi--Q");
        byte[] bytes = webUiAiDrawService.txt2img(new AiImageCreateParameter("masterpiece, best quality,"+"miku,lolita"));
        File file = FileIoUtils.createFileTemp("webuiTest", "test.png");
        Files.write(file.toPath(), bytes, StandardOpenOption.CREATE);
        System.out.println(file.getAbsolutePath());
    }

    @Test
    void img2img() {
    }
}