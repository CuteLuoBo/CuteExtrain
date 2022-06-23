package com.github.cuteluobo.enums;

/**
 * 抽卡模式
 *
 * @author CuteLuoBo
 * @date 2022/6/23 19:02
 */
public enum RollModel {
    /***/
    normal("普通抽卡"),specify("活动定向抽卡"),assign("普通定向抽卡"),special("特殊抽卡");
    /**名称*/
    String text;

    RollModel(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
