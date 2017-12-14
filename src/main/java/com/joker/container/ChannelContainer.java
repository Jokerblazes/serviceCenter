package com.joker.container;

import com.joker.agreement.entity.Message;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by joker on 2017/12/7.
 * Channel容器类
 * https://github.com/Jokerblazes/serviceCenter.git
 */
public class ChannelContainer {
    private final static Logger logger = LoggerFactory.getLogger(ChannelContainer.class);
    private final static ConcurrentMap<Integer,Channel> map = new ConcurrentHashMap<Integer, Channel>();

    /**
     *
     * @param key
     * @param channel
     */
    public static void addChannel(Integer key,Channel channel) {
        map.putIfAbsent(key,channel);
        logger.info("添加key {} channel {}成功",key,channel);
    }

    /**
     *
     * @param key
     */
    public static void removeChannel(Integer key) {
        map.remove(key);
        logger.info("移除 {}的channel",key);
    }

    /**
     * 发送消息
     * @param key
     * @param message
     * https://github.com/Jokerblazes/serviceCenter.git
     */
    public static void wideSend(Integer key,Message message) {
        Channel channel = map.get(key);
        if (channel == null) {
            logger.error("没有key {}对应的channel",key);
            throw new RuntimeException("没有对应的channel");
        }
        channel.writeAndFlush(message);
        logger.info("通过{}发送消息{}",channel,message);
    }
}
