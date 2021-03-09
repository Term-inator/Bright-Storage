package com.example.bright_storage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bright_storage.R;
import com.example.bright_storage.search.SearchActivity;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;

public class TypeActivity extends AppCompatActivity
{
    private ArrayList<String> types;
    String[] countryArray = {"China", "India", "Pakistan", "USA", "UK"};//原文出自【易百教程】，商业转载请联系作者获得授权，非商业请保留原文链接：https://www.yiibai.com/android/android_list_view.html


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        initActivity();

    }

    private void initActivity()
    {
        setContentView(R.layout.activity_type);
        TextView theTitle = findViewById(R.id.title_text);
        theTitle.setText(R.string.title_type);
        Button title_back = (Button) findViewById(R.id.title_back);
        Button title_search = (Button) findViewById(R.id.title_search);
        title_search.setVisibility(INVISIBLE);
        title_back.setOnClickListener(v -> {
            finish();
        });
        Intent intent =getIntent();
        types = intent.getStringArrayListExtra("types");
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.type_list, countryArray);

        ListView listView = (ListView) findViewById(R.id.type_list);
        listView.setAdapter(adapter);
    }
}
