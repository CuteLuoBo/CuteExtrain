package com.github.cuteluobo.command;

import cn.pomit.mybatis.ProxyHandlerFactory;
import com.github.cuteluobo.CuteExtra;
import com.github.cuteluobo.mapper.YysUnitMapper;
import com.github.cuteluobo.model.YysUnit;
import com.github.cuteluobo.pojo.RollUnit;
import com.github.cuteluobo.service.Impl.YysRollServiceImpl;
import kotlin.reflect.KClass;
import net.mamoe.mirai.console.command.*;
import net.mamoe.mirai.console.command.descriptor.CommandArgumentContext;
import net.mamoe.mirai.console.command.descriptor.CommandValueArgumentParser;
import net.mamoe.mirai.console.command.descriptor.SimpleCommandArgumentContext;
import net.mamoe.mirai.console.permission.Permission;
import net.mamoe.mirai.console.permission.PermissionId;
import net.mamoe.mirai.console.permission.PermissionRegistryConflictException;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static com.github.cuteluobo.CuteExtra.basePermission;

/**
 * https://github.com/mamoe/mirai-console/blob/master/docs/Commands.md
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public class RollCommand extends CompositeCommand {
    private static String[] secondCommand = {"阴阳师抽卡","yr"};
    private static final String PRIMARY = "yroll";

    /**指令初始化*/
    public RollCommand() throws PermissionRegistryConflictException {
        super(CuteExtra.INSTANCE, PRIMARY, secondCommand, "/yroll n 10", PermissionService.getInstance().register(new PermissionId(CuteExtra.PLUGIN_ID,PRIMARY),"阴阳师抽卡权限",CuteExtra.INSTANCE.getParentPermission()), SimpleCommandArgumentContext.EMPTY);
    }

    /**
     * 常规抽卡
     * @param sender 消息发送者
     * @param rollNum 抽卡次数
     * @param message UP/no UP
     */
    @SubCommand({"normal","n","普通"})
    public Boolean normal(@NotNull CommandSender sender,Integer rollNum, String message){
        boolean up = message != null && message.toLowerCase().contains("up");
        //设置最大单次500抽
        rollNum = Math.min(rollNum, 500);
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        User user = sender.getUser();
        if (user != null) {
            chainBuilder.append(new At(user.getId()));
            chainBuilder.append("\n");
        }
        chainBuilder.append(rollNum+"抽"+(up?"UP":"")+ "模拟抽卡结果：\n");
        chainBuilder.append(YysRollServiceImpl.INSTANCE.rollText(rollNum, up, null, null, null).printResultText());
        sender.sendMessage(chainBuilder.build());
        return true;
    }

    /**
     * 常规抽卡
     * @param sender 消息发送者
     * @param rollNum 抽卡次数
     */
    @SubCommand({"normal","n","普通"})
    public Boolean normal(@NotNull CommandSender sender,Integer rollNum){
        return normal(sender, rollNum, null);
    }

    /**
     * 指定抽卡
     * @param sender 消息发送者
     * @param unitName   式神名称
     * @param isAll  是否全图
     * @return
     */
    @SubCommand({"assign","a","定向"})
    public Boolean assign(@NotNull  CommandSender sender, String unitName, String isAll){
        YysUnitMapper yysUnitMapper = ProxyHandlerFactory.getMapper(YysUnitMapper.class);
        if (unitName == null) {
            sender.sendMessage(buildNormalMessage(sender, "你没有输入指定UP的式神名称"));
            return false;
        }
        try {
            //获取指定式神
            YysUnit yysUnit = yysUnitMapper.selectOneByName(unitName, true);
            if (yysUnit == null) {
                sender.sendMessage(buildNormalMessage(sender,"没有找到对应式神"));
                return false;
            }
            //全图鉴BUFF
            boolean allBuff = isAll != null && isAll.contains("全图");
            //发送信息


            MessageChainBuilder chainBuilder = new MessageChainBuilder();
            User user = sender.getUser();
            if (user != null) {
//                chainBuilder.append(MessageSource.quote(messageEvent.getMessage()));
                chainBuilder.append(new At(user.getId()));
                chainBuilder.append("\n");
            }
            chainBuilder.append("定向概率UP："+yysUnit.getName()+"("+(allBuff?"全图鉴":"非全图")+") 模拟抽卡结果：\n");
            chainBuilder.append(YysRollServiceImpl.INSTANCE.rollTextForSpecifyUnit(new RollUnit(yysUnit), allBuff).printResultText());
            sender.sendMessage(chainBuilder.build());
            return true;
        } catch (Exception e) {
            sender.sendMessage(buildNormalMessage(sender,"查询式神错误"));
            throw e;
        }
    }

    /**
     * 指定抽卡
     * @param sender 消息发送者
     * @param arg1   式神名称
     * @return
     */
    @SubCommand({"assign","a","定向"})
    public Boolean assign(@NotNull  CommandSender sender,String arg1){
        return assign(sender, arg1, null);
    }

    private MessageChain buildNormalMessage(CommandSender sender,String message) {
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        User user = sender.getUser();
        if (user != null) {
            chainBuilder.append(new At(user.getId()));
            chainBuilder.append("\n");
        }
        chainBuilder.append(message);
        return chainBuilder.build();
    }
}
