package com.example.wanglei.mychat;

import java.io.IOException;
import java.net.Socket;

public class Client {
	private String sendip;//要发送到的服务器ip
	private int sendport;//端口号
	
	public Client(String ip, int port) {
		this.sendip = ip;
		this.sendport = port;
	}
	
	public void send(String data) throws IOException {
		//创建socket对象，指定ip、port
		Socket socket = new Socket(sendip, sendport);
		//将数据传到输出管道字节流发送到服务器端
		socket.getOutputStream().write(data.getBytes());
		//清空IO管道重的缓存数据
		socket.getOutputStream().flush();
		socket.close();
	}
	
	public static void main(String[] args) {
		Client c = new Client("127.0.0.1", 1234);
		try {
			c.send("123456789,nimabinijiehunlema");
			System.out.println("发送成功");
		} catch (IOException e) {
			System.out.println("IO异常");
			e.printStackTrace();
		}
	}
}
