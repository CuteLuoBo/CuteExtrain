package com.github.cuteluobo.command;

import com.github.cuteluobo.CuteExtra;
import com.github.cuteluobo.service.Impl.YysRollServiceImpl;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.MemberCommandSender;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import net.mamoe.mirai.console.permission.Permission;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.aspectj.lang.annotation.Aspect;

/**
 * 常规抽卡指令-废弃
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public class NormalRollCommand extends JSimpleCommand {

    private static String[] secondCommand = {"抽卡","r"};

    /**初始化*/
    public NormalRollCommand() {
        super(CuteExtra.INSTANCE,"roll",secondCommand,Permission.getRootPermission());
        super.setDescription("常规抽卡");
    }

    @Handler
    public Boolean roll(CommandSender sender,Integer rollNum, String message){
        boolean up = message != null && message.toLowerCase().contains("up");
        MessageChain chain = new MessageChainBuilder()
                .append(new At(sender.getUser().getId()))
                .append(YysRollServiceImpl.INSTANCE.rollText(10, up, null, null, null).printResultText())
                .build();
        sender.sendMessage(chain);
        return true;
    }

    @Handler
    public Boolean roll(CommandSender sender,Integer rollNum){
        return roll(sender,rollNum, null);
    }
}
