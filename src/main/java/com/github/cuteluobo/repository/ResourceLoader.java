package com.github.cuteluobo.repository;

import com.github.cuteluobo.CuteExtra;

import java.io.File;

/**
 * 资源加载器（图片生成用）
 * @author CuteLuoBo
 * @date 2022/6/23 21:47
 */
public class ResourceLoader {
    private File normalResourceFolder;
    private File normalTempFolder;

    public static final ResourceLoader INSTANCE = new ResourceLoader();

    private ResourceLoader() {
        //初始化
        if (CuteExtra.INSTANCE != null) {
            normalResourceFolder = new File(CuteExtra.INSTANCE.getDataFolder().getAbsolutePath()+File.separator+"imgResource");
            normalTempFolder = new File(CuteExtra.INSTANCE.getDataFolder().getAbsolutePath()+File.separator+"temp");
        } else {
            normalResourceFolder = new File(String.valueOf(this.getClass().getResource("imgResource")));
            normalTempFolder = new File(String.valueOf(this.getClass().getResource("temp")));
        }
        //创建文件夹
        if (!normalResourceFolder.exists()) {
            normalTempFolder.mkdirs();
        }
        if (!normalTempFolder.exists()) {
            normalTempFolder.mkdirs();
        }
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
