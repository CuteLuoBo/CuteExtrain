package com.github.cuteluobo.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 全局配置
 *
 * @author CuteLuoBo
 * @date 2022/6/24 16:27
 */
public class GlobalConfig {
    Logger logger = LoggerFactory.getLogger(GlobalConfig.class);
    public static final String NOVELAI_TOKEN_FILE_NAME = "novel-token.txt";
    public static final long ADMIN_ID = 635419450L;
    private String novelaiToken = "novelaiToken";
    public static final String YOU_DAO_TRANSLATE_APP_KEY = "YOU_DAO_TRANSLATE_APP_KEY";
    public static final String YOU_DAO_TRANSLATE_APP_SECRET = "YOU_DAO_TRANSLATE_APP_KEY";

    private static volatile GlobalConfig instance;


    public static GlobalConfig getInstance() {
        if (instance == null) {
            synchronized (GlobalConfig.class) {
                instance = new GlobalConfig();
            }
        }
        return instance;
    }


    private GlobalConfig(){
        File novelaiTokenFile = new File(ResourceLoader.INSTANCE.getDataFolder().getAbsolutePath() + File.separator + NOVELAI_TOKEN_FILE_NAME);
        if (!novelaiTokenFile.exists()) {
            try {
                novelaiTokenFile.createNewFile();
            } catch (IOException ioException) {
                logger.error("创建文件失败，路径:{}", novelaiTokenFile.getAbsolutePath(), ioException);
            }
        }
        if (novelaiTokenFile.exists()) {
            try {
                String readString = Files.readString(novelaiTokenFile.toPath()).trim();
                if (readString.length() > 0) {
                    novelaiToken = Files.readString(novelaiTokenFile.toPath());
                }
                novelaiToken = Files.readString(novelaiTokenFile.toPath());
            } catch (IOException ioException) {
                logger.error("读取token文件失败，文件路径:{}", novelaiTokenFile.getAbsolutePath(), ioException);
            }
        }
    }

    public String getNovelaiToken() {
        return novelaiToken;
    }

    public void setNovelaiToken(String novelaiToken) {
        this.novelaiToken = novelaiToken;
    }
}
