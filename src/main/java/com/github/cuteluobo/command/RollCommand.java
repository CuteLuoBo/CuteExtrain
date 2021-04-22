package com.github.cuteluobo.command;

import cn.pomit.mybatis.ProxyHandlerFactory;
import com.github.cuteluobo.CuteExtra;
import com.github.cuteluobo.mapper.YysUnitMapper;
import com.github.cuteluobo.model.YysUnit;
import com.github.cuteluobo.pojo.RollUnit;
import com.github.cuteluobo.service.Impl.YysRollServiceImpl;
import net.mamoe.mirai.console.command.*;
import net.mamoe.mirai.console.permission.Permission;
import net.mamoe.mirai.message.MessageReceipt;
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
    private static String[] secondCommand = {"阴阳师抽卡","yr"};

    /**指令初始化*/
    public RollCommand() {
        super(CuteExtra.INSTANCE, "yroll", secondCommand, "/yroll 10",Permission.getRootPermission(),null);
    }
    //TODO 增加抽卡指令

    /**
     * 常规抽卡
     * @param sender 消息发送者
     * @param rollNum 抽卡次数
     * @param message UP/no UP
     */
    @SubCommand({"normal","n","普通"})
    public Boolean normal(@NotNull CommandSender sender,Integer rollNum, String message){
        boolean up = message != null && message.toLowerCase().contains("up");
        MessageChain chain = new MessageChainBuilder()
                .append(new At(sender.getUser().getId()))
                .append(YysRollServiceImpl.INSTANCE.rollText(rollNum, up, null, null, null).printResultText())
                .build();
        sender.sendMessage(chain);
        return true;
    }

    /**
     * 指定抽卡
     * @param sender 消息发送者
     * @param arg1   式神名称
     * @param arg2  是否全图
     * @return
     */
    @SubCommand({"assign","a","追梦"})
    public Boolean assign(@NotNull CommandSender sender,String arg1,String arg2){
        YysUnitMapper yysUnitMapper = ProxyHandlerFactory.getMapper(YysUnitMapper.class);
        if (arg1 == null) {
            MessageChain chain = new MessageChainBuilder()
                    .append(new At(sender.getUser().getId()))
                    .append("你没有输入指定UP的式神名称")
                    .build();
            sender.sendMessage(chain);
            return false;
        }
        try {
            //获取指定式神
            YysUnit yysUnit = yysUnitMapper.selectOneByName(arg1, false);
            if (yysUnit == null) {
                sender.sendMessage(buildNormalMessage(sender.getUser().getId(),"没有找到对应式神"));
            }
            //全图鉴BUFF
            boolean allBuff = arg2 != null && arg2.contains("全图");
            //发送信息
            MessageChain chain = new MessageChainBuilder()
                    .append(new At(sender.getUser().getId()))
                    .append(YysRollServiceImpl.INSTANCE.rollTextForSpecifyUnit(new RollUnit(yysUnit),allBuff).printResultText())
                    .build();
            sender.sendMessage(chain);
            return true;
        } catch (Exception e) {
            sender.sendMessage(buildNormalMessage(sender.getUser().getId(),"查询式神错误"));
            throw e;
        }
    }

    private MessageChain buildNormalMessage(Long userId,String message) {
        MessageChain chain = new MessageChainBuilder()
                .append(new At(userId))
                .append("你没有输入指定UP的式神名称")
                .build();
        return chain;
    }
}
