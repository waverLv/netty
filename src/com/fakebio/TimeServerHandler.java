package com.fakebio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class TimeServerHandler implements Runnable {
	private Socket socket;
	
	public TimeServerHandler(Socket socket) {
		this.socket = socket;
	}



	@Override
	public void run() {
//		System.out.println("---------------++++++++++++++++++++++++-----------------");
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			out =  new PrintWriter(this.socket.getOutputStream(), true);
			String currentTime = null;
			String body = null;
			while(true){
				body = in.readLine();
				System.out.println("--------------------------------");
				if(body == null){
					break;
				}
				System.out.println("The time server recive order : "+body);
				currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ERROR";
				out.println(currentTime);
			}
		} catch (IOException e) {
			e.printStackTrace();
			if(out != null){
				out.close();
				out= null;
			}
			if(in != null){
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				in=null;
			}
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				socket = null;
			}
		}
	}

}
