package com.fakebio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.bio.TimeServerHandler;

public class TimeServer {
	public static void main(String[] args) {
		int  port = 8080;
		if(args != null && args.length > 0){
			port = Integer.valueOf(args[0]);
		}
		ServerSocket  serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("The time server is start in port : "+port);
			Socket socket = null;
			TimeServerHandleExecutePool pool = new TimeServerHandleExecutePool(50,10000);
			while(true){
				socket = serverSocket.accept();
				pool.execute(new TimeServerHandler(socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(serverSocket != null){
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("The time server is close");
				serverSocket = null;
			}
		}
	}
}
