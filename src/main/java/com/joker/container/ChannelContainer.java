package com.joker.container;

import com.joker.server.entity.Message;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by joker on 2017/12/7.
 */
public class ChannelContainer {
    private final static ConcurrentMap<Integer,Channel> map = new ConcurrentHashMap<Integer, Channel>();

    public static void addChannel(Integer key,Channel channel) {
        map.putIfAbsent(key,channel);
    }

    public static void removeChannel(Integer key) {
        map.remove(key);
    }

    public static void wideSend(Integer key,Message message) {
        Channel channel = map.get(key);
        channel.writeAndFlush(message);
    }
}
