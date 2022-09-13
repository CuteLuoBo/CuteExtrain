package com.github.cuteluobo.task;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * 下载者约束接口
 *
 * @author CuteLuoBo
 * @date 2022/9/9 16:33
 */
public interface IDownloader {
    Logger logger = LoggerFactory.getLogger(IDownloader.class);

    /**
     * 名称重复时的保存模式
     */
    enum DuplicationSaveMode {
        /**将新文件对旧文件进行替换*/
        REPLACE,
        /**
         * 新文件重命名
         */
        RENAME,
        /**
         * 跳过
         */
        SKIP
    }

    /**
     * 根据指定URL下载单个文件 (重名默认直接替换)
     * @param url 文件URL
     * @param fileSaveName 文件保存名称（包含后缀），为Null时，使用url的源文件名称与后缀保存
     * @param savePath 保存文件路径
     * @return 将保存的文件路径
     * @throws IOException 网络操作错误
     */
    default File downloadFile(@NotNull URL url, String fileSaveName, Path savePath) throws IOException {
        return downloadFile(url, fileSaveName, savePath, DuplicationSaveMode.REPLACE);
    }

    /**
     * 根据指定URL下载单个文件
     * @param url 文件URL
     * @param fileSaveName 文件保存名称（包含后缀），为Null时，使用url的源文件名称与后缀保存
     * @param savePath 保存文件路径
     * @param mode         文件重名时的操作模式
     * @return 将保存的文件路径
     * @throws IOException 网络操作错误
     */
    public File downloadFile(@NotNull URL url, String fileSaveName, Path savePath,DuplicationSaveMode mode) throws IOException;

    /**
     * 将下载文件保存到缓存目录中进行临时储存
     * @param url 文件URL
     * @return 保存的文件路径
     */
    public File downloadFileToTemp(URL url) throws IOException;

    /**
     * 批量下载URL文件到指定目录 (重名默认直接替换)
     * @param urlList           文件URL列表
     * @param saveFileNameList  保存名称列表，与URL列表对应，为null时，使用url的源文件名称与后缀保存
     * @param savePath          文件保存路径
     * @return 保存的文件列表
     */
    default List<File> downloadFileList(@NotNull List<URL> urlList, List<String> saveFileNameList, Path savePath) throws IOException {
        return downloadFileList(urlList, saveFileNameList, savePath, DuplicationSaveMode.REPLACE);
    }

    /**
     * 批量下载URL文件到指定目录
     * @param urlList           文件URL列表
     * @param saveFileNameList  保存名称列表，与URL列表对应，为null时，使用url的源文件名称与后缀保存
     * @param savePath          文件保存路径
     * @param mode              文件重名时操作模式
     * @return 保存的文件列表
     */
    default List<File> downloadFileList(@NotNull List<URL> urlList, List<String> saveFileNameList, Path savePath,DuplicationSaveMode mode){
        List<File> fileList = new ArrayList<>(urlList.size());
        if (saveFileNameList == null || saveFileNameList.size() == 0) {
            for (int i = 0; i < urlList.size(); i++) {
                try {
                    fileList.add(downloadFile(urlList.get(i),null,savePath,mode));
                } catch (IOException ioException) {
                    logger.debug("下载文件时出现错误，url:{}", urlList.get(i), ioException);
                }

            }
        } else if (urlList.size() == saveFileNameList.size()) {
            for (int i = 0; i < urlList.size(); i++) {
                String saveName = saveFileNameList.get(i);
                try {
                    fileList.add(downloadFile(urlList.get(i),saveName,savePath,mode));
                } catch (IOException ioException) {
                    logger.debug("下载文件时出现错误，url:{}", urlList.get(i), ioException);
                }

            }
        } else {
            for (int i = 0; i < urlList.size(); i++) {
                String saveName = null;
                if (i < saveFileNameList.size()) {
                    saveName = saveFileNameList.get(i);
                }
                try {
                    fileList.add(downloadFile(urlList.get(i), saveName, savePath, mode));
                } catch (IOException ioException) {
                    logger.debug("下载文件时出现错误，url:{}", urlList.get(i), ioException);
                }
            }
        }
        return fileList;
    }
}
