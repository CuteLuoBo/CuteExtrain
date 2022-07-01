package com.github.cuteluobo.advices;

import com.github.cuteluobo.CuteExtra;
import com.github.cuteluobo.enums.TriggerType;
import com.github.cuteluobo.model.CommandLimit;
import com.github.cuteluobo.pojo.CommandExecTemp;
import com.github.cuteluobo.repository.CommandLimitRepository;
import com.github.cuteluobo.repository.GlobalConfig;
import com.github.cuteluobo.util.AuthUtils;
import com.github.cuteluobo.util.CommandLimitUtils;
import net.mamoe.mirai.console.command.Command;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.MemberCommandSender;
import net.mamoe.mirai.console.permission.AbstractPermitteeId;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.console.permission.Permittee;
import net.mamoe.mirai.console.permission.PermitteeId;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 指令处理
 * @link AOP理解 https://my.oschina.net/itblog/blog/211693
 * @link AspectJ入门 https://www.jianshu.com/p/f9acae180f81
 * @author CuteLuoBo
 * @date 2021-04-15
 */

@Aspect
public class CommandAdvice {
    Logger logger = LoggerFactory.getLogger(CommandAdvice.class);
    /**毫秒换算单位*/
    private final Integer MS_RATIO = 1000;
    /**
     * 检查指令触发频率限制
     * @param point
     */
    @Around("execution(* com.github.cuteluobo.command.*Command.*(..))")
    public Object commandLimitCheck(ProceedingJoinPoint point) throws Throwable {
        logger.debug("args:",point.getArgs());
        logger.debug("target:",point.getTarget());
        Long userId = null;
        Long groupId = null;
        String primary = null;
        Object[] args = point.getArgs();
        //确保可获取CommandSender-指令执行者
        if (args.length > 0) {
            //指令方法默认第一个参数为CommandSender，尝试转换
            Object arg0 = args[0];
            if (arg0 instanceof CommandSender) {
                CommandSender commandSender = (CommandSender) arg0;
                logger.debug("commandSenderUser:",commandSender.getUser());
                //可获取到用户的非控制台指令
                if (commandSender.getUser() != null) {
                    userId = commandSender.getUser().getId();
                    //为群内消息时，读取群ID
                    if (arg0 instanceof MemberCommandSender) {
                        groupId = ((MemberCommandSender) arg0).getGroup().getId();
                    }
                    Object targetObj = point.getTarget();
                    //获取当前被调用的指令名称
                    if (targetObj instanceof Command) {
                        Command command = (Command) targetObj;
                        primary = command.getPrimaryName();
                        //获取执行指令的限制
                        CommandLimit commandLimit = CommandLimitRepository.getInstance().getCommandLimit(groupId, userId, primary);
                        //存在限制时，判断执行记录，记录为管理员ID或具有插件管理权限
                        boolean adminPermission = AuthUtils.isAdmin(commandSender.getPermitteeId());
                        if (commandLimit != null || GlobalConfig.ADMIN_ID == userId || adminPermission) {
                            CommandLimitUtils commandLimitUntil = CommandLimitUtils.getInstance();
                            //TODO 增加对子命令限制的处理，储存格式：主命令.子命令
                            //获取当前记录的执行记录
                            CommandExecTemp commandExecTemp = commandLimitUntil.getCommandRecord(userId, groupId, primary);
                            if (commandExecTemp != null) {
                                //当在周期时间内
                                if ((commandExecTemp.getFirstTime() - System.currentTimeMillis()) / MS_RATIO < commandLimit.getCycleSecond()) {
                                    //指令执行次数达到限制时
                                    if (commandExecTemp.getNumber() >= commandLimit.getCycleNum()) {
                                        //获取触发效果类型
                                        TriggerType triggerType = commandExecTemp.getTrigger();
                                        //没有设置过触发效果时
                                        if (triggerType == null) {
                                            //获取指令默认触发的状态
                                            Integer state = commandLimit.getState();
                                            if (state == null) {
                                                state = 0;
                                            }
                                            //TODO 可增加更多触发效果
                                            switch (state) {
                                                default:
                                                    triggerType = TriggerType.IGNORE_TEN_MINUTE;
                                                    commandExecTemp.setTrigger(triggerType);
                                                    //设置触发效果结束时间
                                                    commandExecTemp.setTriggerEndTime(System.currentTimeMillis() + (long) triggerType.getSecond() * MS_RATIO);
                                                    break;
                                            }
                                            //发送提示信息
                                            MessageChain chain = new MessageChainBuilder()
                                                    .append(new At(userId))
                                                    .append(triggerType.getDescription())
                                                    .build();
                                            commandSender.sendMessage(chain);
                                            return false;
                                        }
                                        //当超过触发状态结束时间时，重置记录
                                        if (commandExecTemp.getTriggerEndTime() < System.currentTimeMillis()) {
                                            commandLimitUntil.clearCommandRecord(userId, groupId, primary);
                                        }
                                        //在触发状态内时，拒绝执行指令
                                        else {
                                            return false;
                                        }
                                    }
                                    //超出时间时，进行首次执行时间重置
                                }else {
                                    commandLimitUntil.clearCommandRecord(userId, groupId, primary);
                                }
                            }
                            //指令存在限制时，带记录执行
                            Object returnObject = point.proceed();
                            //获取指令返回结果，正确执行时记录
                            if (returnObject instanceof Boolean) {
                                if ((Boolean) returnObject) {
                                    CommandLimitUtils.getInstance().addCommandRecord(userId, groupId, primary);
                                }
                            }
                            return returnObject;
                        }
                        //普通执行
                        return point.proceed();
                    }
                }
            }
        }
        //未进入拦截逻辑时，普通执行
        return point.proceed();
    }

}
