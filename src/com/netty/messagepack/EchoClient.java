package com.netty.messagepack;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class EchoClient {
	
	private final String host;
	private final int port;
	private final int sendNum;
	
	
	
	public EchoClient(String host, int port, int sendNum) {
		super();
		this.host = host;
		this.port = port;
		this.sendNum = sendNum;
	}

	public void connect(String host,int port) throws InterruptedException{
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
			 .channel(NioSocketChannel.class)
			 .option(ChannelOption.TCP_NODELAY, true)
			 .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
			 .handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel arg0) throws Exception {
					arg0.pipeline().addLast("msgpack decoder",new MsgPackDecoder());
					arg0.pipeline().addLast("msgpack encoder",new MsgPackEncoder());
					arg0.pipeline().addLast(new EchoClientHandler(sendNum));
				}
			});
			ChannelFuture f = b.connect(host,port).sync();
			f.channel().closeFuture().sync();
		}finally{
			group.shutdownGracefully();
		}
	}
	

}
