package com.example.bright_storage.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.baidu.speech.asr.SpeechConstant;
import com.example.bright_storage.R;
import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.recog.MyRecognizer;
import com.example.bright_storage.recog.listener.IRecogListener;
import com.example.bright_storage.recog.listener.MessageStatusRecogListener;
import com.example.bright_storage.repository.StorageUnitRepository;
import com.example.bright_storage.service.CategoryService;
import com.example.bright_storage.service.impl.CategoryServiceImpl;
import com.example.bright_storage.ui.home.HomeFragment;
import com.example.bright_storage.util.SharedPreferencesUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    public static DisplayMetrics dm;
    public static int width;

    private static SharedPreferences sharedPreferences;

    public static SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }

    @Inject
    CategoryService categoryService;

    private void initPermission() {
        String[] permissions = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<>();

        for (String perm :permissions){
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.

            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()){
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        categoryService = new CategoryServiceImpl();
        sharedPreferences = getSharedPreferences("BrightStorage", MODE_PRIVATE);
        SharedPreferencesUtil.setSharedPreferences(sharedPreferences);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        SharedPreferences preferences = getSharedPreferences("isFirst",MODE_PRIVATE);
        boolean isFirst = preferences.getBoolean("isFirst", true);
        if (isFirst) {
            //Toast.makeText(MainActivity.this, "hahaha", Toast.LENGTH_SHORT);
            initRoom();
            initCategory();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirst", false).commit();
        }
        setContentView(R.layout.activity_main);
        this.dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.width = dm.widthPixels;
//        this.height = dm.heightPixels;
        View titleView = this.findViewById(R.id.title_bar);
        Button title_search = (Button) titleView.findViewById(R.id.title_search);
        Button title_back = (Button) titleView.findViewById(R.id.title_back);

        initPermission();
        IRecogListener listener = new MessageStatusRecogListener(null);
        MyRecognizer myRecognizer = new MyRecognizer(this, listener);

        Map<String, Object> params = new LinkedHashMap<>();
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        params.put(SpeechConstant.DISABLE_PUNCTUATION, true);

        FloatingActionButton fab = findViewById(R.id.fab);
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

    private void initRoom() {
        StorageUnit storageUnit = new StorageUnit();
        storageUnit.setName("客厅");
        storageUnit.setLocalParentId(0L);
        storageUnit.setType(1);
        storageUnit.setAmount(1);
        storageUnit.setAccess(true);
        StorageUnitRepository storageUnitRepository = new StorageUnitRepository();
        storageUnitRepository.save(storageUnit);
        storageUnit.setName("餐厅");
        storageUnitRepository.save(storageUnit);
        storageUnit.setName("主卧");
        storageUnitRepository.save(storageUnit);
        storageUnit.setName("次卧");
        storageUnitRepository.save(storageUnit);
        storageUnit.setName("卫生间");
        storageUnitRepository.save(storageUnit);
    }

    private void initCategory() {
        addCategory("食品");
        addCategory("书籍");
        addCategory("工具");
        addCategory("衣物");
        addCategory("日用品");
        addCategory("贵重物品");
    }

    private void addCategory(String inputStr) {
        Category c1 = new Category(null, null, inputStr);
        categoryService.create(c1);
    }

}