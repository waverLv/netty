package com.netty.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends ChannelHandlerAdapter{
	private int counter = 0;
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		String body = (String)msg;
		System.out.println("This is "+ ++counter+" times recive client ["+body+" ]");
		body += "$_";
		ByteBuf buf = Unpooled.copiedBuffer(body.getBytes());
		ctx.writeAndFlush(buf);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	
  
}
