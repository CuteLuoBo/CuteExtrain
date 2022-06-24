package com.github.cuteluobo.repository;

import net.mamoe.mirai.data.RequestEventData;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * 邀请事件储存器
 * TODO 先实现程序内缓存，后续通过持久保存再重启读取
 * @author CuteLuoBo
 * @date 2022/6/24 17:36
 */
public class InvitedEventRepository {

    public static final InvitedEventRepository INSTANCE = new InvitedEventRepository();
    /**
     * 缓存Map
     */
    private Map<Long, BotInvitedJoinGroupRequestEvent> groupInvitedJoinEventMap = new HashMap<>();
    /**
     * 缓存Map
     */
    private Map<Long, NewFriendRequestEvent> friendRequestEventMap = new HashMap<>();
    private InvitedEventRepository() {}

    public Map<Long, BotInvitedJoinGroupRequestEvent> getGroupInvitedJoinEventMap() {
        return groupInvitedJoinEventMap;
    }

    public void setGroupInvitedJoinEventMap(Map<Long, BotInvitedJoinGroupRequestEvent> groupInvitedJoinEventMap) {
        this.groupInvitedJoinEventMap = groupInvitedJoinEventMap;
    }

    public Map<Long, NewFriendRequestEvent> getFriendRequestEventMap() {
        return friendRequestEventMap;
    }

    public void setFriendRequestEventMap(Map<Long, NewFriendRequestEvent> friendRequestEventMap) {
        this.friendRequestEventMap = friendRequestEventMap;
    }
}
