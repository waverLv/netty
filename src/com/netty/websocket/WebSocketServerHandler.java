package com.netty.websocket;

import java.util.Date;

import org.apache.log4j.Logger;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
	
	private final static Logger log = Logger.getLogger(WebSocketServerHandler.class);
	private WebSocketServerHandshaker webSocketServerHandshaker;

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if(msg instanceof FullHttpRequest){
			handleHttpRequest(ctx,(FullHttpRequest)msg);
		}
		if(msg instanceof WebSocketFrame){
			handleWebSocketFrame(ctx,(WebSocketFrame)msg);
		}
		
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	private void handleHttpRequest(ChannelHandlerContext ctx,FullHttpRequest request){
		if(!request.getDecoderResult().isSuccess() || !"websocket".equals(request.headers().get("Upgrade"))){
			sendHttpResponse(ctx,request,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		};
		WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket",null,false);
		webSocketServerHandshaker = factory.newHandshaker(request);
		if(null != webSocketServerHandshaker){
			WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
		}else{
			webSocketServerHandshaker.handshake(ctx.channel(), request);
		}
	}
	
	private void handleWebSocketFrame(ChannelHandlerContext ctx,WebSocketFrame frame){
		if(frame instanceof CloseWebSocketFrame){
			webSocketServerHandshaker.close(ctx.channel(),((CloseWebSocketFrame) frame).retain());
			return;
		}
		if(frame instanceof PingWebSocketFrame){
			ctx.channel().write(new PongWebSocketFrame(((PingWebSocketFrame) frame).content().retain()));
			return;
		}
		if(!(frame instanceof TextWebSocketFrame)){
			throw new UnsupportedOperationException(String.format("%s frams types not supported",frame.getClass().getName()));
			
		}
		String messgage = ((TextWebSocketFrame)frame).text();
		ctx.channel().write(new TextWebSocketFrame(messgage+",欢迎使用Netty WebSocket服务，现在时刻"+new Date().toString()));
		
	}
	
	private static void sendHttpResponse(ChannelHandlerContext ctx,FullHttpRequest request,FullHttpResponse response){
		
		if(response.getStatus().code() != 200){
			ByteBuf buf = Unpooled.copiedBuffer(response.getStatus().toString(),CharsetUtil.UTF_8);
			response.content().writeBytes(buf);
			buf.release();
			HttpHeaders headers = request.headers();
			headers.setContentLength(request,response.content().readableBytes());
		}
		ChannelFuture f = ctx.channel().writeAndFlush(response);
		if(!isKeepAlive(request) || response.getStatus().code() != 200){
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	
	

}
