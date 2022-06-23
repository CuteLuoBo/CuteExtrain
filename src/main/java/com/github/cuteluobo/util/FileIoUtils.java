package com.github.cuteluobo.util;

import com.github.cuteluobo.repository.ResourceLoader;

import java.io.File;
import java.util.Random;

/**
 * 文件IO操作工具类
 * @author CuteLuoBo
 * @date 2022/6/23 22:20
 */
public class FileIoUtils {
    /**
     * 生成缓存文件
     * @param modelName 模块名称
     * @param fileName 文件名称（带后缀）
     * @return
     */
    public static File createFileTemp(String modelName,String fileName) {
        return new File(ResourceLoader.INSTANCE.getNormalTempFolder().getAbsolutePath() + File.separator + modelName + new Random().nextInt(10) + "-" + System.currentTimeMillis() + fileName);
    }


}
