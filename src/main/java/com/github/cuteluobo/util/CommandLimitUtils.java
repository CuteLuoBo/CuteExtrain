package com.github.cuteluobo.util;

import com.github.cuteluobo.enums.TriggerType;
import com.github.cuteluobo.model.CommandLimit;
import com.github.cuteluobo.pojo.CommandExecTemp;
import com.github.cuteluobo.pojo.UserCommandRecord;
import com.github.cuteluobo.repository.CommandLimitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 指令限制器
 * @author CuteLuoBo
 * @date 2021-04-15
 */
public class CommandLimitUtils {
    private static Logger logger = LoggerFactory.getLogger(CommandLimitUtils.class);
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
        //没有执行记录时，创建一个新的记录对象
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
     * 指令当前周期内尝试执行，返回执行后将触发的效果
     * 同时在进入新周期时进行执行次数初始化
     * @param userId        用户ID
     * @param groupId       群ID
     * @param primary       指令前缀
     * @return 返回执行后将触发的效果，为null时说明不设限
     */
    public CommandExecTemp commandTryExec(Long userId, Long groupId, String primary) {
        CommandLimit commandLimit = CommandLimitRepository.getInstance().getCommandLimit(groupId, userId, primary);
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
            if ((nowTime - commandExecTemp.getFirstTime() ) / 1000 < commandLimit.getCycleSecond()) {
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
            //不在周期时间时，进行首次执行时间重置并返回最新结果
            else {
                clearCommandRecord(userId, groupId, primary);
                return getCommandRecord(userId, groupId, primary);
            }
        }

        return commandExecTemp;
    }

    /**
     * 指令执行拦截结果
     * 对于拒绝执行，将自动累计一次执行次数，避免重复状态0
     * @param userId 用户ID
     * @param groupId 群ID
     * @param primary 执行指令
     * @return -1=拒绝执行，0=第一次到达周期限制，1=允许执行
     */
    public int commandExecInterceptResult(Long userId, Long groupId, String primary) {

        CommandLimit commandLimit = CommandLimitRepository.getInstance().getCommandLimit(groupId,userId, primary);
        logger.debug("commandLimit:{}", commandLimit);
        //有指令限制时，执行验证
        if (commandLimit != null) {
            logger.debug("enterCommandLimitCheck,userId:{},groupId:{},primary:{}", userId, groupId, primary);
            //验证指令
            CommandExecTemp commandExecTemp = commandTryExec(userId, groupId, primary);
            logger.debug("commandExecTemp:{}", commandExecTemp);
            //触发结果不为NONE，同时达到限制条件时，增加执行次数，拒绝指令执行
            if (commandExecTemp != null && !TriggerType.NONE.equals(commandExecTemp.getTrigger()) && commandExecTemp.getNumber() >= commandLimit.getCycleNum()) {
                //增加执行次数，避免重复状态0
                addCommandRecord(userId, groupId, primary);
                //第一次触发限制时，返回提示信息
                if (commandLimit.getCycleNum().equals(commandExecTemp.getNumber()-1)) {
                    return 0;
                }
                logger.debug("rejectCommand:{}-ByCommandLimit", primary);
                return -1;
            }
            logger.debug("allowCommand:{}-hasLimited", primary);
            //指令存在限制但未到限制条件时，正常执行
            return 1;
        }
        //普通执行
        logger.debug("allowCommand:{}", primary);
        logger.debug("noLimitAllow,userId:{},groupId:{},primary:{}", userId, groupId, primary);
        return 1;
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
