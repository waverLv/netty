package com.netty.messagepack;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoClientHandler extends ChannelHandlerAdapter {
		private final int sendNum;
	
	

	public EchoClientHandler(int sendNum) {
		this.sendNum = sendNum;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		UserInfo[] userInfos = getUserInfo();
		for(UserInfo userInfo : userInfos){
			ctx.write(userInfo);
		}
		ctx.flush();
	}
	
	private UserInfo[] getUserInfo(){
		UserInfo[] userInfos = new UserInfo[sendNum];
		UserInfo userInfo = null;
		for(int i=0; i<sendNum; i++){
			userInfo = new UserInfo();
			userInfo.setAge(i);
			userInfo.setName("ABCDEFG ------>"+i);
			userInfos[i] = userInfo;
		}
		return userInfos;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		System.out.println("Client receive the msgpack message->"+msg);
		ctx.write(msg);
		
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	
	
}
