package com.github.cuteluobo.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class AiImgUtilsTest {

    @Test
    void getImg() throws URISyntaxException, IOException, InterruptedException, NoSuchAlgorithmException {
        byte[] bytes = AiImgUtils.getImg("miku,lolita,flat_chest", true);
        File file = FileIoUtils.createFileTemp("AiImgUtilsTest", "test.png");
        Files.write(file.toPath(), bytes, StandardOpenOption.CREATE);
        System.out.println(file.getAbsolutePath());
    }
}