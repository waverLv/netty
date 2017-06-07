package com.netty.http_xml.server;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class HttpXmlResponseEncoder extends AbstractHttpXmlEncoder<HttpXmlResponse>{

	protected void encode(ChannelHandlerContext ctx, HttpXmlResponse msg,
			List<Object> out) throws Exception {
		ByteBuf body = encode0(ctx, msg.getResult());
		
	}
	
	

}
