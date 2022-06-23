package com.github.cuteluobo.util;

import com.github.cuteluobo.pojo.TextDrawData;

import java.awt.*;
import java.util.List;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

/**
 * 快速绘制容器类
 *
 * @author CuteLuoBo
 * @date 2022/6/23 16:02
 */
public class FastDrawContainerHelper {
    /**
     * 绘制画笔
     */
    private Graphics2D graphics2D;

    /**
     * 容器宽度
     */
    private int containerWidth;
    /**
     * 容器高度
     */
    private int containerHeight;
    /**
     * 绘制起始X点
     */
    private int startX;
    /**
     * 绘制起始Y点
     */
    private int startY;
    /**
     * 容器背景颜色
     */
    private Color backgroundColor;

    /**
     * 容器弧度
     */
    private int roundRate;
    /**
     * 是否启用容器边框
     */
    private boolean containerBorder;

    /**
     * 边框宽度
     */
    private int borderWidth = 1;
    /**
     * 边框颜色
     */
    private Color borderColor = Color.BLACK;


    /**
     * 文本绘制列表
     */
    List<TextDrawData> textDrawList = new ArrayList<>();

    public FastDrawContainerHelper(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;
    }

    /**
     * 绘制全部
     */
    public void drawAll() {
        //背景绘制
        drawContainerBackground();
        //边框绘制
        drawContainerBorder();
        //标题绘制
        drawAllText();
    }

    public void drawContainerBackground() {
        //背景颜色不为null时，填充背景
        if (backgroundColor != null) {
            //设置填充颜色
            graphics2D.setColor(backgroundColor);
            //圆角与直角矩形
            if (roundRate > 0) {
                graphics2D.fillRoundRect(startX, startY
                        , containerWidth, containerHeight
                        , roundRate, roundRate);
            } else {
                graphics2D.fillRect(startX, startY
                        , containerWidth, containerHeight);
            }
        }
    }

    public void drawContainerBorder() {
        if (containerBorder) {
            //设置边框颜色
            graphics2D.setColor(borderColor);
            //圆角与直角矩形
            if (roundRate > 0) {
                graphics2D.draw(new RoundRectangle2D.Float(startX, startY
                        , containerWidth, containerHeight
                        , roundRate, roundRate));
            } else {
                graphics2D.draw(new Rectangle(startX, startY
                        , containerWidth, containerHeight));
            }
        }
    }

    public void drawAllText() {
        if (textDrawList != null) {
            textDrawList.forEach(tdd ->{
                String text = tdd.getText();
                graphics2D.setFont(tdd.getFont().deriveFont((float) tdd.getSize()));
                graphics2D.setColor(tdd.getColor());
                //计算起始坐标-X
                int titleStartX ;
                //定义左边距时
                if (tdd.getLeftSpace() != null) {
                    titleStartX = startX + tdd.getLeftSpace();
                }
                //否则居中
                else {
                    titleStartX = startX + DrawUtils.calcTextCenterStartX(graphics2D.getFontMetrics(), text, containerWidth);
                }
                //计算Y结束坐标
                int titleEndY ;
                //定义顶部距离时
                if (tdd.getTopSpace() != null) {
                    titleEndY = startY + tdd.getTopSpace() + tdd.getSize();
                }
                //否则居中
                else {
                    titleEndY = startY + DrawUtils.calcTextVerticalCenterEndY(text, tdd.getSize(), containerHeight);
                }
                //绘制标题
                graphics2D.drawString(text,  titleStartX, titleEndY);
            });
        }
    }

    /**
     * 设置容器本体
     * @param containerWidth 容器宽度
     * @param containerHeight 容器高度
     * @param startX 起始X
     * @param startY 起始Y
     * @return
     */
    public void setContainer(int containerWidth, int containerHeight, int startX, int startY) {
        this.containerWidth = containerWidth;
        this.containerHeight = containerHeight;
        this.startX = startX;
        this.startY = startY;
    }

    public void setContainerBorder(Color borderColor,Integer borderWidth) {
        containerBorder = true;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
    }

    public int getRoundRate() {
        return roundRate;
    }

    public void setRoundRate(int roundRate) {
        this.roundRate = roundRate;
    }

    public Graphics2D getGraphics2D() {
        return graphics2D;
    }

    public void setGraphics2D(Graphics2D graphics2D) {
        this.graphics2D = graphics2D;
    }

    public int getContainerWidth() {
        return containerWidth;
    }

    public void setContainerWidth(int containerWidth) {
        this.containerWidth = containerWidth;
    }

    public int getContainerHeight() {
        return containerHeight;
    }

    public void setContainerHeight(int containerHeight) {
        this.containerHeight = containerHeight;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isContainerBorder() {
        return containerBorder;
    }

    public void setContainerBorder(boolean containerBorder) {
        this.containerBorder = containerBorder;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public List<TextDrawData> getTextDrawList() {
        return textDrawList;
    }

    public void setTextDrawList(List<TextDrawData> textDrawList) {
        this.textDrawList = textDrawList;
    }
}
