package com.netty.http_xml.client;

import com.netty.http_xml.OrderFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HttpXmlClientHandler extends SimpleChannelInboundHandler<HttpXmlResponse> {
	
	public void channelActive(ChannelHandlerContext ctx){
		HttpXmlRequest request = new HttpXmlRequest(null,OrderFactory.create(123));
		ctx.writeAndFlush(request);
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
		cause.printStackTrace();
		ctx.close();
	}
	
	
	@Override
	protected void messageReceived(ChannelHandlerContext arg0,
			HttpXmlResponse arg1) throws Exception {
		
	}

}
