package com.github.cuteluobo.Pojo;

/**
 * 抽取后的结果
 * @author CuteLuoBo
 */
public class RollResultUnit extends RollUnit {

    private Boolean up;

    /**
     * 特殊状态
     */
    private Boolean special;
    /**
     * 出货时的抽取次数
     */
    private Integer sequence;

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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RollResultUnit{");
        sb.append("第").append(sequence).append("抽:")
                .append(getLevel()).append(" ").append(getName()).append(up?"(UP)":"");
        return sb.toString();
    }
}
