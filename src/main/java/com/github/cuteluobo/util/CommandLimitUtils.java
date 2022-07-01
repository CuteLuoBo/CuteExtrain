package com.github.cuteluobo.util;

import com.github.cuteluobo.pojo.CommandExecTemp;
import com.github.cuteluobo.pojo.UserCommandRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * 指令限制器
 * @author CuteLuoBo
 * @date 2021-04-15
 */
public class CommandLimitUtils {
    private static CommandLimitUtils INSTANCE ;

    private Map<Long, UserCommandRecord> recordMap;

    private CommandLimitUtils(){
        recordMap = new HashMap<>();
    }


    /**
     * 获取单例
     * @return 单例
     */
    public static CommandLimitUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommandLimitUtils();
        }
        return INSTANCE;
    }

    /**
     * 获取指令执行记录
     * @param userId 用户ID
     * @param groupId 群ID，为Null时获取私聊指令
     * @param commandString 指令字符串
     * @return 当前的指令执行数据，如果未有记录则返回Null
     */
    public CommandExecTemp getCommandRecord(Long userId, Long groupId, String commandString) {
        UserCommandRecord userCommandRecord = recordMap.get(userId);
        if (userCommandRecord == null) {
            userCommandRecord = new UserCommandRecord(userId);
            recordMap.put(userId, userCommandRecord);
        }
        Map<String, CommandExecTemp> commandMap;
        if (groupId != null) {
            Map<Long, Map<String, CommandExecTemp>> groupCommandMap = userCommandRecord.getGroupCommandMap();
            if (groupCommandMap == null) {
                return null;
            }else {
                commandMap = groupCommandMap.get(groupId);
            }
        } else {
            commandMap = userCommandRecord.getPrivateCommandMap();
        }
        if (commandMap == null) {
            return null;
        } else {
            return commandMap.get(commandString);
        }
    }

    /**
     * 获取指令执行记录
     * @param userId 用户ID
     * @param commandString 指令字符串
     * @return 当前的指令执行数据，如果未有记录则返回Null
     */
    public CommandExecTemp getCommandRecord(Long userId, String commandString) {
        return getCommandRecord(userId, null, commandString);
    }

    /**
     * 增加指令执行记录
     * @param userId 用户ID
     * @param groupId 群ID，为Null时记录私聊指令
     * @param commandString 指令字符串
     * @return 更新后的指令执行数据
     */
    public CommandExecTemp addCommandRecord(Long userId, Long groupId, String commandString) {
        //获取当前用户指令记录，不存在时进行初始化
        UserCommandRecord userCommandRecord = recordMap.get(userId);
        if (userCommandRecord == null) {
            userCommandRecord = new UserCommandRecord(userId);
            recordMap.put(userId, userCommandRecord);
        }
        Map<String, CommandExecTemp> commandMap;
        //记录群聊指令触发记录
        if (groupId != null) {
            Map<Long, Map<String, CommandExecTemp>> groupCommandMap = userCommandRecord.getGroupCommandMap();
            if (groupCommandMap == null) {
                groupCommandMap = new HashMap<>(10);
            }
            commandMap = addCommandExecNumber(groupCommandMap.get(groupId),commandString);
            groupCommandMap.put(groupId, commandMap);
        }
        //记录私聊指令触发记录
        else {
            commandMap = addCommandExecNumber(userCommandRecord.getPrivateCommandMap(),commandString);
        }
        return commandMap.get(commandString);
    }

    public CommandExecTemp addCommandRecord(Long userId, String commandString) {
        return addCommandRecord(userId, null, commandString);
    }

    /**
     * 清除指令
     * @param userId    用户ID
     * @param groupId 群ID
     * @param commandString 指令字符串
     */
    public void clearCommandRecord(Long userId, Long groupId, String commandString) {
        UserCommandRecord userCommandRecord = recordMap.get(userId);
        if (userCommandRecord == null) {
            userCommandRecord = new UserCommandRecord(userId);
            recordMap.put(userId, userCommandRecord);
        }
        if (groupId != null) {
            Map<Long, Map<String, CommandExecTemp>> groupCommandMap = userCommandRecord.getGroupCommandMap();
            if (groupCommandMap != null) {
                groupCommandMap.remove(groupId);
            }
        }else {
            Map<String, CommandExecTemp> commandMap = userCommandRecord.getPrivateCommandMap();
            if (commandMap != null) {
                commandMap.remove(commandString);
            }
        }
    }


    /**
     * 初始化指令map并增加指令执行次数
     * @param commandMap 指定指令map，为Null时在内部初始化
     * @param commandString 指令字符串
     * @return 处理后的指令map
     */
    private Map<String, CommandExecTemp> addCommandExecNumber(Map<String, CommandExecTemp> commandMap, String commandString) {
        if (commandMap == null) {
            commandMap = new HashMap<>(10);
        }
        //获取已有对应指令执行次数，为Null时进行初始化
        CommandExecTemp commandExecTemp = commandMap.get(commandString);
        if (commandExecTemp == null) {
            commandExecTemp = new CommandExecTemp(System.currentTimeMillis());
        }
        //增加指令执行次数
        commandExecTemp.setNumber(commandExecTemp.getNumber()+1);
        commandMap.put(commandString, commandExecTemp);
        return commandMap;
    }


    public Map<Long, UserCommandRecord> getRecordMap() {
        return recordMap;
    }

    public void setRecordMap(Map<Long, UserCommandRecord> recordMap) {
        this.recordMap = recordMap;
    }

    public void clearAllRecord() {
        this.recordMap.clear();
    }
}
