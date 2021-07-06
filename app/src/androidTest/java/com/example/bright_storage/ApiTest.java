package com.example.bright_storage;

import com.baidu.aip.nlp.AipNlp;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ApiTest {

    private final String APP_ID = "24504515";
    private final String API_KEY = "bTG7ohvMjG6Md5x8XQ3BxGOd";
    private final String SECRET_KEY = "6SOL17NGM0dP4MA9SDAA45uyHtpUzdW4";

    @Test
    public void baidu() throws Exception {

        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);


        // 调用接口
        String text = "百度不是一家高科技ASD公司";
//        text = new String(text.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        JSONObject res = client.lexer(text, null);
        System.out.println(res.toString(2));
        System.out.println(Charset.defaultCharset());
    }
}
