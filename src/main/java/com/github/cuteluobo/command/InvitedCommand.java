package com.github.cuteluobo.command;

import com.github.cuteluobo.CuteExtra;
import com.github.cuteluobo.repository.GlobalConfig;
import com.github.cuteluobo.repository.InvitedEventRepository;
import com.github.cuteluobo.service.Impl.YysRollServiceImpl;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.CompositeCommand;
import net.mamoe.mirai.console.command.descriptor.SimpleCommandArgumentContext;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import net.mamoe.mirai.console.permission.Permission;
import net.mamoe.mirai.console.permission.PermissionId;
import net.mamoe.mirai.console.permission.PermissionRegistryConflictException;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.data.RequestEventData;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

/**
 * @author CuteLuoBo
 * @date 2022/6/24 16:55
 */
public class InvitedCommand  extends CompositeCommand {
    private static final String[] SECOND_COMMAND = {"i","邀请"};
    private static final String PRIMARY = "invited";

    public InvitedCommand() throws PermissionRegistryConflictException {
        super(CuteExtra.INSTANCE, PRIMARY, SECOND_COMMAND
                ,"/i g 1234567 n -拒绝1234567群邀请"
                , PermissionService.getInstance().register(new PermissionId(CuteExtra.PLUGIN_ID,PRIMARY)
                        ,"机器人邀请处理权限"
                        ,CuteExtra.INSTANCE.getParentPermission())
                , SimpleCommandArgumentContext.EMPTY);
    }

    /**
     * 群邀请消息处理
     * @param sender 发送者
     * @param groupId
     * @param opinion
     * @return
     */
    @SubCommand({"group","g"})
    public Boolean group(CommandSender sender, long groupId,Boolean opinion){
        if (sender.getUser() != null && GlobalConfig.ADMIN_ID == sender.getUser().getId()) {
            //初始化消息builder
            MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
            //添加at
            messageChainBuilder.append(new At(sender.getUser().getId())).append("\n");
            //获取缓存事件
            BotInvitedJoinGroupRequestEvent event = InvitedEventRepository.INSTANCE.getGroupInvitedJoinEventMap().get(groupId);
            if (event == null) {
                messageChainBuilder.append("事件ID无效或已过期");
            }else {
                if (opinion) {
                    event.accept();
                    messageChainBuilder.append("群邀请已通过");
                    InvitedEventRepository.INSTANCE.getGroupInvitedJoinEventMap().remove(groupId);
                } else {
                    event.ignore();
                    messageChainBuilder.append("群邀请已忽略");
                    InvitedEventRepository.INSTANCE.getGroupInvitedJoinEventMap().remove(groupId);
                }
            }
            sender.sendMessage(messageChainBuilder.build());
            return true;
        }
        return false;
    }

    /**
     * 好友消息处理
     * @param sender 发送者
     * @param id           用户ID
     * @param opinion      处理意见Y/N
     * @param addBlackList 是否拉黑
     * @return
     */
    @SubCommand({"friend","f"})
    public Boolean friend(CommandSender sender, long id,Boolean opinion,Boolean addBlackList){
        if (sender.getUser() != null && GlobalConfig.ADMIN_ID == sender.getUser().getId()) {
            //初始化消息builder
            MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
            //添加at
            messageChainBuilder.append(new At(sender.getUser().getId())).append("\n");
            //获取缓存事件
            NewFriendRequestEvent event = InvitedEventRepository.INSTANCE.getFriendRequestEventMap().get(id);
            if (event == null) {
                messageChainBuilder.append("事件ID无效或已过期");
            }else {
                if (opinion) {
                    event.accept();
                    messageChainBuilder.append("好友添加申请已通过");
                    InvitedEventRepository.INSTANCE.getFriendRequestEventMap().remove(id);
                } else {
                    event.reject(addBlackList);
                    messageChainBuilder.append("好友添加申请已拒绝" + (addBlackList ? "+拉黑" : ""));
                    InvitedEventRepository.INSTANCE.getFriendRequestEventMap().remove(id);
                }
            }
            sender.sendMessage(messageChainBuilder.build());
            return true;
        }
        return false;
    }
}
