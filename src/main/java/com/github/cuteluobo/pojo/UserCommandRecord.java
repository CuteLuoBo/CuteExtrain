package com.github.cuteluobo.pojo;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 用户执行指令记录类
 *
 * @author CuteLuoBo
 * @date 2021-04-22
 */
public class UserCommandRecord {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 群内命令执行次数
     * 群聊指令map<群号，<指令名，执行次数缓存类>>
     */
    private Map<Long, Map<String, CommandExecTemp>> groupCommandMap;

    /**
     * 私聊指令map<指令名，执行次数缓存类>
     */
    private Map<String, CommandExecTemp> privateCommandMap;

    public UserCommandRecord(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Map<Long, Map<String, CommandExecTemp>> getGroupCommandMap() {
        return groupCommandMap;
    }

    public void setGroupCommandMap(Map<Long, Map<String, CommandExecTemp>> groupCommandMap) {
        this.groupCommandMap = groupCommandMap;
    }

    public Map<String, CommandExecTemp> getPrivateCommandMap() {
        return privateCommandMap;
    }

    public void setPrivateCommandMap(Map<String, CommandExecTemp> privateCommandMap) {
        this.privateCommandMap = privateCommandMap;
    }
}
