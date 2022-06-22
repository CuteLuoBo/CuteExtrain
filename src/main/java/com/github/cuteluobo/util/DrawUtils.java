package com.github.cuteluobo.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 绘图工具类
 *
 * @author CuteLuoBo
 * @date 2022/6/20 19:15
 */
public class DrawUtils {
    /**
     * 计算文本在容器钟的起始X点
     * @param fontMetrics 字体度量
     * @param text 需要写入的文本
     * @param containerWidth 容器宽度
     * @return
     */
    public static Integer getCenterStringStartPoint(FontMetrics fontMetrics,String text,Integer containerWidth) {
        int textWidth = fontMetrics.stringWidth(text);
        return (containerWidth - textWidth) / 2;
    }

    /**
     * 加载图片文件
     * @param file 图片文件路径
     * @return BufferedImage
     * @throws IOException 读取图片错误
     */
    public static BufferedImage loadImageFile(File file) throws IOException {
        if (file != null) {
            return ImageIO.read(file);
        }
        return null;
    }
}
