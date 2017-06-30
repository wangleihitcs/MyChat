package com.example.wanglei.mychat;

import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by wanglei on 2017/6/23.
 */

public class FileUtils {

    /**
     * 从文件中读取数据
     * @param filename
     * @return
     * @throws IOException
     */
    public String readFromFile(String filename, Activity activity) throws IOException {
        String str;
        InputStream is = activity.openFileInput(filename);
        byte[] buffer = new byte[1000];
        int byteCounts = is.read(buffer);
        str = new String(buffer, 0, byteCounts, "UTF-8");
        is.close();
        return str;
    }

    /**
     * 向文件中写入数据
     * @param filename
     * @param data
     * @throws IOException
     */
    public void writeToFile(String filename, String data, Activity activity) throws IOException {
        OutputStream os = activity.openFileOutput(filename, Activity.MODE_PRIVATE);
        os.write(data.getBytes("UTF-8"));
        os.close();
    }

    /**
     *  清空文件内容
     */
    public void flushFile(String filename, Activity activity) throws IOException {
        writeToFile(filename, "##", activity);
    }
}
