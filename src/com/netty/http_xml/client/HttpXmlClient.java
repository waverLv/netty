package com.netty.http_xml.client;

import java.net.InetSocketAddress;

import org.jboss.netty.handler.codec.http.HttpResponseDecoder;

import com.netty.http_xml.pojo.Order;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;

public class HttpXmlClient {
	
	public  void connect(int port) throws InterruptedException{
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,true).handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel arg0) throws Exception {
					arg0.pipeline().addLast("http-decoder",new io.netty.handler.codec.http.HttpResponseDecoder());
					arg0.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
					arg0.pipeline().addLast("xml-decoder", new HttpXmlResponseDecoder(Order.class,true));
					arg0.pipeline().addLast("http-encoder",new HttpRequestEncoder());
					arg0.pipeline().addLast("xml-encoder", new HttpXmlRequestEncoder());
					arg0.pipeline().addLast("xmlClientHandler",new HttpXmlClientHandler());
					
				}
				
			});
			ChannelFuture f = b.connect(new InetSocketAddress(port)).sync();
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
		
	}
	
	public static void main(String[] args) {
		int port = 8080;
		if(args != null && args.length > 0){
			port = Integer.valueOf(port);
		}
		try {
			new HttpXmlClient().connect(port);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
