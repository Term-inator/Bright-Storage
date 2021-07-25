package com.example.bright_storage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.baidu.speech.asr.SpeechConstant;
import com.example.bright_storage.R;
import com.example.bright_storage.api.Analyzer;
import com.example.bright_storage.recog.MyRecognizer;
import com.example.bright_storage.recog.listener.IRecogListener;
import com.example.bright_storage.recog.listener.MessageStatusRecogListener;
import com.example.bright_storage.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedHashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    public static DisplayMetrics dm;
    public static int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        this.dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.width = dm.widthPixels;
//        this.height = dm.heightPixels;
        View titleView = this.findViewById(R.id.title_bar);
        Button title_search = (Button) titleView.findViewById(R.id.title_search);
        Button title_back = (Button) titleView.findViewById(R.id.title_back);
//        title_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //finish();
////                Intent intent = new Intent(BSProActivity.this, MainActivity.class);
////                startActivity(intent);
//                HomeFragment.back();
//            }
//        });
//        title_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(titleView.getContext(), SearchActivity.class);
//                startActivity(intent);
//            }
//        });
        IRecogListener listener = new MessageStatusRecogListener(null);
        MyRecognizer myRecognizer = new MyRecognizer(this, listener);

        Map<String, Object> params = new LinkedHashMap<>();
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);

//        findViewById(R.id.btn_start_record).setOnClickListener(v -> myRecognizer.start(params));
//        findViewById(R.id.btn_stop_record).setOnClickListener(v -> myRecognizer.stop());

        Analyzer analyzer = new Analyzer();

        FloatingActionButton fab = findViewById(R.id.fab);
        System.out.println(fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("pid",HomeFragment.getPid());
                intent.setClass(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                myRecognizer.start(params);
                String result = "";
                System.out.println(result);
                try {
                    analyzer.analyze(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

}