package com.example.wanglei.mychat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button button_set;//设置按钮
    private Button button_his;//历史按钮
    private TextView textViewStrSend;//显示发送的信息
    private EditText editTextStrInput;//发送的信息
    private Button button_send;//发送按钮

    private Context context;
    private String sendip, sendport, conFlag;

    private String myStr="";//存储已发送的信息
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    FileUtils fileUtils = new FileUtils();
                    try {
                        Thread.currentThread().sleep(1000);//毫秒
                        if((fileUtils.readFromFile("server.txt", MainActivity.this) != null) && !((fileUtils.readFromFile("server.txt", MainActivity.this)).equals("##"))) {
                            String temp_str = fileUtils.readFromFile("server.txt", MainActivity.this);
                            myStr += "他:" + temp_str + "<br>" + "<br>";
                            fileUtils.flushFile("server.txt", MainActivity.this);
                            setText("<font color='#FFA500'>", myStr);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //设置按钮
        button_set = (Button) findViewById(R.id.button_set);
        button_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(i);
            }
        });

        //历史按钮
        button_his = (Button) findViewById(R.id.button_his);
        button_his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setText("<font color='#FFA500'>", myStr);
                Toast.makeText(context, "显示成功", Toast.LENGTH_LONG).show();
            }
        });


        //输入要发送的信息控件区域
        textViewStrSend = (TextView) findViewById(R.id.str_send);

        editTextStrInput = (EditText) findViewById(R.id.str_input);

        //发送按钮
        button_send = (Button) findViewById(R.id.button_send);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtils fileUtils_set = new FileUtils();
                String str_setfile="0:0:0";
                try {
                    str_setfile = fileUtils_set.readFromFile("setting.txt", MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String[] temp = str_setfile.split(":");
                sendip = temp[0];
                sendport = temp[1];
                conFlag = temp[2];

                final String mstr = editTextStrInput.getText().toString();
                if(conFlag.equals("1")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Client c = new Client(sendip, Integer.parseInt(sendport));
//                            Client c = new Client("192.168.20.227", 12345);
                            try {
                                c.send(mstr);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    try {
                        Thread.currentThread().sleep(100);//毫秒
                    }
                    catch( Exception e){
                        e.printStackTrace();
                    }

                    //显示已发送的信息
                    myStr += "我:" + mstr + "<br>" + "<br>";
                    setText("<font color='#FFA500'>", myStr);

                    Toast.makeText(context, "发送成功", Toast.LENGTH_LONG).show();


                } else {
                    Toast.makeText(context, "目标IP:Port未连接成功", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * 这个函数用于设置EditText的显示内容,主要是为了加上html标签.
     * 所有的显示EditText内容都需要调用此函数
     */
    private void setText(String color, String data) {
        StringBuilder builder = new StringBuilder();
        // 添加颜色标签
        builder.append(color).append(data).append("</font>");
        // 显示内容
        textViewStrSend.setText(Html.fromHtml(builder.toString()));
    }

}
