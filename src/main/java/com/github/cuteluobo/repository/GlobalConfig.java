package com.github.cuteluobo.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

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
        loadNovelaiToken();
    }

    /**
     * 保存NovelaiToken到文件
     * @param token token
     * @throws IOException 保存文件时错误
     */
    public boolean saveNovelaiTokenFile(String token) throws IOException {
        if (token == null || token.trim().length() == 0) {
            return false;
        }
        //更新缓存值
        novelaiToken = token.trim();
        //进行文件操作
        File novelaiTokenFile = checkNovelaiTokenFile();
        //删除并重写
        novelaiTokenFile.delete();
        Files.writeString(novelaiTokenFile.toPath(), novelaiToken, StandardOpenOption.CREATE);
        return true;
    }

    private File checkNovelaiTokenFile() {
        File novelaiTokenFile = new File(ResourceLoader.INSTANCE.getDataFolder().getAbsolutePath() + File.separator + NOVELAI_TOKEN_FILE_NAME);
        if (!novelaiTokenFile.exists()) {
            try {
                novelaiTokenFile.createNewFile();
            } catch (IOException ioException) {
                logger.error("创建文件失败，路径:{}", novelaiTokenFile.getAbsolutePath(), ioException);
            }
        }
        return novelaiTokenFile;
    }

    private void loadNovelaiToken() {
        File novelaiTokenFile = checkNovelaiTokenFile();
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
