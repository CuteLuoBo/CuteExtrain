package com.github.cuteluobo.repository;

import java.util.*;

/**
 * 消息缓存
 * 一般用于获取引用数据中的图片
 *
 * @author CuteLuoBo
 * @date 2022/10/14 22:20
 */
public class MessageTemp {

    /**
     * 群消息缓存限制
     */
    public static final int GROUP_MESSAGE_TEMP_LIMIT = 500;

    /**
     * 好友消息缓存限制
     */
    public static final int FRIEND_MESSAGE_TEMP_LIMIT = 200;
    /**
     * 群消息ID与ID中附带的图片的对应关系
     */
    private Map<Integer, List<String>> groupMessageImageIdMap = new LinkedHashMap<>(GROUP_MESSAGE_TEMP_LIMIT);

    /**
     * 私聊消息ID与ID中附带的图片的对应关系
     * 目前缓存200
     */
    private Map<Integer, List<String>> friendMessageImageIdMap = new LinkedHashMap<>(FRIEND_MESSAGE_TEMP_LIMIT);
    private static volatile MessageTemp instance;



    public static MessageTemp getInstance() {
        if (instance == null) {
            synchronized (MessageTemp.class) {
                instance = new MessageTemp();
            }
        }
        return instance;
    }

    private MessageTemp() {

    }

    public Map<Integer, List<String>> getGroupMessageImageIdMap() {
        return groupMessageImageIdMap;
    }

    public void setGroupMessageImageIdMap(Map<Integer, List<String>> groupMessageImageIdMap) {
        this.groupMessageImageIdMap = groupMessageImageIdMap;
    }

    public Map<Integer, List<String>> getFriendMessageImageIdMap() {
        return friendMessageImageIdMap;
    }

    public void setFriendMessageImageIdMap(Map<Integer, List<String>> friendMessageImageIdMap) {
        this.friendMessageImageIdMap = friendMessageImageIdMap;
    }

    public void putGroupMessageImageTemp(int messageId, List<String> messageTemp) {
        removeOverflow(groupMessageImageIdMap, GROUP_MESSAGE_TEMP_LIMIT);
        groupMessageImageIdMap.put(messageId, messageTemp);
    }

    public void putFriendMessageImageTemp(int messageId, List<String> messageTemp) {
        removeOverflow(friendMessageImageIdMap, FRIEND_MESSAGE_TEMP_LIMIT);
        friendMessageImageIdMap.put(messageId, messageTemp);
    }

    /**
     * 超限时，对map中旧的额外数据进行移除
     * @param map 需要处理的map，最好为LinkHashMap可以记录数据先后
     * @param max 数组上限
     */
    public void removeOverflow(Map<?, ?> map, int max) {
        int size = map.size();
        if (size >= max) {
            Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator();
            for (int i = 0; i <= size - max; i++) {
                map.remove(iterator.next().getKey());
            }
        }
    }
}
