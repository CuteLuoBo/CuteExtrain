package com.github.cuteluobo.enums;

/**
 * 抽卡游戏类型
 *
 * @author CuteLuoBo
 * @date 2022/6/23 21:55
 */
public enum RollGameType {
    /**阴阳师*/
    yys("yys");
    private String text;

    RollGameType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
