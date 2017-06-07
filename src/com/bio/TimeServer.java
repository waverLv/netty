package com.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
			while(true){
				socket = serverSocket.accept();
				new Thread(new TimeServerHandler(socket)).start();
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
