package com.github.cuteluobo.advices;

import com.github.cuteluobo.model.CommandLimit;
import com.github.cuteluobo.pojo.CommandExecTemp;
import com.github.cuteluobo.repository.CommandLimitRepository;
import com.github.cuteluobo.repository.GlobalConfig;
import com.github.cuteluobo.util.AuthUtils;
import com.github.cuteluobo.util.CommandLimitUtils;
import net.mamoe.mirai.console.command.Command;
import net.mamoe.mirai.console.command.CommandSenderOnMessage;
import net.mamoe.mirai.console.command.MemberCommandSender;
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
    //TODO 对于单独群的设置会导致全局生效，需要DEBUG排查
    //TODO 需要增加可对外操作和执行的指令类
    static Logger logger = LoggerFactory.getLogger(CommandAdvice.class);
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
            if (args[0] instanceof CommandSenderOnMessage && point.getTarget() instanceof Command) {
                CommandSenderOnMessage commandSender = (CommandSenderOnMessage) args[0];
                logger.debug("commandSender:{}", commandSender);
                logger.debug("commandSenderUser:{}", commandSender.getUser());
                Command command = (Command) point.getTarget();
                userId = commandSender.getUser().getId();
                //为群内消息时，读取群ID
                if (commandSender instanceof MemberCommandSender) {
                    groupId = ((MemberCommandSender) commandSender).getGroup().getId();
                }
                primary = command.getPrimaryName();
                boolean noAdminPermission = GlobalConfig.ADMIN_ID != userId || !AuthUtils.isAdmin(commandSender.getPermitteeId());
                //无管理权限时，检查指令限制
                if (noAdminPermission) {
                    CommandLimit commandLimit = CommandLimitRepository.getInstance().getCommandLimit(groupId, userId, primary);
                    int checkResult = CommandLimitUtils.getInstance().commandExecInterceptResult(userId, groupId, primary);
                    switch (checkResult) {
                        //-1 = 拒绝执行
                        case -1:return false;
                        //0 = 第一次触发限制，发送提示消息并拒绝执行
                        case 0:
                            //获取指令限制用于输出
                            CommandExecTemp commandExecTemp = CommandLimitUtils.getInstance().commandTryExec(userId, groupId, primary);
                            logger.debug("sendFirstLimitMessage");
                            //发送提示信息
                            MessageChain chain = new MessageChainBuilder()
                                    .append(new QuoteReply(commandSender.getFromEvent().getSource()))
                                    .append("触发消息限制：").append("\n")
                                    .append("指令： ").append(primary).append("-")
                                    .append(String.valueOf(commandLimit.getCycleNum())).append("次 / ")
                                    .append(String.valueOf(commandLimit.getCycleSecond())).append("秒").append("\n")
                                    .append("触发效果：").append("\n")
                                    .append(commandExecTemp.getTrigger().getDescription())
                                    .build();
                            commandSender.sendMessage(chain);
                            return false;
                        default:
                    }
                }
                Object returnObject = point.proceed();
                //获取指令返回结果，正确执行时记录
                if (returnObject instanceof Boolean) {
                    if ((Boolean) returnObject) {
                        CommandExecTemp commandExecTempExecute = CommandLimitUtils.getInstance().addCommandRecord(userId, groupId, primary);
                        logger.debug("addCommandRightExecuteNum:{}", commandExecTempExecute);
                    }
                }
                return returnObject;
            }
        }
        //未进入拦截逻辑时，普通执行
        return point.proceed();
    }



}
