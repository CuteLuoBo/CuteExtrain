package com.github.cuteluobo.repository;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.github.cuteluobo.enums.config.MainConfigEnum;
import com.github.cuteluobo.util.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    public static final String CONFIG_FILE_NAME = "config.yaml";
    public static final String NOVELAI_TOKEN_FILE_NAME = "novel-token.txt";
    //TODO 修改为通过配置文件加载
    public static long ADMIN_ID = 635419450L;
    private String novelaiToken = "novelaiToken";
    public static final String YOU_DAO_TRANSLATE_APP_KEY = "YOU_DAO_TRANSLATE_APP_KEY";
    public static final String YOU_DAO_TRANSLATE_APP_SECRET = "YOU_DAO_TRANSLATE_APP_KEY";

    private static volatile GlobalConfig instance;

    private YamlMapping configMapping;
    private YamlMapping aiDrawMapping;

    public static GlobalConfig getInstance() {
        if (instance == null) {
            synchronized (GlobalConfig.class) {
                instance = new GlobalConfig();
            }
        }
        return instance;
    }

    /**
     * 重载配置文件
     */
    public void reloadConfig(){
        try {
            File configFile = checkConfigFile();
            configMapping = Yaml.createYamlInput(configFile).readYamlMapping();
            loadMapping();
        } catch (IOException ioException) {
            logger.error("读取配置文件错误,文件路径:{}", checkConfigFile().getAbsolutePath(), ioException);
        }
    }

    public void loadMapping() {
        aiDrawMapping = configMapping.yamlMapping(MainConfigEnum.AI_DRAW.getLabel());
    }

    public File checkConfigFile() {
        File configFile = new File(ResourceLoader.INSTANCE.getDataFolder().getAbsolutePath() + File.separator + CONFIG_FILE_NAME);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                YamlMapping yamlMapping = ConfigUtils.createDefaultConfig();
                Files.writeString(configFile.toPath(), yamlMapping.toString(), StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            } catch (IOException ioException) {
                logger.error("创建文件失败，路径:{}", configFile.getAbsolutePath(), ioException);
            }
        }
        return configFile;
    }


    private GlobalConfig(){
        reloadConfig();
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

    public YamlMapping getConfigMapping() {
        return configMapping;
    }

    public void setConfigMapping(YamlMapping configMapping) {
        this.configMapping = configMapping;
    }

    public YamlMapping getAiDrawMapping() {
        return aiDrawMapping;
    }

    public void setAiDrawMapping(YamlMapping aiDrawMapping) {
        this.aiDrawMapping = aiDrawMapping;
    }

    public String getNovelaiToken() {
        return novelaiToken;
    }

    public void setNovelaiToken(String novelaiToken) {
        this.novelaiToken = novelaiToken;
    }
}
