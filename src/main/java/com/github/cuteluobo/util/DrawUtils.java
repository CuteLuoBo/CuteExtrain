package com.github.cuteluobo.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
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
     * 绘制圆角矩形容器
     *
     * @param graphics2D               绘制对象
     * @param containerBackgroundColor 容器的背景颜色，为null时即不填充背景颜色
     * @param borderColor              边框的颜色，为null时即不绘制边框
     * @param startX                   起始X
     * @param startY                   起始Y
     * @param width                    容器宽度
     * @param height                   容器高度
     * @param roundRate                圆角弧度
     */
    public void drawRoundRectContainer(Graphics2D graphics2D, Color containerBackgroundColor, Color borderColor, int startX, int startY, int width, int height, int roundRate) {
        //容器
        graphics2D.setColor(containerBackgroundColor);
        graphics2D.fillRoundRect(startX, startY
                , width, height
                , roundRate, roundRate);
        //绘制容器外框
        graphics2D.setColor(borderColor);
        graphics2D.draw(new RoundRectangle2D.Float(startX, startY
                , width, height
                , roundRate, roundRate));
    }

    /**
     * 计算文本在容器居中的起始X坐标
     * @param fontMetrics 字体度量
     * @param text 需要写入的文本
     * @param containerWidth 容器宽度
     * @return
     */
    public static int calcTextCenterStartX(FontMetrics fontMetrics, String text, Integer containerWidth) {
        return (containerWidth - fontMetrics.stringWidth(text)) / 2;
    }

    /**
     * 计算文本在容器垂直居中的结束Y坐标
     *
     * @param text            需要写入的文本
     * @param fontSize        字体大小
     * @param containerHeight 容器高度
     * @return
     */
    public static int calcTextVerticalCenterEndY(String text, Integer fontSize, Integer containerHeight) {
        return (containerHeight + fontSize) / 2;
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
