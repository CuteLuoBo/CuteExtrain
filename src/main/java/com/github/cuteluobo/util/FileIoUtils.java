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

    /**
     * 删除文件夹内文件
     * @param folder           文件夹
     * @param deleteFolderSelf 是否删除文件夹本身
     * @throws SecurityException 删除错误时提示
     */
    public static void deleteFolderFile(File folder, boolean deleteFolderSelf) throws SecurityException {
        if (folder == null) {
            return;
        }
        //为文件夹时才执行删除
        if (folder.isDirectory()) {
            //遍历内部文件执行删除
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f :
                        files) {
                    //为文件夹时，递归删除
                    if (f.isDirectory()) {
                        deleteFolderFile(f, true);
                    } else {
                        f.delete();
                    }
                }
            }
            //删除文件夹自身
            if (deleteFolderSelf) {
                folder.delete();
            }
        }
    }
}
