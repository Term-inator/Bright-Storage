package com.example.bright_storage.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bright_storage.R;
import com.example.bright_storage.search.SearchActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        Button title_search = (Button) root.findViewById(R.id.title_search);
//        Button title_back = (Button) root.findViewById(R.id.title_back);
        //        RelativeLayout layout = new RelativeLayout(this);


//      初始化SwipeRefreshLayout
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.id_pull_flush);
        swipeRefreshLayout.setColorSchemeResources(R.color.auxiliary_color);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });

//        swipeRefreshLayout.setColorSchemeResources(
//                R.color.colorPrimary,
//                R.color.green,
//                R.color.red
//        );
//        @Override
//        public void onRefresh() {
//            // 网络请求
//            okHttp.getHandler(handlerForGenJin);
//            // 这里用sortWay变量 这样即使下拉刷新也能保持用户希望的排序方式
//            askForOkHttp(sortWay);
//        }
//         通知结束下拉刷新
//        handlerForRefresh.sendEmptyMessage(0x93);
//        Handler handlerForRefresh = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case 0x93: {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }
//            }
//        };
//        swipeRefreshLayout.setOnRefreshListener(this);
//        初始化RecyclerView
        RecyclerView mRecyclerView = (RecyclerView) root.findViewById(R.id.id_recyclerview);
//      RecyclerView设置展示的的样式（listView样子，gridView样子，瀑布流样子）
//        listView纵向滑动样子
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 3));


//      获取数据，向适配器传数据，绑定适配器
        ArrayList<String> datas = initData();
        HomeAdapter honmeAdapter = new HomeAdapter(root.getContext(), datas);
        mRecyclerView.setAdapter(honmeAdapter);
//      调用按钮返回事件回调的方法
        honmeAdapter.layoutSetOnclick(new HomeAdapter.layoutInterface() {
            @Override
            public void onclick(View view, int position) {
                Toast.makeText(root.getContext(), "点击条目上的按钮" + position, Toast.LENGTH_SHORT).show();
            }
        });
        /*title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
//                Intent intent = new Intent(BSProActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });*/
//        title_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(root.getContext(), SearchActivity.class);
//                startActivity(intent);
//            }
//        });
        /*homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;

    }
    protected ArrayList<String> initData() {
        ArrayList<String> mDatas = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            mDatas.add("我是条目" + i);
        }
        return mDatas;
    }
}