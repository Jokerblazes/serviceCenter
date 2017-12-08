package com.joker.server.codec;


import com.joker.server.entity.Head;
import com.joker.server.entity.Message;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class MessageEncoder extends MessageToByteEncoder<Message> {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageEncoder.class);
	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		logger.info("开始编码，编码内容 {}",msg);
		Head head = msg.getHead();
		out.writeByte(head.getHead());
		out.writeShort(msg.getLength());
		out.writeByte(head.getLength());
		if (head.getUrl() != null)
			out.writeBytes(head.getUrl());
		out.writeByte(msg.getChannel());
		out.writeByte(msg.getDirection());
		out.writeInt(msg.getSource());
		out.writeInt(msg.getDest());
		out.writeByte(msg.getCmdType());
		out.writeByte(msg.getOpStatus());	
		if (msg.getOptionData() != null)
			out.writeBytes(msg.getOptionData());
		out.writeShort(msg.getCheckSum());
		logger.info("编码完成！");
	
	}

}
