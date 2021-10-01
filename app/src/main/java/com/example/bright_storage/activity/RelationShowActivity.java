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
import com.example.bright_storage.model.dto.StorageUnitDTO;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.model.query.StorageUnitQuery;
import com.example.bright_storage.repository.StorageUnitRepository;
import com.example.bright_storage.search.SearchActivity;
import com.example.bright_storage.service.StorageUnitService;
import com.example.bright_storage.service.impl.StorageUnitServiceImpl;
import com.example.bright_storage.ui.home.HomeAdapter;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.inject.Inject;

public class RelationShowActivity extends AppCompatActivity {
    @Inject
    StorageUnitService storageUnitService;

    private StorageUnitRepository storageUnitRepository;
    private static RecyclerView mRecyclerView;
    private static StorageUnitQuery select = new StorageUnitQuery();
    private static Stack<StorageUnitDTO> p_id = new Stack<>();
    private static Stack<String> title_name = new Stack<>();
    private static List<StorageUnitDTO> datas;
    private static List<StorageUnit> dataToDelete = new ArrayList<>();
    private static RelationShowAdapter RelationShowAdapter;
    private Button title_back, title_search;
    private TextView title_text;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Long relateid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_relation);
        //        初始化RecyclerView
        mRecyclerView = (RecyclerView)findViewById(R.id.id_recyclerview);
        title_back = (Button)findViewById(R.id.title_back);
        title_search = (Button)findViewById(R.id.title_search);
        title_text = (TextView)findViewById(R.id.title_text);
        title_text.setText("智存");
        refresh();
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!p_id.empty())
                    p_id.pop();
                if (!title_name.empty())
                    title_name.pop();
                setTitle();
                refresh();
                SetOnClick();
            }
        });
        title_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RelationShowActivity.this, SearchActivity.class);
//                startActivityForResult(intent, SELECT_PATH);
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
    protected void initData() {
        relateid = getIntent().getLongExtra("id",0l);
        StorageUnitService StorageUnitService = new StorageUnitServiceImpl();
        datas = new ArrayList<>();
        if (p_id.empty())
        {
            List<StorageUnitDTO> all = storageUnitService.listStorageUnitByRelationId(relateid);
            for(StorageUnitDTO it : all) {
                if(!it.getDeleted()) {
                    datas.add(it);
                }
            }
        }
        else
        {
            List<StorageUnitDTO> all = p_id.peek().getChildren();
            for(StorageUnitDTO it : all) {
                if(!it.getDeleted()) {
                    datas.add(it);
                }
            }
        }

    }

    protected void SetOnClick() {

        //      调用按钮返回事件回调的方法
        RelationShowAdapter.layoutSetOnclick(new RelationShowAdapter.layoutInterface() {
            @Override
            public void onclick(View view, StorageUnitDTO TstorageUnit) {
//                Toast.makeText(root.getContext(), "点击条目上的按钮" + position, Toast.LENGTH_SHORT).show();
                if (TstorageUnit.getType() == 1) {
                    p_id.push(TstorageUnit);  //将pid设置为点击的storageunit的id
                    title_name.push(TstorageUnit.getName()); //将titlename设置成点击的路径；
                    setTitle();
                }
                if (TstorageUnit.getType() == 1) {
                    refresh();
                    SetOnClick();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("Id", TstorageUnit.getId());
                    intent.setClass(RelationShowActivity.this, ShowActivity.class);
                    startActivity(intent);
                }
            }

//            @Override
//            public void onLongClick(View view, StorageUnitDTO TstorageUnit) {
//                /*FloatingActionButton fab = root.findViewById(R.id.fab);
//                RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
//                //HomeAdapter.MyViewHolder holder = (HomeAdapter.MyViewHolder) mRecyclerView.getChildViewHolder(view);
//                for (int i = 0; i < manager.getChildCount();i++) {
//                    View view = manager.getChildAt(i);
//                    HomeAdapter.MyViewHolder holder = (HomeAdapter.MyViewHolder) mRecyclerView.getChildViewHolder(view);
//                    holder.deleteCheckBox.setVisibility(View.VISIBLE);
//                }*/
//                MessageDialog.show((AppCompatActivity) RelationShowActivity.this, "删除", "确定要删除" + TstorageUnit.getName() + "吗？", "确定", "取消")
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

    private void delete(StorageUnit storageUnit , StorageUnitRepository storageUnitRepository) {
        StorageUnitQuery query = new StorageUnitQuery();
        query.setParentId(storageUnit.getLocalId());
        storageUnit.setDeleted(true);
        storageUnitRepository.update(storageUnit);
        // storageUnitRepository.delete(storageUnit);
        storageUnitService = new StorageUnitServiceImpl();
        List<StorageUnit> res = storageUnitService.query(query);
        if(res == null || res.size() == 0)
            return;
        for(StorageUnit it : res) {
            delete(it, storageUnitRepository);
        }
    }

//    public static StorageUnitDTO getPid() {
//        if (!p_id.empty())
//            return p_id.peek();
//        return 0l;
//    }

    protected void refresh() {
        initData();
        RelationShowAdapter = new RelationShowAdapter(this, datas);
        mRecyclerView.setAdapter(RelationShowAdapter);
    }

    protected void setTitle() {
        if (title_name.empty())
            title_text.setText("智存");
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
