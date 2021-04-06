package com.example.bright_storage.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bright_storage.R;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.model.query.StorageUnitQuery;
import com.example.bright_storage.repository.StorageUnitRepository;
import com.example.bright_storage.search.SearchActivity;
import com.example.bright_storage.tree.MyNodeViewFactory;
import com.example.bright_storage.ui.home.HomeAdapter;
import com.example.bright_storage.ui.home.HomeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Stack;

import me.texy.treeview.TreeNode;
import me.texy.treeview.TreeView;

import static android.view.View.INVISIBLE;

public class PathSelectActivity extends AppCompatActivity
{
    private HomeViewModel homeViewModel;
    private static RecyclerView mRecyclerView;
    private static StorageUnitQuery select = new StorageUnitQuery();
    private static Stack<Long> p_id = new Stack<>();
    private static List<StorageUnit> datas;
    private static HomeAdapter honmeAdapter;
    private Button title_back, title_search;
    private FloatingActionButton mCheck;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        初始化RecyclerView
        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_path);
        TextView thetitle = (TextView) findViewById(R.id.title_text);
        thetitle.setText("选择路径");
        mRecyclerView = (RecyclerView) this.findViewById(R.id.id_recyclerview);
        title_back = (Button) this.findViewById(R.id.title_back);
        title_search = (Button) this.findViewById(R.id.title_search);
        mCheck = (FloatingActionButton) this.findViewById(R.id.fab1);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!p_id.empty())
                    p_id.pop();
                else
                {
                    finish();
                }
                initData();
                honmeAdapter = new HomeAdapter(PathSelectActivity.this, datas);
                mRecyclerView.setAdapter(honmeAdapter); //can change like this  重新加载list中的数据到页面上
                SetOnClick();
            }
        });
        title_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PathSelectActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        mCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.putExtra("respond",getPid());
                setResult(Activity.RESULT_OK,intent);
            }
        });
//      RecyclerView设置展示的的样式（listView样子，gridView样子，瀑布流样子）
        mRecyclerView.setLayoutManager(new GridLayoutManager(PathSelectActivity.this, 3));


//      获取数据，向适配器传数据，绑定适配器
        initData();
        honmeAdapter = new HomeAdapter(PathSelectActivity.this, datas);
        mRecyclerView.setAdapter(honmeAdapter);
        //      初始化SwipeRefreshLayout
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.id_pull_flush);
        swipeRefreshLayout.setColorSchemeResources(R.color.auxiliary_color);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                honmeAdapter = new HomeAdapter(PathSelectActivity.this, datas);
                mRecyclerView.setAdapter(honmeAdapter); //can change like this  重新加载list中的数据到页面上
                SetOnClick();
                swipeRefreshLayout.setRefreshing(false);    //隐藏刷新图标
            }
        });
        SetOnClick();
    }
    protected void initData() {
        select.setParentId(getPid());
        select.setType(1);
        StorageUnitRepository StorageUnitRepo = new StorageUnitRepository();
        datas = StorageUnitRepo.query(select);
    }
    protected Long getPid(){
        if (!p_id.empty())
            return p_id.peek();
        return 0l;
    }
    protected void SetOnClick(){
        //      调用按钮返回事件回调的方法
        honmeAdapter.layoutSetOnclick(new HomeAdapter.layoutInterface() {

            @Override
            public void onclick(View view, StorageUnit TstorageUnit) {
                p_id.push(TstorageUnit.getLocalId());  //将pid设置为点击的storageunit的id
                if(TstorageUnit.getType()== 1) {
//                    swipeRefreshLayout.setRefreshing(true); //显示刷新图标
                    initData();   //根据pid初始化list
                    honmeAdapter = new HomeAdapter(PathSelectActivity.this, datas);
                    mRecyclerView.setAdapter(honmeAdapter);     //can change like this
//                    swipeRefreshLayout.setRefreshing(false);    //隐藏刷新图标
                    SetOnClick();
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("Id",p_id.peek());
                    intent.setClass(PathSelectActivity.this,ShowActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
//    private ViewGroup viewGroup;
//    private TreeNode this;
//    private TreeView treeView;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
//        setContentView(R.layout.activity_select_path);
//        initView();
//        this = TreeNode.this();
//        buildTree();
//        treeView = new TreeView(this, this, new MyNodeViewFactory(this));
//        View view = treeView.getView();
//        view.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        viewGroup.addView(view);
//    }
//
//    private void buildTree() {
//        //TODO构建树
//        for (int i = 0; i < 20; i++) {
//            TreeNode treeNode = new TreeNode("Parent  " + "No." + i);
//            treeNode.setLevel(0);
//            if(i != 3) { // avoids creating child nodes for "parent" 3 (which then is not a parent, so the semantic in the displayed text becomes incorrect)
//                for (int j = 0; j < 10; j++) {
//                    TreeNode treeNode1 = new TreeNode("Child " + "No." + j);
//                    treeNode1.setLevel(1);
//                    if(j != 5) {
//                        for (int k = 0; k < 5; k++) {
//                            TreeNode treeNode2 = new TreeNode("Grand Child " + "No." + k);
//                            treeNode2.setLevel(2);
//                            for(int l = 0; l < 3; l ++)
//                            {
//                                TreeNode treeNode3 = new TreeNode("Grand Grand Child " + "No." + l);
//                                treeNode3.setLevel(3);
//                                treeNode2.addChild(treeNode3);
//                            }
//                            treeNode1.addChild(treeNode2);
//                        }
//                    }
//                    treeNode.addChild(treeNode1);
//                }
//            }
//            this.addChild(treeNode);
//        }
//    }
//
//
//    private void initView() {
//        viewGroup = (ConstraintLayout) findViewById(R.id.container);
//        TextView thetitle = findViewById(R.id.title_text);
//        thetitle.setText(R.string.title_select_path);
//        Button title_back = (Button) findViewById(R.id.title_back);
//        Button title_search = (Button) findViewById(R.id.title_search);
//        title_search.setVisibility(INVISIBLE);
//        title_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }
}
