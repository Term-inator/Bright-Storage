package com.example.bright_storage.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.speech.asr.SpeechConstant;
import com.example.bright_storage.R;
import com.example.bright_storage.recog.MyRecognizer;
import com.example.bright_storage.recog.listener.IRecogListener;
import com.example.bright_storage.recog.listener.MessageStatusRecogListener;
import com.example.bright_storage.search.SearchActivity;
import com.example.bright_storage.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.MotionEvent.ACTION_BUTTON_PRESS;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;


public class MainActivity extends AppCompatActivity {
    private static boolean longClicked = false;
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
        IRecogListener listener = new MessageStatusRecogListener(null);
        MyRecognizer myRecognizer = new MyRecognizer(this, listener);

        Map<String, Object> params = new LinkedHashMap<>();
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);

        FloatingActionButton fab = findViewById(R.id.fab);
        System.out.println(fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(longClicked == false) {
                    Intent intent = new Intent();
                    intent.putExtra("pid",HomeFragment.getPid());
                    intent.setClass(MainActivity.this, AddActivity.class);
                    startActivity(intent);
                } else {

                    myRecognizer.stop();
                    longClicked = false;
                }
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myRecognizer.start(params);
                longClicked = true;
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