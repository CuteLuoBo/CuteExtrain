package com.github.cuteluobo.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class FileIoUtilsTest {

    @Test
    void loadFile2Base64() throws IOException, URISyntaxException {
        String rawBase64 = FileIoUtils.loadFile2Base64(new File("J:\\test-temp\\test.png"));
        byte[] decode = Base64.getDecoder().decode(rawBase64);
        Files.write(new File("J:\\test-temp\\test-respawn.png").toPath(), decode, StandardOpenOption.CREATE);
    }
}