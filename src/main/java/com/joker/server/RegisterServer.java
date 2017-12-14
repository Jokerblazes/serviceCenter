package com.joker.server;


import com.joker.agreement.codec.MessageDecoder;
import com.joker.agreement.codec.MessageEncoder;
import com.joker.agreement.entity.MessageConstant;
import com.joker.agreement.handler.HeartBeatHandler;
import com.joker.server.handler.ServiceHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class RegisterServer {
	public static void main(String[] args) {

		ServerBootstrap bootstrap = new ServerBootstrap();
		//bossGroup监听端口线程组 workGroup工作线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			bootstrap.group(bossGroup,workGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 2048)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(
							new MessageDecoder(
									Integer.MAX_VALUE, MessageConstant.lengthFieldOffset,
	 								MessageConstant.lengthFieldLength,MessageConstant.lengthAdjustment,MessageConstant.initialBytesToStrip));
					ch.pipeline().addLast(new MessageEncoder());
					ch.pipeline().addLast(new IdleStateHandler(2500, 2500, 5000));
					ch.pipeline().addLast(new HeartBeatHandler());
					ch.pipeline().addLast(new ServiceHandler());

				}
			});
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);//维持链接的活跃，清除死链接
			bootstrap.childOption(ChannelOption.TCP_NODELAY, true);//关闭延迟发送
			ChannelFuture future = bootstrap.bind(8001).sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally{
			//释放资源
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
