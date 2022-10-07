package com.github.cuteluobo.enums;

import java.math.BigDecimal;

/**
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public enum YysRoll {
    /**设置各抽卡概率，见官方:https://yys.163.com/news/notice/2017/05/01/25369_665793.html*/
    N("N",0f,"0"),R("R",0.7875f,"0.7875")
    ,SR("SR",0.2f,"0.2"),SSR("SSR",0.01F,"0.01")
    ,SP("SP",0.0025F,"0.0025")
    ;
    /**
     * 等级名称
     */
    private final String level;

    /**常规抽取概率*/
    private final float rollProb;

    /**
     * 抽取概率字符串-用于BigDecimal初始化
     */
    private final String rollProbString;

    YysRoll(String level, float rollProb, String rollProbString) {
        this.level = level;
        this.rollProb = rollProb;
        this.rollProbString = rollProbString;
    }

    public String getRollProbString() {
        return rollProbString;
    }

    public String getLevel() {
        return level;
    }

    public float getRollProb() {
        return rollProb;
    }
}
