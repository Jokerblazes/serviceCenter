package com.joker.server.codec;



import com.joker.server.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class MessageDecoder extends LengthFieldBasedFrameDecoder{
	
	private static final Logger logger = LoggerFactory.getLogger(MessageDecoder.class);

	public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
			int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		logger.info("开始解码！");
		Object msg = super.decode(ctx, in);
		if(null != msg){
			Message message = Message.messageDecode((ByteBuf)msg);
		   if(message!=null){
			   logger.info("解码成功！");
			   return message;
		   }else{
			   logger.info("解码失败并关闭连接！");
			   ctx.channel().close();
		   }
		}
		return null;
	}

	

}
