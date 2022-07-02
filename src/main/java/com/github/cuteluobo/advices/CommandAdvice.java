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
import net.mamoe.mirai.console.command.CommandSenderOnMessage;
import net.mamoe.mirai.console.command.MemberCommandSender;
import net.mamoe.mirai.console.permission.AbstractPermitteeId;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.console.permission.Permittee;
import net.mamoe.mirai.console.permission.PermitteeId;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
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
        logger.debug("args:{}",point.getArgs());
        logger.debug("target:{}",point.getTarget());
        Long userId = null;
        Long groupId = null;
        String primary = null;
        Object[] args = point.getArgs();
        //确保可获取CommandSender-指令执行者
        if (args.length > 0) {
            //指令方法默认第一个参数为CommandSender，尝试转换
            Object arg0 = args[0];
            if (arg0 instanceof CommandSenderOnMessage) {
                CommandSenderOnMessage commandSender = (CommandSenderOnMessage) arg0;
                logger.debug("commandSender:{}",commandSender);
                logger.debug("commandSenderUser:{}",commandSender.getUser());
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
                        logger.debug("commandLimit:{}",commandLimit);
                        boolean noAdminPermission = GlobalConfig.ADMIN_ID != userId || !AuthUtils.isAdmin(commandSender.getPermitteeId());
//                        测试用 boolean noAdminPermission = true;
                        //有指令限制同时非管理员时，执行
                        if (commandLimit != null && noAdminPermission) {
                            logger.debug("enterCommandLimitCheck,userId:{},groupId:{},primary:{}",userId, groupId, primary);
                            //验证指令
                            CommandExecTemp commandExecTemp = CommandLimitUtils.getInstance().commandVerify(userId, groupId, primary, commandLimit);
                            logger.debug("commandExecTemp:{}",commandExecTemp);
                            //触发结果不为NONE，同时达到限制条件时，拒绝指令执行
                            if (commandExecTemp != null && !TriggerType.NONE.equals(commandExecTemp.getTrigger()) && commandExecTemp.getNumber() >= commandLimit.getCycleNum()) {
                                //第一次触发限制时，返回提示信息
                                if (commandLimit.getCycleNum().equals(commandExecTemp.getNumber())) {
                                    logger.debug("sendFirstLimitMessage");
                                    //发送提示信息
                                    MessageChain chain = new MessageChainBuilder()
                                            .append(new QuoteReply(commandSender.getFromEvent().getSource()))
//                                            .append(new At(userId)).append("\n")
                                            .append("触发消息限制：").append("\n")
                                            .append("指令： ").append(primary).append("-").append(commandLimit.getCycleNum()+"次 / ").append(commandLimit.getCycleSecond()+"秒").append("\n")
                                            .append("触发效果：").append("\n")
                                            .append(commandExecTemp.getTrigger().getDescription())
                                            .build();
                                    commandSender.sendMessage(chain);
                                }
                                //增加指令执行次数
                                CommandLimitUtils.getInstance().addCommandRecord(userId, groupId, primary);
                                logger.debug("rejectCommand:{}-ByCommandLimit",primary);
                                return false;
                            }
                            logger.debug("allowCommand:{}-hasLimited",primary);
                            //指令存在限制但未到限制条件时，正常执行
                            Object returnObject = point.proceed();
                            //获取指令返回结果，正确执行时记录
                            if (returnObject instanceof Boolean) {
                                if ((Boolean) returnObject) {
                                    CommandExecTemp commandExecTempExecute = CommandLimitUtils.getInstance().addCommandRecord(userId, groupId, primary);
                                    logger.debug("addCommandRightExecuteNum:{}",commandExecTempExecute);
                                }
                            }
                            return returnObject;
                        }
                        //普通执行
                        logger.debug("allowCommand:{}",primary);
                        return point.proceed();
                    }
                }
            }
        }
        //未进入拦截逻辑时，普通执行
        return point.proceed();
    }



}
