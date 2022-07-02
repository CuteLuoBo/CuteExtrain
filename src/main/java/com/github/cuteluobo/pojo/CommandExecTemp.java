package com.github.cuteluobo.pojo;

import com.github.cuteluobo.enums.TriggerType;

/**
 * 指令执行缓存
 * @author CuteLuoBo
 */
public class CommandExecTemp {
    /**
     * 指令首次执行时间
     */
    private Long firstTime;

    /**
     * 指令执行次数
     */
    private int number = 0;

    /**
     * 触发状态类型
     */
    private TriggerType trigger = null;

    /**
     * 触发结束时间
     */
    private Long triggerEndTime;

    public CommandExecTemp(Long firstTime) {
        this.firstTime = firstTime;
    }

    public TriggerType getTrigger() {
        return trigger;
    }

    public void setTrigger(TriggerType trigger) {
        this.trigger = trigger;
    }

    public Long getTriggerEndTime() {
        return triggerEndTime;
    }

    public void setTriggerEndTime(Long triggerEndTime) {
        this.triggerEndTime = triggerEndTime;
    }

    public Long getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Long firstTime) {
        this.firstTime = firstTime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CommandExecTemp{");
        sb.append("firstTime=").append(firstTime);
        sb.append(", number=").append(number);
        sb.append(", trigger=").append(trigger);
        sb.append(", triggerEndTime=").append(triggerEndTime);
        sb.append('}');
        return sb.toString();
    }
}
