package com.example.wanglei.mychat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/*
 * 必须在服务器端开启的时候才能扫描成功
 */
public class ScanPorts {
	
	public boolean ScanConnection(String ip, int port) {
		Socket socket = new Socket();
		SocketAddress socketAddress = new InetSocketAddress(ip, port);
		try {
			socket.connect(socketAddress, 50);
			socket.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
