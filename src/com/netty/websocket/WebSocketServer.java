package com.netty.websocket;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {
	
	public void run(int port) throws InterruptedException{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel arg0) throws Exception {
					ChannelPipeline ch = arg0.pipeline();
					ch.addLast("http-codec", new HttpServerCodec());
					ch.addLast("aggregator",new HttpObjectAggregator(65536));
					ch.addLast("http-chunked", new ChunkedWriteHandler());
					ch.addLast("handler", new WebSocketServerHandler());
				}
			});
			
			Channel channel = b.bind(new InetSocketAddress(port)).sync().channel();
			System.out.println("Web Socket Server start at port "+port+".");
			System.out.println("Open your brower and navigate to http://localhost:"+port+"/");
			channel.closeFuture().sync();
		} finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
		
		
	}
	
	public static void main(String[] args) {
		int port = 8080;
		if(args != null && args.length > 0){
			port = Integer.valueOf(args[0]);
		}
		try {
			new WebSocketServer().run(port);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
