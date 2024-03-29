package com.example.bright_storage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bright_storage.R;
import com.example.bright_storage.model.dto.RelationDTO;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.model.param.LoginParam;
import com.example.bright_storage.model.query.StorageUnitQuery;
import com.example.bright_storage.model.support.BaseResponse;
import com.example.bright_storage.service.RelationService;
import com.example.bright_storage.service.SyncService;
import com.example.bright_storage.service.UserService;
import com.example.bright_storage.service.impl.RelationServiceImpl;
import com.example.bright_storage.service.impl.SyncServiceImpl;
import com.example.bright_storage.service.impl.UserServiceImpl;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RelationActivity extends AppCompatActivity
{
    private static View root;
    private static RecyclerView mRecyclerView;
    private static StorageUnitQuery select = new StorageUnitQuery();
    private static List<RelationDTO> datas;
    private static List<StorageUnit> dataToDelete = new ArrayList<>();
    private static RelationAdapter relationAdapter;
    private TextView title_text;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserService userService = new UserServiceImpl();
    private SyncService syncService = new SyncServiceImpl();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        login();
//        System.out.println(userService.getUserInfo().getNickname());
//        syncService.push();
//        System.out.println("pushed");
        getSupportActionBar().hide();
        setContentView(R.layout.activity_relation_member);
        mRecyclerView = (RecyclerView) this.findViewById(R.id.relation_member_rv);
        title_text = findViewById(R.id.title_text);
        title_text.setText("我的关系");
        Button title_back = (Button) findViewById(R.id.title_back);
        Button title_add = (Button) findViewById(R.id.title_search);
        FloatingActionButton fab = findViewById(R.id.fab2);
        fab.setVisibility(View.GONE);
        title_add.setBackgroundResource(R.drawable.ic_baseline_add_72dp);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent = new Intent(RelationActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });
        title_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RelationActivity.this,NewRelationActivity.class);
                startActivity(intent);
//                Intent intent = new Intent(RelationActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(RelationActivity.this, 3));
        refresh();


//      获取数据，向适配器传数据，绑定适配器
        //      初始化SwipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.id_pull_flush);
        swipeRefreshLayout.setColorSchemeResources(R.color.auxiliary_color);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                SetOnClick();
                swipeRefreshLayout.setRefreshing(false);    //隐藏刷新图标
            }
        });
        SetOnClick();
    }
    protected void initData() {
        RelationService RelationService = new RelationServiceImpl();
        datas = new ArrayList<>();
//        try {
//            login();
            datas = RelationService.listByCurrentUser();
            System.out.println(datas.get(0));
//        }catch (Exception e)
//        {
////            Toast.makeText(RelationActivity.this,"请登录后尝试",Toast.LENGTH_LONG).show();
//        }
        //        List<StorageUnit> all = StorageUnitService.query(select);
//        for(StorageUnit it : all) {
//            if(!it.getDeleted()) {
//                datas.add(it);
//            }
//        }
    }

    protected void login(){
        LoginParam loginParam = new LoginParam();
        loginParam.setPhone("15822222222");
        loginParam.setPassword("ab123456");
        BaseResponse<?> response = userService.loginPassword(loginParam);
    }

    protected void SetOnClick() {

        //      调用按钮返回事件回调的方法
        relationAdapter.layoutSetOnclick(new RelationAdapter.layoutInterface() {
            @Override
            public void onclick(View view, RelationDTO TstorageUnit) {
                    System.out.println(TstorageUnit.getId());
                    Intent intent = new Intent();
                    final Bundle bundle = new Bundle();
                    bundle.putLong("id", TstorageUnit.getId());
                    bundle.putString("name",TstorageUnit.getName());
                    intent.setClass(RelationActivity.this, RelationShowActivity2.class);
                    intent.putExtra("relate",bundle);
                    login();
                    startActivity(intent);
            }

//            @Override
//            public void onLongClick(View view, StorageUnit TstorageUnit) {
//                /*FloatingActionButton fab = root.findViewById(R.id.fab);
//                RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
//                //relationAdapter.MyViewHolder holder = (relationAdapter.MyViewHolder) mRecyclerView.getChildViewHolder(view);
//                for (int i = 0; i < manager.getChildCount();i++) {
//                    View view = manager.getChildAt(i);
//                    relationAdapter.MyViewHolder holder = (relationAdapter.MyViewHolder) mRecyclerView.getChildViewHolder(view);
//                    holder.deleteCheckBox.setVisibility(View.VISIBLE);
//                }*/
//                MessageDialog.show((AppCompatActivity) root.getContext(), "删除", "确定要删除" + TstorageUnit.getName() + "吗？", "确定", "取消")
//                        .setOkButton("确定", new OnDialogButtonClickListener() {
//                            @Override
//                            public boolean onClick(BaseDialog baseDialog, View v) {
//                                storageUnitRepository = new StorageUnitRepository();
//                                delete(TstorageUnit, storageUnitRepository);
//                                refresh();
//                                SetOnClick();
//                                //Toast.makeText(getActivity(), "点击了OK！", Toast.LENGTH_SHORT).show();
//                                return false;
//                            }
//                        });
//            }
        });
    }

//    private void delete(StorageUnit storageUnit , StorageUnitRepository storageUnitRepository) {
//        StorageUnitQuery query = new StorageUnitQuery();
//        query.setParentId(storageUnit.getLocalId());
//        storageUnit.setDeleted(true);
//        storageUnitRepository.update(storageUnit);
//        // storageUnitRepository.delete(storageUnit);
//        storageUnitService = new StorageUnitServiceImpl();
//        List<StorageUnit> res = storageUnitService.query(query);
//        if(res == null || res.size() == 0)
//            return;
//        for(StorageUnit it : res) {
//            delete(it, storageUnitRepository);
//        }
//    }
    

    protected void refresh() {
        initData();
        relationAdapter = new RelationAdapter(this, datas);
        mRecyclerView.setAdapter(relationAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
        SetOnClick();
        title_text.setText("我的关系");
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//        switch(requestCode) {
//            case SELECT_PATH:
//                if (data != null) {
//                    p_id.push(data.getExtras().getLong("pid"));
//                    title_name.push(data.getExtras().getString("name"));
//                    refresh();
//                    SetOnClick();
//                    setTitle();
//                }
//        }
//    }
}
