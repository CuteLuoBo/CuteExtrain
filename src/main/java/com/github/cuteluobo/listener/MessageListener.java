package com.github.cuteluobo.listener;

import com.github.cuteluobo.CuteExtra;
import com.github.cuteluobo.command.AiDrawCommand;
import com.github.cuteluobo.repository.MessageTemp;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.command.*;
import net.mamoe.mirai.console.plugin.PluginManager;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author CuteLuoBo
 * @date 2022/10/14 22:26
 */
public class MessageListener extends SimpleListenerHost {
    Logger logger = LoggerFactory.getLogger(MessageListener.class);

    public MessageListener() {
        super();
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        super.handleException(context, exception);
        logger.error("消息事件监听处理出现错误", exception);
    }

    /**
     * 消息图片缓存
     * @param event 消息时间
     */
    @EventHandler
    public void messageImageTemp(@NotNull MessageEvent event){
        MessageChain messageChain = event.getMessage();
        //为群聊或好友私聊消息时，储存
        if (event instanceof GroupMessageEvent || event instanceof FriendMessageEvent) {
            //过滤图片数据
            List<String> imageIdList = messageChain.stream().filter(m -> m instanceof Image).map(m -> (Image) m).map(Image::getImageId).collect(Collectors.toList());
            //有图片时，转换并保存
            if (!imageIdList.isEmpty()) {
                MessageTemp messageTemp = MessageTemp.getInstance();
                MessageSource messageSource = messageChain.get(MessageSource.Key);
                if (messageSource != null) {
                    int[] ids = messageSource.getIds();
                    if (ids.length > 0) {
                        //未知分片效果，所以默认取index0
                        if (event instanceof GroupMessageEvent) {
                            messageTemp.putGroupMessageImageTemp(ids[0],imageIdList);
                        } else {
                            messageTemp.putFriendMessageImageTemp(ids[0],imageIdList);
                        }
                    }
                }
            }
            //对于绘图指令的额外拦截执行
            boolean hasExtraCommand = false;
            SingleMessage prefixMessage = null;
            SingleMessage commandMessage = null;
            for (SingleMessage sm :
                    messageChain) {
                //识别到当前指令
                if (sm.contentToString().contains("/ad")) {
                    //且前置指令为At或者图片，进行额外执行
                    if ((prefixMessage instanceof At || prefixMessage instanceof Image)) {
                        hasExtraCommand = true;
                        commandMessage = sm;
                        break;
                    }
                }
                prefixMessage = sm;
            }
            if (hasExtraCommand) {
                MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
                //在原指令位置中移除，替换成新的执行顺序
//                messageChain.remove(commandMessage);
                messageChainBuilder.append(commandMessage).append(" ");
                messageChainBuilder.addAll(messageChain.stream().filter(m -> !m.contentToString().contains("/ad")).collect(Collectors.toList()));
                MessageChain appendCommandMessage = messageChainBuilder.build();
                if (event instanceof GroupMessageEvent) {
                    GroupMessageEvent groupMessageEvent = (GroupMessageEvent) event;
                    CommandManager.INSTANCE.executeCommand(new MemberCommandSenderOnMessage(groupMessageEvent), appendCommandMessage, true);
                } else {
                    FriendMessageEvent friendMessageEvent = (FriendMessageEvent) event;
                    CommandManager.INSTANCE.executeCommand(new FriendCommandSenderOnMessage(friendMessageEvent), appendCommandMessage, true);
                }
            }
        }
    }

    /**
     * 机器人自我发送消息图片缓存
     * @param event 消息事件
     */
    @EventHandler
    public void botMessageImageTemp(@NotNull MessagePostSendEvent<?> event) {
        //消息发送成功时，才进行缓存
        if (event.getException() == null && event.getReceipt() != null) {
            MessageChain messageChain = event.getMessage();
            //为群聊或好友私聊消息时，储存
            if (event instanceof GroupMessagePostSendEvent || event instanceof FriendMessagePostSendEvent) {
                //过滤图片数据
                List<String> imageIdList = messageChain.stream().filter(m -> m instanceof Image).map(m -> (Image) m).map(Image::getImageId).collect(Collectors.toList());
                //有图片时，转换并保存
                if (!imageIdList.isEmpty()) {
                    MessageTemp messageTemp = MessageTemp.getInstance();
                    MessageReceipt<?> receipt = event.getReceipt();
                    MessageSource messageSource = receipt.getSource();
                    if (messageSource != null) {
                        int[] ids = messageSource.getIds();
                        if (ids.length > 0) {
                            //未知分片效果，所以默认取index0
                            if (event instanceof GroupMessagePostSendEvent) {
                                messageTemp.putGroupMessageImageTemp(ids[0], imageIdList);
                            } else {
                                messageTemp.putFriendMessageImageTemp(ids[0], imageIdList);
                            }

                        }
                    }

                }
            }
        }
    }
}
