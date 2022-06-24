package com.github.cuteluobo.pojo;

import java.math.BigDecimal;

/**
 * 抽取后的结果
 * @author CuteLuoBo
 */
public class RollResultUnit extends RollUnit {

    public RollResultUnit(Integer id,Long officialId, String level, String name) {
        super(id,officialId,level,name);
    }
    public RollResultUnit(RollUnit rollUnit) {
        super(rollUnit);
    }

    private Boolean up = false;

    /**
     * 特殊状态
     */
    private Boolean special = false;
    /**
     * 出货时的抽取次数
     */
    private Integer sequence;

    /**
     * 当前阶级的抽取概率
     */
    private BigDecimal levelRollRate;

    /**
     * 当前式神在同阶级中抽取概率
     */
    private BigDecimal unitRollRate;

    public Boolean getSpecial() {
        return special;
    }

    public void setSpecial(Boolean special) {
        this.special = special;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Boolean getUp() {
        return up;
    }

    public void setUp(Boolean up) {
        this.up = up;
    }

    public BigDecimal getLevelRollRate() {
        return levelRollRate;
    }

    public void setLevelRollRate(BigDecimal levelRollRate) {
        this.levelRollRate = levelRollRate;
    }

    public BigDecimal getUnitRollRate() {
        return unitRollRate;
    }

    public void setUnitRollRate(BigDecimal unitRollRate) {
        this.unitRollRate = unitRollRate;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("第").append(sequence).append("抽:")
                .append(getLevel()).append(" ").append(!special?getName():getSpecialName()).append(up?"(UP)":"");
        return sb.toString();
    }
}
