package com.github.cuteluobo.CommandHandlers;

import com.github.cuteluobo.CuteExtra;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.SimpleCommand;
import net.mamoe.mirai.console.command.descriptor.CommandArgumentContext;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import net.mamoe.mirai.console.permission.Permission;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public class NormalRollCommand extends JSimpleCommand {

    /**初始化*/
    public NormalRollCommand(){
        super(CuteExtra.INSTANCE,"nr", new String[]{"十连"}, null);
    }

    @Handler
    public void roll(CommandSender sender, String message){
        boolean up = message != null && message.toLowerCase().indexOf("up")!=-1;
        MessageChain chain = new MessageChainBuilder()
                .append(new At(sender.getUser().getId()))
                .append("10次"+(up?"(UP)":"")+"抽卡结果")
                .append("（）")
                .build();
        sender.sendMessage(chain);
    }
}
