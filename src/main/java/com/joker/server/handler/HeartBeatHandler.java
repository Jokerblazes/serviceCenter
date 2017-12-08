package com.joker.server.handler;

import com.joker.server.entity.Message;
import com.joker.server.entity.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HeartBeatHandler extends SimpleChannelInboundHandler<Object>{
	
	private static final Logger logger = LoggerFactory.getLogger(HeartBeatHandler.class);
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		Message message = (Message) msg;
		System.out.println("heart:"+message);
		byte cmd = message.getCmdType();
		System.out.println("isHeartBeat:"+(cmd == MessageType.HEARTBEAT.value()));
		if (cmd == MessageType.HEARTBEAT.value()) {
			logger.info("心跳请求消息 {}",message);
			buildHeartBeat(message);
			System.out.println("HeartBeatMessage:"+message);
			logger.info("心跳回复消息 {}",message);
			ctx.writeAndFlush(message);
		} else {
			logger.info("非心跳消息！");
			ctx.fireChannelRead(msg);
		}
	}
	
	private void buildHeartBeat(Message message) {
		message.setOpStatus(MessageType.Success.value());
	}
	
}
