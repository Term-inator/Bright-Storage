package com.example.bright_storage.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bright_storage.activity.ShowActivity;
import com.example.bright_storage.repository.AbstractRepository;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.model.query.StorageUnitQuery;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bright_storage.R;
import com.example.bright_storage.repository.StorageUnitRepository;
import com.example.bright_storage.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private static View root;
    private static RecyclerView mRecyclerView;
    private static StorageUnitQuery select = new StorageUnitQuery();
    private static Stack<Long> p_id = new Stack<>();
    private static Stack<String> title_name = new Stack<>();
    private static List<StorageUnit> datas;
    private static HomeAdapter honmeAdapter;
    private Button title_back, title_search;
    private TextView title_text;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
//        初始化RecyclerView
        mRecyclerView = (RecyclerView) root.findViewById(R.id.id_recyclerview);
        title_back = (Button) root.findViewById(R.id.title_back);
        title_search = (Button) root.findViewById(R.id.title_search);
        title_text = (TextView) root.findViewById(R.id.title_text);
        title_text.setText("智存");
        refresh();
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
//                Intent intent = new Intent(BSProActivity.this, MainActivity.class);
//                startActivity(intent);
                if (!p_id.empty()) {
                    p_id.pop();
                    if (p_id.empty())
                        initData(0l);
                    else
                        initData(p_id.peek());
                }
                if (!title_name.empty()){
                    title_name.pop();
                    if (title_name.empty())
                        title_text.setText("智存");
                    else
                        title_text.setText(title_name.peek());
                }else
                    title_text.setText("智存");
                honmeAdapter = new HomeAdapter(root.getContext(), datas);
                mRecyclerView.setAdapter(honmeAdapter); //can change like this  重新加载list中的数据到页面上
                SetOnClick();
            }
        });
        title_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
//      RecyclerView设置展示的的样式（listView样子，gridView样子，瀑布流样子）
//        listView纵向滑动样子
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 3));


//      获取数据，向适配器传数据，绑定适配器
//        initData();
//            honmeAdapter = new HomeAdapter(root.getContext(), datas);
//            mRecyclerView.setAdapter(honmeAdapter);
        //      初始化SwipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.id_pull_flush);
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
        return root;

    }
    protected static void initData() {
        select.setParentId(0l);
        StorageUnitRepository StorageUnitRepo = new StorageUnitRepository();
        datas = StorageUnitRepo.query(select);
    }
    protected static void initData(Long p_id) {
        select.setParentId(p_id);
        StorageUnitRepository StorageUnitRepo = new StorageUnitRepository();
        datas = StorageUnitRepo.query(select);
    }
    protected void updataData(Long p_id){
        initData(p_id);
    }
    protected void SetOnClick(){
        //      调用按钮返回事件回调的方法
        honmeAdapter.layoutSetOnclick(new HomeAdapter.layoutInterface() {

            @Override
            public void onclick(View view, StorageUnit TstorageUnit) {
//                Toast.makeText(root.getContext(), "点击条目上的按钮" + position, Toast.LENGTH_SHORT).show();
                if(TstorageUnit.getType()==1)
                {
                    p_id.push(TstorageUnit.getLocalId());  //将pid设置为点击的storageunit的id
                    title_name.push(TstorageUnit.getName()); //将titlename设置成点击的路径；
                    title_text.setText(title_name.peek());
                }
                if(TstorageUnit.getType()== 1) {
//                    System.out.println(TstorageUnit);
//                    swipeRefreshLayout.setRefreshing(true); //显示刷新图标
                    updataData(p_id.peek());   //根据pid初始化list
                    honmeAdapter = new HomeAdapter(root.getContext(), datas);
                    mRecyclerView.setAdapter(honmeAdapter);     //can change like this
//                    swipeRefreshLayout.setRefreshing(false);    //隐藏刷新图标
                    SetOnClick();
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("Id",TstorageUnit.getLocalId());
                    intent.setClass(root.getContext(),ShowActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    public static Long getPid(){
        if (!p_id.empty())
            return p_id.peek();
        return 0l;
    }
    public static void refresh(){
        if (!p_id.empty())
            initData(p_id.peek());
        else
            initData();
        honmeAdapter = new HomeAdapter(root.getContext(), datas);
        mRecyclerView.setAdapter(honmeAdapter);
    }
}