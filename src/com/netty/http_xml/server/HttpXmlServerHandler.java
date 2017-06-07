package com.netty.http_xml.server;

import java.util.ArrayList;
import java.util.List;

import com.netty.http_xml.pojo.Address;
import com.netty.http_xml.pojo.Order;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class HttpXmlServerHandler extends SimpleChannelInboundHandler<HttpXmlRequest> {
	private final String CONTENT_TYPE = "Content-Type";
	
	public void messageReceived(final ChannelHandlerContext ctx,HttpXmlRequest xmlRequest){
		HttpRequest request = xmlRequest.getRequest();
		Order order = (Order)xmlRequest.getBody();
		System.out.println("Http Server Recevie Request:"+order);
		doBusiness(order);
		ChannelFuture future = ctx.writeAndFlush(new HttpXmlResponse(null,order));
		if(!isKeepAlive(request)){
			future.addListener(new GenericFutureListener<Future<? super Void>>() {

				@Override
				public void operationComplete(Future future)
						throws Exception {
					ctx.close();
				}
				
			});
		}
	}
	
	private void doBusiness(Order order){
		order.getCustomer().setFirstName("��");
		order.getCustomer().setLastName("�ν�");
		List<String> middleList = new ArrayList<String>();
		middleList.add("��Ԫ��");
		order.getCustomer().setMiddleNames(middleList);
		Address address = order.getBillTo();
		address.setCity("����");
		address.setCountry("�Ƴ�");
		address.setPostCode("471900");
		address.setState("�Ӽ��");
		address.setStreet1("����·");
		address.setStreet2("���Ǵ��");
		order.setBillTo(address);
		order.setShipTo(address);
		
	}
	
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
		cause.printStackTrace();
		if(ctx.channel().isActive()){
			sendError(ctx,HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private void sendError(ChannelHandlerContext ctx,HttpResponseStatus status){
		FullHttpResponse response = new DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer("ʧ��"
						+ status.toString() + "\r\n", CharsetUtil.UTF_8));
		response.headers().set(CONTENT_TYPE,"text/plain;charset=utf-8");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		
	}

}
