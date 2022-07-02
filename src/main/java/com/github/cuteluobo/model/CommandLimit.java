package com.github.cuteluobo.model;

/**
 * 指令限制-数据库储存
 * @author CuteLuoBo
 */
public class CommandLimit {
    /**
     * 记录ID
     */
    private Long id;

    /**
     * 群号
     */
    private Long groupId;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 指令名
     */
    private String primary;

    /**
     * 周期限制数量
     */
    private Integer cycleNum;

    /**
     * 周期时间定义
     */
    private Integer cycleSecond;

    /**
     * 状态码（达到周期后触发某种命令）
     */
    private Integer state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary == null ? null : primary.trim();
    }

    public Integer getCycleNum() {
        return cycleNum;
    }

    public void setCycleNum(Integer cycleNum) {
        this.cycleNum = cycleNum;
    }

    public Integer getCycleSecond() {
        return cycleSecond;
    }

    public void setCycleSecond(Integer cycleSecond) {
        this.cycleSecond = cycleSecond;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", groupId=").append(groupId);
        sb.append(", userId=").append(userId);
        sb.append(", primary=").append(primary);
        sb.append(", cycleNum=").append(cycleNum);
        sb.append(", cycleSecond=").append(cycleSecond);
        sb.append(", state=").append(state);
        sb.append("]");
        return sb.toString();
    }
}