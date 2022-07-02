package com.github.cuteluobo.util;

import com.github.cuteluobo.enums.TriggerType;
import com.github.cuteluobo.model.CommandLimit;
import com.github.cuteluobo.pojo.CommandExecTemp;
import com.github.cuteluobo.pojo.UserCommandRecord;
import com.github.cuteluobo.repository.CommandLimitRepository;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

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

    /**
     * 对传入指令进行验证，并根据结果处理记录并返回触发结果
     * 用于命令执行前判断拦截，已执行次数到达限制条件时就会返回触发条件
     * @param userId        用户ID
     * @param groupId       群ID
     * @param primary       指令前缀
     * @param commandLimit  指令限制对象
     * @return 触发结果，为null时说明不设限
     */
    public CommandExecTemp commandVerify(Long userId, Long groupId, String primary, CommandLimit commandLimit) {
        //无指令限制时，直接返回null
        if (commandLimit == null) {
            return null;
        }
        //指令处理时间
        long nowTime = System.currentTimeMillis();
        //获取当前记录的执行记录
        CommandExecTemp commandExecTemp = getCommandRecord(userId, groupId, primary);
        if (commandExecTemp != null) {
            //当在周期时间内
            if ((commandExecTemp.getFirstTime() - nowTime) / 1000 < commandLimit.getCycleSecond()) {
                //指令执行次数达到限制时
                if (commandExecTemp.getNumber() >= commandLimit.getCycleNum()) {
                    //获取触发效果类型
                    TriggerType triggerType = commandExecTemp.getTrigger();
                    //没有设置过触发效果时
                    if (triggerType == null) {
                        //从指令限制对象中进行读取
                        triggerType = TriggerType.valueOf(commandLimit.getState());
                        //没有读取到结果时，视为不设限
                        if (triggerType == null) {
                            triggerType = TriggerType.NONE;
                        }
                        commandExecTemp.setTrigger(triggerType);
                        commandExecTemp.setTriggerEndTime(nowTime + (long) triggerType.getSecond() * 1000);
                    }
                    //当超过触发状态结束时间时，重置记录
                    if (commandExecTemp.getTriggerEndTime() < nowTime) {
                        clearCommandRecord(userId, groupId, primary);
                        commandExecTemp = getCommandRecord(userId, groupId, primary);
                    }
                    return commandExecTemp;
                }
            }
            //不在周期时间时，进行首次执行时间重置
            else {
                clearCommandRecord(userId, groupId, primary);
            }
        }
        return commandExecTemp;
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
