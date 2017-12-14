package com.joker.server.handler;


import com.joker.agreement.entity.Message;
import com.joker.agreement.entity.MessageType;
import com.joker.container.ChannelContainer;
import com.joker.container.CustomerContainer;
import com.joker.container.ProviderContainer;
import com.joker.dto.CustomerDTO;
import com.joker.entity.*;
import com.joker.utils.CheckUntils;
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
    public static AttributeKey<Object> ATTACHMENT_KEY = AttributeKey.valueOf("ATTACHMENT_KEY");
    private static final Logger logger = LoggerFactory.getLogger(ServiceHandler.class);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("ServiceHandler新的链接进入 {}", ctx.channel().toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("ServiceHandler链接异常，关闭 {}", ctx.channel().toString());
        exceptionalProcess(ctx);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("ServiceHandler链接断开 {}", ctx.channel().toString());
        exceptionalProcess(ctx);
    }

    //心跳检测
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) {
                logger.info("长时间不读写，断开连接 {}", ctx.channel().toString());
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("服务器收到消息 {}", msg);
        Message reqMessage = (Message) msg;
        byte cmd = reqMessage.getCmdType();
        //判断多个角色的操作
        if (cmd == MessageType.PROVIDER_REGIST.value()) {
            ProviderContainer container = ProviderContainer.getInstance();
            final Provider provider = (Provider) MessagePackageFactory.bytesToEntity(reqMessage.getOptionData(), Provider.class);
            boolean flag = CheckUntils.checkNull(provider);
            Message message = null;
            if (!flag) {
                logger.error("注册信息不全 {}",provider);
                message = Message.messageResult(null,MessageType.Error.value(),"");
            } else {
                ChannelKey key = new ChannelKey(provider.getNode().getId(), provider.getServiceName());
                ctx.channel().attr(ATTACHMENT_KEY).set(key);
                container.registService(provider.getServiceName(), provider.getNode().getId(), provider);
                message = Message.messageResult(null, MessageType.Success.value(), "");
                message.setCmdType(MessageType.Login.value());
            }
            ctx.writeAndFlush(message);
        } else if (cmd == MessageType.CUSTOMER_REGIST.value()) {
            Message message = null;
            final CustomerDTO customerDTO = (CustomerDTO) MessagePackageFactory.bytesToEntity(reqMessage.getOptionData(), CustomerDTO.class);
            final Customer customer = new Customer(customerDTO);
            boolean flag = CheckUntils.checkNull(customer);
            if (!flag) {
                logger.error("注册信息不全 {}",customer);
                throw new Exception("注册信息不全");
            } else {
                ChannelContainer.addChannel(customer.getNode().getId(), ctx.channel());
                ChannelKey key = new ChannelKey(customer.getNode().getId(), "");
                ctx.channel().attr(ATTACHMENT_KEY).set(key);
                List<String> serviceNames = customer.getServiceNames();
                ProviderContainer providerContainer = ProviderContainer.getInstance();
                for (String serviceName :
                        serviceNames) {
                    ProviderSet providerSet = providerContainer.getServiceSet(serviceName);
                    if (providerSet != null)
                        providerSet.addObserver(customer);
                    else {
                        logger.error("{}服务不存在", serviceName);
                        throw new Exception("注册信息不全");
                    }
                }
                CustomerContainer.getInstance().put(customer.getNode().getId(), customer);
                message = Message.messageResult(null, MessageType.Success.value(), "");
                message.setCmdType(MessageType.Login.value());
            }
            ctx.writeAndFlush(message);
        } else if (cmd == MessageType.PROVIDER_CLOSE.value()) {
            ChannelKey key = (ChannelKey) ctx.channel().attr(ATTACHMENT_KEY).get();
            cancleError(key);
            ProviderContainer.getInstance().removeService(key.getServiceName(), key.getId());
        } else if (cmd == MessageType.CUSTOMER_CLOSE.value()) {
            ChannelKey key = (ChannelKey) ctx.channel().attr(ATTACHMENT_KEY).get();
            cancleError(key);
            CustomerContainer.getInstance().remove(key.getId());
        }

    }

    //异常处理
    public void exceptionalProcess(ChannelHandlerContext ctx) {
        ChannelKey key = (ChannelKey) ctx.channel().attr(ATTACHMENT_KEY).get();
        String serviceName = key.getServiceName();
        if ("".equals(serviceName))
            CustomerContainer.getInstance().remove(key.getId());
        else
            ProviderContainer.getInstance().removeService(key.getServiceName(),key.getId());
        ChannelContainer.removeChannel(key.getId());
    }

    private void cancleError(ChannelKey key) throws Exception {
        if (key == null) {
            logger.error("已取消注册");
            throw new Exception("已取消注册");
        }
    }



}
