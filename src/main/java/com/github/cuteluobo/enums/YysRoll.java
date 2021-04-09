package com.github.cuteluobo.enums;

/**
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public enum YysRoll {
    /**设置各抽卡概率，见官方:https://yys.163.com/news/notice/2017/05/01/25369_665793.html*/
    R("R",0.7875f),SR("SR",0.2f),SSR("SSR",0.01F),SP("SP",0.0025F);
    /**
     * 等级名称
     */
    private final String level;

    /**常规抽取概率*/
    private final Float rollProb;

    YysRoll(String level, Float rollProb) {
        this.level = level;
        this.rollProb = rollProb;
    }

    public String getLevel() {
        return level;
    }

    public Float getRollProb() {
        return rollProb;
    }
}
