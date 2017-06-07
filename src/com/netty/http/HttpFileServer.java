package com.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpFileServer {
	
	private static final String DEFAULT_URL = "/src/com/netty/http";
	public void run(final String url,final int port) throws InterruptedException{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b  = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			 .channel(NioServerSocketChannel.class)
//		 .option(ChannelOption.SO_BACKLOG, 100)
			 .childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel arg0) throws Exception {
					arg0.pipeline().addLast("header-decoder",new HttpRequestDecoder());
					arg0.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
					arg0.pipeline().addLast("http-encoder",new HttpResponseEncoder());
					arg0.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
					arg0.pipeline().addLast("fileServerHandler",new HttpFileServerHandler(url));
				}
			
			});
			ChannelFuture f = b.bind("127.0.0.1", port).sync();
			System.out.println("HTTP 文件目录服务器启动，地址是http://192.168."+port+url);
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		int port = 8080;
		if(args != null && args.length > 0){
			port = Integer.valueOf(args[0]);
		}
		String url = DEFAULT_URL;
		if(args.length > 1){
			url = args[1];
		}
		try {
			new HttpFileServer().run(url,port);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
