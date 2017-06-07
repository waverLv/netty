package com.netty.self_protocal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public final class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {
	MarshallingDecoder marshallingDecoder;
	public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset,
			int lengthFieldLength) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		try {
			marshallingDecoder = new MarshallingDecoder();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in)
			throws Exception {
		ByteBuf  frame = (ByteBuf) super.decode(ctx, in);
		if(frame == null){
			return null;
		}
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setCrcCode(in.readInt());
		header.setLength(in.readInt());
		header.setSessionID(in.readLong());
		header.setType(in.readByte());
		header.setPriority(in.readByte());
		message.setHeader(header);
		int size = in.readInt();
		if(size > 0 ){
			Map<String,Object> attach = new HashMap<String,Object>(size);
			int keySize = 0;
			byte[] keyArray = null;
			String value = null;
			for(int i=0; i<keySize; i++){
				keyArray = new byte[keySize];
				in.readBytes(keyArray);
				value = new String(keyArray,"UTF-8");
				attach.put(value, marshallingDecoder.decode(in));
			}
			keyArray = null;
			value = null;
			header.setAttachment(attach);
		}
		if(in.readableBytes() > 4){
			message.setBody(marshallingDecoder.decode(in));
		}
		message.setHeader(header);
		return message;
	}
	
	

}
