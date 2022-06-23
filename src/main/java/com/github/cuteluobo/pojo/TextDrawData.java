package com.github.cuteluobo.pojo;

import java.awt.*;

/**
 * 文字绘制数据
 *
 * @author CuteLuoBo
 * @date 2022/6/23 17:14
 */
public class TextDrawData {

    /**
     * 文本
     */
    private String text;

    /**
     * 字体
     */
    private Font font;

    /**
     * 字体大小
     */
    private int size = 12;

    /**
     * 颜色
     */
    private Color color = Color.BLACK;

    /**
     * 左距离，为null时为居中模式
     */
    private Integer leftSpace;

    /**
     * 顶部距离，为null时为居中模式
     */
    private Integer topSpace;

    public TextDrawData(String text, Font font) {
        this.text = text;
        this.font = font;
    }

    public TextDrawData(String text, Font font, int size, Color color) {
        this.text = text;
        this.font = font;
        this.size = size;
        this.color = color;
    }

    public TextDrawData(String text, Font font, int size, Color color, Integer leftSpace, Integer topSpace) {
        this.text = text;
        this.font = font;
        this.size = size;
        this.color = color;
        this.leftSpace = leftSpace;
        this.topSpace = topSpace;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Integer getLeftSpace() {
        return leftSpace;
    }

    public void setLeftSpace(Integer leftSpace) {
        this.leftSpace = leftSpace;
    }

    public Integer getTopSpace() {
        return topSpace;
    }

    public void setTopSpace(Integer topSpace) {
        this.topSpace = topSpace;
    }
}
