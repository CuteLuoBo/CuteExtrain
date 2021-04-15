package com.github.cuteluobo.command;

import com.github.cuteluobo.CuteExtra;
import com.github.cuteluobo.service.Impl.YysRollServiceImpl;
import net.mamoe.mirai.console.command.*;
import net.mamoe.mirai.console.permission.Permission;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * https://github.com/mamoe/mirai-console/blob/master/docs/Commands.md
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public class RollCommand extends CompositeCommand {
    private static String[] secondCommand = {"抽卡","r"};

    /**指令初始化*/
    public RollCommand() {
        super(CuteExtra.INSTANCE, "roll", secondCommand, "/roll 10",Permission.getRootPermission(),null);
    }
    //TODO 增加抽卡指令

    /**
     * 常规抽卡
     * @param sender 消息发送者
     * @param rollNum
     * @param message
     */
    public void roll(@NotNull CommandSender sender,Integer rollNum, String message){
        boolean up = message != null && message.toLowerCase().contains("up");
        MessageChain chain = new MessageChainBuilder()
                .append(new At(sender.getUser().getId()))
                .append(YysRollServiceImpl.INSTANCE.rollText(rollNum, up, null, null, null).printResultText())
                .build();
        sender.sendMessage(chain);
    }
}
