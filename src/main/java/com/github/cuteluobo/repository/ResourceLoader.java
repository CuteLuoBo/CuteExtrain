package com.github.cuteluobo.repository;

import com.github.cuteluobo.CuteExtra;
import com.github.cuteluobo.util.FileIoUtils;
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
    private File dataFolder;

    public static final ResourceLoader INSTANCE = new ResourceLoader();


    public static final String YYS_UNIT_IMAGE_HEAD = File.separator+"yys"+File.separator+"head";
    public static final String YYS_UNIT_IMAGE_BODY = File.separator+"yys"+File.separator+"body";

    private ResourceLoader() {
        //初始化
        try {
            normalResourceFolder = new File(CuteExtra.INSTANCE.getDataFolder().getAbsolutePath()+File.separator+"imgResource");
            normalTempFolder = new File(CuteExtra.INSTANCE.getDataFolder().getAbsolutePath()+File.separator+"temp");
            dataFolder = new File(CuteExtra.INSTANCE.getDataFolder().getAbsolutePath());
            //每次启动时，删除temp内所有文件
            FileIoUtils.deleteFolderFile(normalTempFolder,false);
            logger.warn("提示：插件资源路径中的temp文件夹在启动时会自动清空，请勿在此目录内存放其他文件");
        } catch (Throwable throwable) {
            normalResourceFolder = new File("external/imgResource");
            normalTempFolder = new File("external/temp");
            dataFolder = new File("external");
            logger.warn("使用插件的数据路径错误，切换为外置路径:{}",normalResourceFolder.getAbsolutePath());
            logger.debug("StackTrace:",throwable);
        }
        //创建文件夹
        if (!normalResourceFolder.exists()) {
            normalResourceFolder.mkdirs();
        }
        if (!normalTempFolder.exists()) {
            normalTempFolder.mkdirs();
        }
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        logger.info("使用的资源文件夹路径：{}",normalResourceFolder.getAbsolutePath());
        logger.info("使用的缓存文件夹路径：{}",normalTempFolder.getAbsolutePath());
        logger.info("使用的数据文件夹路径：{}",dataFolder.getAbsolutePath());
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public void setDataFolder(File dataFolder) {
        this.dataFolder = dataFolder;
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
