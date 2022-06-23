package com.github.cuteluobo.util;

import com.github.cuteluobo.pojo.TextDrawData;

import java.awt.*;
import java.util.List;

/**
 * 快速绘制容器构建类
 *
 * @author CuteLuoBo
 * @date 2022/6/23 16:12
 */
public class FastDrawContainerHelperBuilder {

    FastDrawContainerHelper fastDrawContainerHelper;

    public FastDrawContainerHelperBuilder(Graphics2D graphics2D) {
        fastDrawContainerHelper = new FastDrawContainerHelper(graphics2D);

    }

    public FastDrawContainerHelper build() {
        return fastDrawContainerHelper;
    }

    /**
     * 设置容器本体
     * @param containerWidth
     * @param containerHeight
     * @param startX
     * @param startY
     * @return
     */
    public FastDrawContainerHelperBuilder setContainer(int containerWidth, int containerHeight, int startX, int startY) {
        fastDrawContainerHelper.setContainer(containerWidth, containerHeight, startX, startY);
        return this;
    }

    /**
     * 设置荣光其背景颜色
     * @param backgroundColor
     * @return
     */
    public FastDrawContainerHelperBuilder setContainerBackground(Color backgroundColor) {
        fastDrawContainerHelper.setBackgroundColor(backgroundColor);
        return this;
    }

    /**
     * 设置容器圆角弧度
     * @param roundRate
     * @return
     */
    public FastDrawContainerHelperBuilder setContainerRoundRate(int roundRate) {
        fastDrawContainerHelper.setRoundRate(roundRate);
        return this;
    }

    /**
     * 设置容器边框
     * @param borderColor
     * @param borderWidth
     * @return
     */
    public FastDrawContainerHelperBuilder setContainerBorder(Color borderColor,Integer borderWidth) {
        fastDrawContainerHelper.setContainerBorder(borderColor, borderWidth);
        return this;
    }

    /**
     * 设置容器标题
     * @param title          标题文本
     * @param titleFont      标题字体
     * @param titleFontSize  标题字体大小
     * @param titleFontColor 标题颜色
     * @param titleLeftSpace 标题距左距离，null时居中
     * @param titleTopSpace  标题距顶距离，null时居中
     * @return this
     */
    public FastDrawContainerHelperBuilder addContainerTitle(String title, Font titleFont, Integer titleFontSize, Color titleFontColor, Integer titleLeftSpace, Integer titleTopSpace) {
        TextDrawData textDrawData = new TextDrawData(title, titleFont, titleFontSize, titleFontColor, titleLeftSpace, titleTopSpace);
        fastDrawContainerHelper.getTextDrawList().add(textDrawData);
        return this;
    }

    /**
     * 设置容器文本列表（覆盖）
     * @param textDrawDataList 文本列表
     * @return this
     */
    public FastDrawContainerHelperBuilder setTextDrawDataList(List<TextDrawData> textDrawDataList) {
        fastDrawContainerHelper.setTextDrawList(textDrawDataList);
        return this;
    }
}
