package com.github.cuteluobo.repository;

import cn.pomit.mybatis.ProxyHandlerFactory;
import com.github.cuteluobo.mapper.CommandLimitMapper;
import com.github.cuteluobo.model.CommandLimit;
import com.github.cuteluobo.untils.CommandLimitUntil;
import net.mamoe.mirai.console.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限缓存类
 * @author CuteLuoBo
 */
public class CommandLimitRepository {
    private CommandLimitMapper commandLimitMapper;
    private static CommandLimitRepository INSTANCE ;
    /**
     * <群号，<个人号，权限对象>>
     */
    private Map<Long, Map<Long, CommandLimit>> tempMap = new HashMap<>();


    /**
     * 初始化，进行数据缓存
     */
    private CommandLimitRepository(){
        commandLimitMapper = ProxyHandlerFactory.getMapper(CommandLimitMapper.class);
        List<CommandLimit> commandLimitList = commandLimitMapper.selectList();
        for (CommandLimit commandLimit :
                commandLimitList
        ) {
            //WRAN 返回Null时可能会有问题？
            long groupId = commandLimit.getGroupId();
            Map<Long, CommandLimit> groupLimitMap = tempMap.get(groupId);
            if (groupLimitMap == null) {
                groupLimitMap = new HashMap<>();
            }
            groupLimitMap.put(commandLimit.getUserId(), commandLimit);
            tempMap.put(groupId, groupLimitMap);
        }
    }


    /**
     * 获取单例
     * @return 单例
     */
    public static CommandLimitRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommandLimitRepository();
        }
        return INSTANCE;
    }
}
