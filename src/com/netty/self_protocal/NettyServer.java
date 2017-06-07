package com.netty.self_protocal;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettyServer {
	
	public void bind() throws InterruptedException{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel arg0) throws Exception {
				arg0.pipeline().addLast(new NettyMessageDecoder(1024*1024,4,4));
				arg0.pipeline().addLast(new NettyMessageEncoder());
				arg0.pipeline().addLast("readTimeoutHandler",new ReadTimeoutHandler(50));
				arg0.pipeline().addLast(new LoginAuthRespHandler());
				arg0.pipeline().addLast("HeartBeatHandler",new HeartBeatRespHandler());
				
			}
		});
		b.bind(NettyConstant.REMOTEHOST,NettyConstant.PORT).sync();
		System.out.println("Netty server start ok:"+NettyConstant.REMOTEHOST+":"+NettyConstant.PORT);
	}
	
	public static void main(String[] args) {
		try {
			new NettyServer().bind();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
