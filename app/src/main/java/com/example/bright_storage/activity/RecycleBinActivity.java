package com.example.bright_storage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.speech.asr.SpeechConstant;
import com.example.bright_storage.R;
import com.example.bright_storage.recog.MyRecognizer;
import com.example.bright_storage.recog.listener.IRecogListener;
import com.example.bright_storage.recog.listener.MessageStatusRecogListener;
import com.example.bright_storage.ui.home.HomeAdapter;
import com.example.bright_storage.ui.recyclebin.RecycleBinAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

import static android.view.View.INVISIBLE;

public class RecycleBinActivity extends AppCompatActivity {

    public static DisplayMetrics dm;
    public static int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_recycle_bin);
        this.dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.width = dm.widthPixels;
        TextView thetitle = findViewById(R.id.title_text);
        thetitle.setText(R.string.title_RecycleBin);
        Button title_back = (Button) findViewById(R.id.title_back);
        Button title_search = (Button) findViewById(R.id.title_search);
        title_search.setVisibility(INVISIBLE);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent = new Intent(RecycleBinActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });
//        IRecogListener listener = new MessageStatusRecogListener(null);
//        MyRecognizer myRecognizer = new MyRecognizer(this, listener);
//
//        Map<String, Object> params = new LinkedHashMap<>();
//        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
//        params.put(SpeechConstant.DISABLE_PUNCTUATION, true);
    }
}
