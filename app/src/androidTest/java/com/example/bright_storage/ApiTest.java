package com.example.bright_storage;

import com.baidu.aip.nlp.AipNlp;
import com.example.bright_storage.api.Analyzer;
import com.example.bright_storage.repository.StorageUnitRepository;

import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiTest {

    private final String APP_ID = "24504515";
    private final String API_KEY = "bTG7ohvMjG6Md5x8XQ3BxGOd";
    private final String SECRET_KEY = "6SOL17NGM0dP4MA9SDAA45uyHtpUzdW4";

    private TreeMap<String, Integer> dict = new TreeMap<>();
    private TreeMap<String, Word> sentence = new TreeMap<>();
    private StorageUnitRepository storageUnitRepo = new StorageUnitRepository();

    @Test
    public void barcode() {
//        String mAPI_KEY = "a07d8456772095170c102e523d07eea3";
//        OkHttpClient okHttpClient = new OkHttpClient();
//        RequestBody requestBody = new FormBody.Builder() .add("key", mAPI_KEY) .add("barcode", "6923599932108").build();
//        final Request request = new Request.Builder()
//                .url("http://api.tianapi.com/txapi/barcode/index")
//                .post(requestBody)
//                .build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                System.out.println(e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                System.out.println("onResponse: " + response.body().string());
//            }
//        });

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("code", "6923599932108").build();
        final Request request = new Request.Builder()
                .url("http://barcode.api.bdymkt.com/barcode")
                .header("X-Bce-Signature", "AppCode/8fbd053b4ee449fca32dac100801a56a")
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("onResponse: " + response.body().string());
            }
        });
    }
    
    @Test
    public void baidu() throws Exception {

        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);


        // 调用接口
        String text = "把面包放至抽屉里";
//        text = new String(text.getBytes(StandardCharsets.UTF_8), StndardCharsets.UTF_8);
        JSONObject res = client.lexer(text, null);
        System.out.println(res.toString(2));
        System.out.println(Charset.defaultCharset());
    }

    @Test
    public void indexOfTest() {
        String a = "123456";
        String b = "45";
        System.out.println(a.indexOf(b));
    }

    @Test
    public void new_analyze() {
        Analyzer analyzer = new Analyzer();
        String text = "在客厅中添加面包";
        try {
            analyzer.analyze(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Word {
    private String id;
    private String word;
    private String postag;
    private String head;
    private String deprel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPostag() {
        return postag;
    }

    public void setPostag(String postag) {
        this.postag = postag;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDeprel() {
        return deprel;
    }

    public void setDeprel(String deprel) {
        this.deprel = deprel;
    }
}
