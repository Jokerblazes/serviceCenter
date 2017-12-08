
import com.joker.server.entity.Head;
import com.joker.server.entity.Message;
import com.joker.server.entity.MessageConstant;
import com.joker.server.entity.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(buildLogin());
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		ctx.fireChannelRead(msg);
	}

    private Message buildLogin() {
		String url = "order/login";
		byte[] urlBytes = url.getBytes();
		Message message = new Message();
		Head head = new Head();
		head.setHead(MessageConstant.Header);
		head.setLength(urlBytes.length);
		head.setUrl(urlBytes);
		message.setHead(head);;
		message.setCmdType(MessageType.Login.value());
		message.setLength(18+head.getLength());
		return message;
	}
}
