package com.github.cuteluobo.advices;

import net.mamoe.mirai.console.command.Command;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.MemberCommandSender;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * 指令处理
 * @link AOP理解 https://my.oschina.net/itblog/blog/211693
 * @link AspectJ入门 https://www.jianshu.com/p/f9acae180f81
 * @author CuteLuoBo
 * @date 2021-04-15
 */

@Aspect
public class CommandAdvice {
    /**
     * 检查指令触发频率限制
     * @param point
     */
    @Before("execution(* com.github.cuteluobo.command.*Command.*(..))")
    public void CommandLimitCheck(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        //获取指令调用者
        if (args.length > 0) {
            Object arg0 = args[0];
            if (arg0 instanceof CommandSender) {
                long userId = ((CommandSender) arg0).getUser().getId();
                long groupId = 0;
                if (arg0 instanceof MemberCommandSender) {
                    groupId = ((MemberCommandSender) arg0).getGroup().getId();
                }
                Object targetObj = point.getTarget();
                //获取当前被调用的指令名称
                if (targetObj instanceof Command) {
                    Command command = (Command) targetObj;
                    String primary = command.getPrimaryName();
                }
            }
            //TODO 完成指令调用记录，超过限制后进行对应处理
        }

        point.proceed();
    }
}
