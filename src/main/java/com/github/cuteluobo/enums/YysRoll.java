package com.github.cuteluobo.enums;

/**
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public enum YysRoll {
    R("R",0.7875f),SR("SR",0.2f),SSR("SSR",0.01F),SSR_SP_SKIN("SSR",0.001F),SP("SP",0.0025F);


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
