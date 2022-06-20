package com.github.cuteluobo.repository;

import cn.pomit.mybatis.ProxyHandlerFactory;
import com.github.cuteluobo.mapper.CommandLimitMapper;
import com.github.cuteluobo.model.CommandLimit;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 权限缓存类
 *
 * @author CuteLuoBo
 */
public class CommandLimitRepository {
    private CommandLimitMapper commandLimitMapper;
    private static CommandLimitRepository INSTANCE;
//    /**
//     * <群号，<个人号，权限对象>>
//     */
//    private Map<Long, Map<Long, Map<String,CommandLimit>>> tempMap = new HashMap<>();

    /**
     //     * <群号+个人号+权限名称，权限对象>>
     //     */
    private Map<String, CommandLimit> tempMap ;

    /**
     * ID中代表所有的替代符
     */
    public static final long ALL_ID = 0;
    /**
     * 指令中代表所有的替代符
     */
    public static final String ALL_COMMAND = "*";

    /**
     * 初始化，进行数据缓存
     */
    private CommandLimitRepository() {
        commandLimitMapper = ProxyHandlerFactory.getMapper(CommandLimitMapper.class);
        tempMap = new ConcurrentHashMap<>();
        List<CommandLimit> commandLimitList = commandLimitMapper.selectList();
        for (CommandLimit commandLimit :
                commandLimitList
        ) {
            Long groupId = commandLimit.getGroupId();
            Long userId = commandLimit.getUserId();
            String primary = commandLimit.getPrimary();
            tempMap.put(createKey(groupId,userId,primary), commandLimit);
        }
    }

    /**
     * 创建专用key
     * @param groupId 群ID
     * @param userId 用户ID
     * @param primary 指令字符串
     * @return 创建结果
     */
    public static String createKey(Long groupId, Long userId, String primary) {
        if (groupId == null) {
            groupId = ALL_ID;
        }
        if (userId == null) {
            userId = ALL_ID;
        }
        if (primary == null) {
            primary = ALL_COMMAND;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(groupId).append(".").append(userId).append(".").append(primary);
        return sb.toString();
    }


    /**
     * 获取单例
     *
     * @return 单例
     */
    public static CommandLimitRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommandLimitRepository();
        }
        return INSTANCE;
    }


    /**
     * 获取指令限制对象
     * @param groupId 群ID
     * @param userId 用户ID
     * @param primary 指令字符串
     * @return 指令限制对象，无对应结果时为null
     */
    public CommandLimit getCommandLimit(Long groupId, Long userId,String primary) {
        return tempMap.get(createKey(groupId, userId, primary));
    }

}
