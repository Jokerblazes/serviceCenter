package com.joker.server.handler;



import com.joker.container.ChannelContainer;
import com.joker.container.CustomerContainer;
import com.joker.container.ProviderContainer;
import com.joker.entity.*;
import com.joker.server.entity.Message;
import com.joker.server.entity.MessageType;
import com.joker.utils.MessagePackageFactory;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.List;


public class ServiceHandler extends SimpleChannelInboundHandler<Object> {
	public static AttributeKey<Object> ATTACHMENT_KEY  = AttributeKey.valueOf("ATTACHMENT_KEY");
	private static final Logger logger = LoggerFactory.getLogger(ServiceHandler.class);

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.info("ServiceHandler新的链接进入 {}",ctx.channel().toString());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.info("ServiceHandler链接异常，关闭 {}",ctx.channel().toString());
		exceptionalProcess(ctx);
		
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("ServiceHandler链接断开 {}",ctx.channel().toString());
		exceptionalProcess(ctx);
	}

	//心跳检测
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if(evt instanceof IdleStateEvent){
			IdleStateEvent event = (IdleStateEvent)evt;
			if(event.state() == IdleState.ALL_IDLE){
				logger.info("长时间不读写，断开连接 {}",ctx.channel().toString());
				ctx.channel().close();
			}
		}else{
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("服务器收到消息 {}",msg);
		Message reqMessage = (Message)msg;
		byte cmd = reqMessage.getCmdType();
		//如果软件登录
		if(cmd == MessageType.Login.value()) {
			Message message = reqMessage;
			message.setOpStatus(MessageType.Success.value());
			ctx.writeAndFlush(message);
		} else if (cmd == MessageType.PROVIDER_REGIST.value()){
			ProviderContainer container = ProviderContainer.getInstance();
			final Provider provider = (Provider) MessagePackageFactory.bytesToEntity(reqMessage.getOptionData(),Provider.class);
			ChannelKey key = new ChannelKey(provider.getNode().getId(),provider.getServiceName());
			ctx.channel().attr(ATTACHMENT_KEY).set(key);
			container.registService(provider.getServiceName(),provider.getNode().getId(),provider);
		} else  if (cmd == MessageType.CUSTOMER_REGIST.value()) {
			final Customer customer = (Customer) MessagePackageFactory.bytesToEntity(reqMessage.getOptionData(),Customer.class);
			ChannelKey key = new ChannelKey(customer.getNode().getId(),"");
			ctx.channel().attr(ATTACHMENT_KEY).set(key);
			List<String> serviceNames = customer.getServiceNames();
			ProviderContainer providerContainer = ProviderContainer.getInstance();
			for (String serviceName:
				 serviceNames) {
				ProviderSet providerSet = providerContainer.getServiceSet(serviceName);
				if (providerSet != null)
					providerSet.addObserver(customer);
			}
			CustomerContainer.getInstance().put(customer.getNode().getId(),customer);
		} else if (cmd == MessageType.PROVIDER_CLOSE.value()) {
			ChannelKey key = (ChannelKey) ctx.channel().attr(ATTACHMENT_KEY).get();
			ProviderContainer.getInstance().removeService(key.getServiceName(),key.getId());
		} else if (cmd == MessageType.CUSTOMER_CLOSE.value()) {
			ChannelKey key = (ChannelKey) ctx.channel().attr(ATTACHMENT_KEY).get();
			CustomerContainer.getInstance().remove(key.getId());
		}
	}
	//异常处理
	public void exceptionalProcess(ChannelHandlerContext ctx) {
		ChannelKey key = (ChannelKey) ctx.channel().attr(ATTACHMENT_KEY).get();
		ChannelContainer.removeChannel(key.getId());
	}

}
