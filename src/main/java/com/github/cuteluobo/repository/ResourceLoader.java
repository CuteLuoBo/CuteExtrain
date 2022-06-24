package com.github.cuteluobo.repository;

import com.github.cuteluobo.CuteExtra;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 资源加载器（图片生成用）
 * @author CuteLuoBo
 * @date 2022/6/23 21:47
 */
public class ResourceLoader {
    Logger logger = LoggerFactory.getLogger(ResourceLoader.class);
    private File normalResourceFolder;
    private File normalTempFolder;

    public static final ResourceLoader INSTANCE = new ResourceLoader();

    private ResourceLoader() {
        //初始化
        try {
            normalResourceFolder = new File(CuteExtra.INSTANCE.getDataFolder().getAbsolutePath()+File.separator+"imgResource");
            normalTempFolder = new File(CuteExtra.INSTANCE.getDataFolder().getAbsolutePath()+File.separator+"temp");
        } catch (Throwable throwable) {
            logger.error("使用插件的数据路径错误，切换为外置路径",throwable);
            normalResourceFolder = new File(String.valueOf(this.getClass().getResource("imgResource")));
            normalTempFolder = new File(String.valueOf(this.getClass().getResource("temp")));
        }
        //创建文件夹
        if (!normalResourceFolder.exists()) {
            normalResourceFolder.mkdirs();
        }
        if (!normalTempFolder.exists()) {
            normalTempFolder.mkdirs();
        }
        logger.info("使用的资源文件夹路径：{}",normalResourceFolder.getAbsolutePath());
        logger.info("使用的缓存文件夹路径：{}",normalTempFolder.getAbsolutePath());
    }

    public File getNormalTempFolder() {
        return normalTempFolder;
    }

    public void setNormalTempFolder(File normalTempFolder) {
        this.normalTempFolder = normalTempFolder;
    }

    public File getNormalResourceFolder() {
        return normalResourceFolder;
    }

    public void setNormalResourceFolder(File normalResourceFolder) {
        this.normalResourceFolder = normalResourceFolder;
    }
}
