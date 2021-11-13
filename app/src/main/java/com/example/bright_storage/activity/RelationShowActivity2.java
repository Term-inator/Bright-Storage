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
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.model.query.StorageUnitQuery;
import com.example.bright_storage.service.StorageUnitService;
import com.example.bright_storage.service.UserService;
import com.example.bright_storage.service.impl.StorageUnitServiceImpl;
import com.example.bright_storage.ui.home.HomeAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.inject.Inject;

public class RelationShowActivity2 extends AppCompatActivity {
    @Inject
    StorageUnitService storageUnitService;
    private static RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private static StorageUnitQuery select = new StorageUnitQuery();
    private static Stack<Long> p_id = new Stack<>();
    private static Stack<String> title_name = new Stack<>();
    private static List<StorageUnit> datas;
    private static List<StorageUnit> dataToDelete = new ArrayList<>();
    private static HomeAdapter HomeAdapter;
    private Button title_back, title_add;
    private TextView title_text;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Long relateid;
    private String name;
    public static final int SELECT_PATH = 1;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_relation_member);
        //        初始化RecyclerView
        mRecyclerView = (RecyclerView)findViewById(R.id.relation_member_rv);
        relateid = getIntent().getBundleExtra("relate").getLong("id");
        title_back = (Button)findViewById(R.id.title_back);
        title_add = (Button)findViewById(R.id.title_search);
        title_add.setBackgroundResource(R.drawable.gengduo);
        fab = (FloatingActionButton) this.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RelationShowActivity2.this, StorageUnitSelectActivity.class);
                intent.putExtra("relation_id", relateid);
                startActivityForResult(intent, SELECT_PATH);
            }
        });
        title_text = (TextView)findViewById(R.id.title_text);
        name = getIntent().getBundleExtra("relate").getString("name");
        title_text.setText(name);
        refresh();
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!p_id.empty())
                    p_id.pop();
                else
                    finish();
                if (!title_name.empty())
                    title_name.pop();
                setTitle();
                refresh();
                SetOnClick();
            }
        });
        title_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RelationShowActivity2.this, RelationMemberActivity.class);
                intent.putExtra("relation_id", relateid);
                startActivityForResult(intent, SELECT_PATH);
            }
        });
//      RecyclerView设置展示的的样式（listView样子，gridView样子，瀑布流样子）
//        listView纵向滑动样子
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));


//      获取数据，向适配器传数据，绑定适配器
        //      初始化SwipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_pull_flush);
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
    protected static void initData() {
        select.setLocalParentId(getPid());
        StorageUnitService StorageUnitService = new StorageUnitServiceImpl();
        datas = new ArrayList<>();
        List<StorageUnit> all = StorageUnitService.query(select);
        for(StorageUnit it : all) {
            if(!it.getDeleted()) {
                datas.add(it);
                break;
            }
        }
    }

    protected void SetOnClick() {

        //      调用按钮返回事件回调的方法
        HomeAdapter.layoutSetOnclick(new HomeAdapter.layoutInterface() {
            @Override
            public void onclick(View view, StorageUnit TstorageUnit) {
//                Toast.makeText(root.getContext(), "点击条目上的按钮" + position, Toast.LENGTH_SHORT).show();
                if (TstorageUnit.getType() == 1) {
                    p_id.push(TstorageUnit.getLocalId());  //将pid设置为点击的storageunit的id
                    title_name.push(TstorageUnit.getName()); //将titlename设置成点击的路径；
                    setTitle();
                }
                if (TstorageUnit.getType() == 1) {
                    refresh();
                    SetOnClick();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("Id", TstorageUnit.getLocalId());
                    intent.setClass(RelationShowActivity2.this, ShowActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, StorageUnit TstorageUnit) {
                /*FloatingActionButton fab = root.findViewById(R.id.fab);
                RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
                //HomeAdapter.MyViewHolder holder = (HomeAdapter.MyViewHolder) mRecyclerView.getChildViewHolder(view);
                for (int i = 0; i < manager.getChildCount();i++) {
                    View view = manager.getChildAt(i);
                    HomeAdapter.MyViewHolder holder = (HomeAdapter.MyViewHolder) mRecyclerView.getChildViewHolder(view);
                    holder.deleteCheckBox.setVisibility(View.VISIBLE);
                }*/
                MessageDialog.show((AppCompatActivity) RelationShowActivity2.this, "删除", "确定要删除" + TstorageUnit.getName() + "吗？", "确定", "取消")
                        .setOkButton("确定", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                delete(TstorageUnit);
                                refresh();
                                SetOnClick();
                                //Toast.makeText(getActivity(), "点击了OK！", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });
            }
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

//    public static StorageUnitDTO getPid() {
//        if (!p_id.empty())
//            return p_id.peek();
//        return 0l;
//    }

    protected void refresh() {
        initData();
        HomeAdapter = new HomeAdapter(this, datas);
        mRecyclerView.setAdapter(HomeAdapter);
    }

    private void delete(StorageUnit storageUnit) {
        StorageUnitQuery query = new StorageUnitQuery();
        query.setLocalParentId(storageUnit.getLocalId());
        storageUnit.setDeleted(true);
        // storageUnitRepository.delete(storageUnit);
        storageUnitService = new StorageUnitServiceImpl();
        storageUnitService.update(storageUnit);
        List<StorageUnit> res = storageUnitService.query(query);
        if(res == null || res.size() == 0)
            return;
        for(StorageUnit it : res) {
            delete(it);
        }
    }

    public static Long getPid() {
        if (!p_id.empty())
            return p_id.peek();
        return 0l;
    }

    protected void setTitle() {
        if (title_name.empty())
            title_text.setText(name);
        else
            title_text.setText(title_name.peek());
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
        SetOnClick();
        setTitle();
    }

}
