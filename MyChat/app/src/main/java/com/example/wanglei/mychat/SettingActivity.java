package com.example.wanglei.mychat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by wanglei on 2017/6/22.
 */

public class SettingActivity extends AppCompatActivity {
    private EditText editTextSentIP;
    private EditText editTextSendPort;
    private EditText editTextAcceptPort;
    private Button button_ack;
    private TextView textViewSetIP;
    private Context context;

    private String sendip, serverip;
    private int sendport, serverport;

    private String str;//服务器端接收的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

//        FileUtils fileUtils_set = new FileUtils();
//        try {
//            if((fileUtils_set.readFromFile("setting.txt", SettingActivity.this)) != null) {
//                String str_setfile = fileUtils_set.readFromFile("setting.txt", SettingActivity.this);
//                String[] temp = str_setfile.split(":");
//                editTextSentIP.setText(temp[0]);
//                editTextSendPort.setText(temp[1]);
//                editTextAcceptPort.setText(temp[3]);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        this.context = this;
        textViewSetIP = (TextView) findViewById(R.id.set_ip);
        serverip = getHostIP();
        textViewSetIP.setText("本机IP地址为:" + serverip);

        editTextSentIP = (EditText) findViewById(R.id.set_sendip);
        editTextSendPort = (EditText) findViewById(R.id.set_sendport);
        editTextAcceptPort = (EditText) findViewById(R.id.set_acceptport);

        button_ack = (Button) findViewById(R.id.button_ack);
        button_ack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendip = editTextSentIP.getText().toString();
                sendport = Integer.parseInt(editTextSendPort.getText().toString());
                serverport = Integer.parseInt(editTextAcceptPort.getText().toString());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Server s = new Server(serverip, serverport);
                        try {
                            s.run(SettingActivity.this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Toast.makeText(context, "服务器端开启", Toast.LENGTH_LONG).show();
                textViewSetIP.setText("本机IP地址为:" + serverip + ",本机端口号为：" + serverport);

                FileUtils fileUtils = new FileUtils();
                try {
                    fileUtils.writeToFile("setting.txt", sendip+":"+sendport+":1" + ":" + serverport,SettingActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取ip地址
     */
    public static String getHostIP() {
        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return hostIp;

    }
}
