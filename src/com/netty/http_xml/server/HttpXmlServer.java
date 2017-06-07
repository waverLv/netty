package com.netty.http_xml.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.net.InetSocketAddress;

import com.netty.http_xml.pojo.Order;

public class HttpXmlServer {
	
	public void run(final int port) throws InterruptedException{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel arg0) throws Exception {
					arg0.pipeline().addLast("http-decoder",new HttpRequestDecoder());
					arg0.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
					arg0.pipeline().addLast("xml-decoder", new HttpXmlRequestDecoder(Order.class,true));
					arg0.pipeline().addLast("http-encoder",new HttpResponseEncoder());
					arg0.pipeline().addLast("xml-encoder", new HttpXmlResponseEncoder());
					arg0.pipeline().addLast("xmlClientHandler",new HttpXmlServerHandler());
				}
			});
			ChannelFuture f = b.bind(new InetSocketAddress(port)).sync();
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
		try {
			new HttpXmlServer().run(port);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
