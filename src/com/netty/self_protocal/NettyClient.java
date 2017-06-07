package com.netty.self_protocal;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NettyClient {
	
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	EventLoopGroup group = new NioEventLoopGroup();
	
	public void connect(String host,int port) throws InterruptedException{
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel arg0) throws Exception {
					arg0.pipeline().addLast(new NettyMessageDecoder(1024*1024,4,4));
					arg0.pipeline().addLast("MessageEncoder", new NettyMessageEncoder());
					arg0.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
					arg0.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
				}
			});
			ChannelFuture f = b.connect(
					new InetSocketAddress(host, port),
					new InetSocketAddress(NettyConstant.LOCALHOST,
							NettyConstant.LOCAL_PORT)).sync();
			f.channel().closeFuture().sync();
		} finally{
			executor.execute(new Runnable(){
				@Override
				public void run() {
					try {
						TimeUnit.SECONDS.sleep(1);
						connect(NettyConstant.REMOTEHOST,NettyConstant.PORT);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	
		
		public static void main(String[] args) {
			try {
				new NettyClient().connect(NettyConstant.REMOTEHOST, NettyConstant.PORT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

}
