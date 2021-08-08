package com.example.bright_storage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.example.bright_storage.R;
import com.example.bright_storage.model.param.RegisterParam;
import com.example.bright_storage.recog.MyRecognizer;
import com.example.bright_storage.recog.listener.IRecogListener;
import com.example.bright_storage.recog.listener.MessageStatusRecogListener;
import com.example.bright_storage.search.SearchActivity;
import com.example.bright_storage.service.UserService;
import com.example.bright_storage.service.impl.UserServiceImpl;
import com.example.bright_storage.util.AudioRecordUtil;
import com.example.bright_storage.util.RecogUtil;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import static android.view.View.INVISIBLE;

public class BSProActivity extends AppCompatActivity
{

    private static final String TAG = "BSProActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_bspro);
        TextView theTitle = findViewById(R.id.title_text);
        theTitle.setText(R.string.title_BSPro);
        Button title_back = (Button) findViewById(R.id.title_back);
        Button title_search = (Button) findViewById(R.id.title_search);
        title_search.setVisibility(INVISIBLE);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent = new Intent(BSProActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });

        UserService userService = new UserServiceImpl();
        findViewById(R.id.btn_start_record).setOnClickListener(v -> userService.register(new RegisterParam()));
    }
}
