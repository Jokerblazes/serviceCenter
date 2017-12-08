
import com.joker.server.entity.Head;
import com.joker.server.entity.Message;
import com.joker.server.entity.MessageConstant;
import com.joker.server.entity.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RegistHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush("");
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		ctx.fireChannelRead(msg);
	}

}
