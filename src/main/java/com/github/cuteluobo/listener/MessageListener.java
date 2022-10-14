package com.github.cuteluobo.listener;

import com.github.cuteluobo.repository.MessageTemp;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageSource;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CuteLuoBo
 * @date 2022/10/14 22:26
 */
public class MessageListener extends SimpleListenerHost {

    public MessageListener() {
        super();
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
