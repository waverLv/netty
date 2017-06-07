package com.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeServerHandler implements Runnable {
	private Integer  port;
	public CountDownLatch countDownLatch;
	public AsynchronousServerSocketChannel asynchronousServerSocketChannel;
	

	public AsyncTimeServerHandler(Integer port) {
		this.port = port;
		try {
			asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
			asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
			System.out.println("Time server is start in port : "+ port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}



	@Override
	public void run() {
		countDownLatch = new CountDownLatch(1);
		doAccept();
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void doAccept(){
		asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler());
	}

}
