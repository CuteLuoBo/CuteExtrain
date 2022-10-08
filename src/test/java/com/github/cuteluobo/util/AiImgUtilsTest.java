package com.github.cuteluobo.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.*;

class AiImgUtilsTest {

    @Test
    void getImg() throws URISyntaxException, IOException, InterruptedException {
        byte[] bytes = AiImgUtils.getImg("白毛 红瞳 猫耳 萝莉", "nai-diffusion", true);
        File file = FileIoUtils.createFileTemp("AiImgUtilsTest", "test.png");
        Files.write(file.toPath(), bytes, StandardOpenOption.CREATE);
        System.out.println(file.getAbsolutePath());

    }
}