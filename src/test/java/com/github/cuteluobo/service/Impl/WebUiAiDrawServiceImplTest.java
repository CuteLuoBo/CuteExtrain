package com.github.cuteluobo.service.Impl;

import com.github.cuteluobo.excepiton.ServiceException;
import com.github.cuteluobo.pojo.aidraw.AiImageCreateImg2ImgParameter;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WebUiAiDrawServiceImplTest {

    @Test
    void login() throws URISyntaxException, IOException, InterruptedException, ServiceException {
        WebUiAiDrawServiceImpl webUiAiDrawService = new WebUiAiDrawServiceImpl("http://127.0.0.1:7860", "test","test");
        assertTrue(webUiAiDrawService.login());
    }

    @Test
    void txt2img() throws URISyntaxException, IOException, InterruptedException, ServiceException {
        WebUiAiDrawServiceImpl webUiAiDrawService = new WebUiAiDrawServiceImpl("http://127.0.0.1:7860", "test","test");
        List<byte[]> bytes = webUiAiDrawService.txt2img(new AiImageCreateParameter("masterpiece, best quality,"+"miku,lolita"));
        assertFalse(bytes.isEmpty());
        File file = FileIoUtils.createFileTemp("webuiTest-txt2img", "test.png");
        Files.write(file.toPath(), bytes.get(0), StandardOpenOption.CREATE);
        System.out.println(file.getAbsolutePath());
    }

    @Test
    void img2img() throws ServiceException, URISyntaxException, IOException, InterruptedException {
        WebUiAiDrawServiceImpl webUiAiDrawService = new WebUiAiDrawServiceImpl("http://127.0.0.1:7860", "test","test");
        AiImageCreateImg2ImgParameter aiImageCreateImg2ImgParameter = new AiImageCreateImg2ImgParameter("masterpiece, best quality,"+"miku,lolita");
        String rawBase64 = FileIoUtils.loadFile2Base64(new File("J:\\test-temp\\test.png"));
        String fileBase64 = "data:image/png;base64,"+rawBase64;
        aiImageCreateImg2ImgParameter.setBase64ImgData(fileBase64);
//        aiImageCreateImg2ImgParameter.setBase64ImgData("data:image/png;base64,6QAAAARnQU1BAACx");
        List<byte[]> bytes = webUiAiDrawService.img2img(aiImageCreateImg2ImgParameter);
        assertFalse(bytes.isEmpty());
        File file = FileIoUtils.createFileTemp("webuiTest-img2img", "test.png");
        Files.write(file.toPath(), bytes.get(0), StandardOpenOption.CREATE);
        System.out.println(file.getAbsolutePath());
    }
}