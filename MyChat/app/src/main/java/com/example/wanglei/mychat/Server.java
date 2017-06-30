package com.example.wanglei.mychat;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private String serverip;//ip地址
	private int serverport;//端口号
	private String myAccStr="";//信息
	
	public Server(String ip, int port) {
		this.serverip = ip;
		this.serverport = port;
	}
	
	public void run(Activity activity) throws IOException{
		//服务端socket对象
		ServerSocket serverSocket = new ServerSocket();
		serverSocket.bind(new InetSocketAddress(serverip, serverport));
		
		while(true) {
			Socket socket = serverSocket.accept();
			//接收客户端发来的数据
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String str = null;
			if( (str = br.readLine()) != null) {
				OutputStream os = activity.openFileOutput("server.txt", Activity.MODE_PRIVATE);
				os.write(str.getBytes());
				os.close();
			}
			socket.close();
		}
	}

}
