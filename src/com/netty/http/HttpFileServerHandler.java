package com.netty.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{
	private static final String BAD_REQUEST = "错误的请求";
	private static final String GET = "get";
	private static final String REQUEST_METHOD_NOT_ALLOWED = "请求方式错误";
	private static final String FORBIDEN = "请求拒绝";
	private static final String NOT_FOUND = "文件没有找到";
	private String url;
	
	 public HttpFileServerHandler(String url) {
		this.url = url;
	}
	@Override
	protected void messageReceived(ChannelHandlerContext ctx,
			FullHttpRequest request) throws Exception {
		/*if(!request.getDecoderResult().isSuccess()){
				sendError(ctx,BAD_REQUEST);
				return;
			}
			if(request.getMethod() != GET){
				sendError(ctx,REQUEST_METHOD_NOT_ALLOWED);
				return;
			}
			final String uri = request.getUri();
			final String path = sanitizeUri(uri);
			if(path == null){
				sendError(ctx,FORBIDEN);
				return;
			}
			File file = new File(path);
			if(file.isHidden() || !file.exists()){
				sendError(ctx,NOT_FOUND);
				return;
			}
			
			if(!file.isFile()){
				sendError(ctx,FORBIDEN);
				return;
			}
			RandomAccessFile randomAccessFile = null;
			randomAccessFile = new RandomAccessFile(file,"r"); //以只读方式访问文件
			
			long fileLength = randomAccessFile.length();
			HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
			setContentLength(response,fileLength);
			setContentTypeHeader(response,file);*/
	}
	
	

}
