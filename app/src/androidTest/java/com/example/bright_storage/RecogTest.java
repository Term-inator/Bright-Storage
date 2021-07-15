package com.example.bright_storage;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.speech.core.ASREngine;
import com.example.bright_storage.util.AudioRecordUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class RecogTest {

    public static final String APP_ID = "你的 App ID";
    public static final String API_KEY = "你的 Api Key";
    public static final String SECRET_KEY = "你的 Secret Key";

    private AudioRecordUtil audioRecordUtil = AudioRecordUtil.getInstance();


    @Test
    public void recongTest() throws Exception {


        audioRecordUtil.startRecord();
        Thread.sleep(3000);
        byte[] result = audioRecordUtil.stopRecord();

        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
        JSONObject res = client.asr(result, "pcm", 16000, null);
        System.out.println(res.toString(2));

    }
}
