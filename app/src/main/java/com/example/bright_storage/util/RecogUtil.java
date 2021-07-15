package com.example.bright_storage.util;

import com.baidu.aip.speech.AipSpeech;

import org.json.JSONObject;

import lombok.SneakyThrows;

public class RecogUtil {

    public static final String APP_ID = "24527486";
    public static final String API_KEY = "uOB9Z59NFMoUtGHWDvj3qcn7";
    public static final String SECRET_KEY = "f6zBiQBeaSYGYcaQctFqbyfxDE63wlRo";

    private static AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

    @SneakyThrows
    public static void recogPcm(byte[] buff){

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 调用接口
        JSONObject res = client.asr(buff, "pcm", 16000, null);
        System.out.println(res.toString(2));
    }
}
