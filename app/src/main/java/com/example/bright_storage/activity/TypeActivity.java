package com.example.bright_storage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bright_storage.R;
import com.example.bright_storage.model.entity.Category;
import com.example.bright_storage.repository.StorageUnitRepository;
import com.example.bright_storage.search.SearchActivity;
import com.example.bright_storage.service.CategoryService;
import com.example.bright_storage.service.impl.CategoryServiceImpl;
import com.example.bright_storage.ui.home.HomeAdapter;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.INVISIBLE;

public class TypeActivity extends AppCompatActivity
{
    @Inject
    CategoryService categoryService;
    private ArrayList<String> types;
    int[] typesArray;
    String[] typeString;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        initActivity();
    }

    private void initActivity()
    {
        categoryService = new CategoryServiceImpl();
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
        typesArray = intent.getIntArrayExtra("types");
        typeString = new String[typesArray.length];
        for(int i = 0; i < typesArray.length; i ++) {
            if(categoryService.getById((long) typesArray[i]) != null)
                typeString[i] = categoryService.getById((long) typesArray[i]).getName();
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.type_list, typeString);

        listView = (ListView) findViewById(R.id.type_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageDialog.show(TypeActivity.this, "删除", "确定要删除吗？", "确定", "取消")
                        .setOkButton("确定", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                categoryService.deleteById((long)typesArray[position]);
                                //Toast.makeText(getActivity(), "点击了OK！", Toast.LENGTH_SHORT).show();
                                refresh();
                                return false;
                            }
                        });
            }
        });
        listView.setAdapter(adapter);
    }

    private void refresh() {
        List<Category> list = categoryService.listAll();
        typeString = new String[list.size()];
        int count =0;
        for(Category it : list) {
            typeString[count ++] = it.getName();
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.type_list, typeString);
        listView.setAdapter(adapter);
    }

}
