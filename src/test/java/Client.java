
import com.joker.server.codec.MessageDecoder;
import com.joker.server.codec.MessageEncoder;
import com.joker.server.entity.MessageConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	    EventLoopGroup group = new NioEventLoopGroup();

	    public void connect(int port, String host) throws Exception {

		// 配置客户端NIO线程组

		try {
		    Bootstrap b = new Bootstrap();
		    b.group(group).channel(NioSocketChannel.class)
			    .option(ChannelOption.TCP_NODELAY, true)
			    .handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch)
					throws Exception {
					ch.pipeline().addLast(
							new MessageDecoder(
									Integer.MAX_VALUE, MessageConstant.lengthFieldOffset,
									MessageConstant.lengthFieldLength,MessageConstant.lengthAdjustment,MessageConstant.initialBytesToStrip));
				    ch.pipeline().addLast(
				    		new MessageEncoder());
				    ch.pipeline().addLast(new LoginHandler());
				    ch.pipeline().addLast(new HeartBeatReqHandler());
				}
			    });
		    // 发起异步连接操作
		    ChannelFuture future = b.connect("127.0.0.1",8888).sync();
		    future.channel().closeFuture().sync();
		} finally {
//		    // 所有资源释放完成之后，清空资源，再次发起重连操作
//		    executor.execute(new Runnable() {
//			public void run() {
//			    try {
//				TimeUnit.SECONDS.sleep(1);
//				try {
//				    connect(8888,"127.0.0.1");// 发起重连操作
//				} catch (Exception e) {
//				    e.printStackTrace();
//				}
//			    } catch (InterruptedException e) {
//				e.printStackTrace();
//			    }
//			}
//		    });
		}
	    }

	    /**
	     * @param args
	     * @throws Exception
	     */
	    public static void main(String[] args) throws Exception {
	    		new Client().connect(8888,"127.0.0.1");
	    }
}
