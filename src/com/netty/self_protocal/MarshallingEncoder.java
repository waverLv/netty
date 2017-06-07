package com.netty.self_protocal;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import org.jboss.marshalling.Marshaller;



public class MarshallingEncoder {
	private final static byte[] LENGTH_PLACEHOLDER = new byte[4];
	Marshaller marshaller;
	public MarshallingEncoder() throws IOException {
		this.marshaller = MarshallingCodecFactory.buildMarshalling();
	}
	
	protected void encode(Object msg,ByteBuf out) throws IOException{
		try {
			int lengthPos = out.writerIndex();
			out.writeBytes(LENGTH_PLACEHOLDER);
			ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);
			marshaller.start(output);
			marshaller.writeObject(msg);
			marshaller.finish();
			out.setInt(lengthPos, out.writerIndex()-lengthPos-4);
		} finally{
			marshaller.close();
		}
	}
	

}
