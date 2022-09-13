package com.github.cuteluobo.task;

import com.github.cuteluobo.util.FileIoUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * 下载接口的基础实现
 * 此处使用的IO下载为同步操作,便于直接返回结果，建议调用的相关任务启用异步操作
 * @author CuteLuoBo
 * @date 2022/9/9 16:55
 */
public class BaseDownloader implements IDownloader {
    Logger logger = LoggerFactory.getLogger(BaseDownloader.class);



    private volatile static BaseDownloader instance;

    private static final Path TEMP_PATH = Paths.get("temp");

    public static BaseDownloader getInstance() {
        if (instance == null) {
            synchronized (BaseDownloader.class) {
                instance = new BaseDownloader();
            }
        }
        return instance;
    }

    private BaseDownloader() {
        File tempPathFile = TEMP_PATH.toFile();
        //删除上次程序运行时的缓存文件
        if (tempPathFile.exists()) {
            FileIoUtils.deleteFolderFile(tempPathFile, false);
        } else {
            tempPathFile.mkdirs();
        }
    }

    /**
     * 根据指定URL下载单个文件
     *
     * @param url          文件URL
     * @param fileSaveName 文件保存名称（包含后缀），为Null时，使用url的源文件名称与后缀保存
     * @param savePath     保存文件路径
     * @param mode         文件重名时的操作模式
     * @return 将保存的文件路径
     */
    @Override
    public File downloadFile(@NotNull URL url, String fileSaveName, Path savePath, DuplicationSaveMode mode) throws IOException {
        //验证保存文件名
        fileSaveName = checkFileName(url, fileSaveName);
        //检查重名
        File file = new File(savePath.toAbsolutePath() + File.separator + fileSaveName);
        //重名且跳过时，直接返回原文件
        File checkFile = duplicationFileHandler(file,mode);
        if (checkFile == null) {
            return file;
        }
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.createNewFile();
        }
        //下载文件
        //参考文档：https://www.baeldung.com/java-download-file
        //C.NIO的Channel复制方法，不需要经过jvm的内存而直接保存
        try (
                ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(checkFile);
        ) {
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
        return checkFile;
    }

    /**
     * 将下载文件保存到缓存目录中进行临时储存
     *
     * @param url 文件URL
     * @return 保存的文件路径
     */
    @Override
    public File downloadFileToTemp(URL url) throws IOException {
        return downloadFile(url,null,TEMP_PATH);
    }

    private String checkFileName(URL url, String fileSaveName) {
        if (fileSaveName == null || fileSaveName.trim().length() == 0) {
            String urlString = url.toString();
            fileSaveName = urlString.substring(urlString.lastIndexOf('/'));
        }
        return fileSaveName;
    }

    /**
     * 重名文件处理方法
     * @param saveFile 保存的文件
     * @param mode     处理模式
     * @return 为可用的新文件对象，返回null时，表明不需要额外操作(skip)
     */
    private File duplicationFileHandler(File saveFile, DuplicationSaveMode mode) {
        String fileSaveName = saveFile.getName();
        String pathName = saveFile.getAbsolutePath();
        switch (mode) {
            //重命名
            case RENAME:
                while (saveFile.exists()) {
                    //查找后缀符
                    int lastSuffixIndex = fileSaveName.lastIndexOf(".");
                    //尝试重命名
                    if (lastSuffixIndex == -1) {
                        fileSaveName = fileSaveName + new Random().nextInt(1000);
                    } else {
                        fileSaveName = fileSaveName.substring(0, lastSuffixIndex - 1)
                                + '-'
                                + new Random().nextInt(1000) + fileSaveName.substring(lastSuffixIndex);
                    }
                    //重赋值
                    saveFile = new File(pathName + File.separator + fileSaveName);
                }
                break;
            //跳过
            case SKIP:
                return null;
            default:
                break;
        }
        return saveFile;
    }
}
