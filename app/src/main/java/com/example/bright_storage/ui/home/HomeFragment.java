package com.example.bright_storage.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bright_storage.activity.SettingActivity;
import com.example.bright_storage.activity.ShowActivity;
import com.example.bright_storage.model.entity.StorageUnit;
import com.example.bright_storage.model.query.StorageUnitQuery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bright_storage.R;
import com.example.bright_storage.repository.StorageUnitRepository;
import com.example.bright_storage.search.SearchActivity;
import com.example.bright_storage.service.StorageUnitService;
import com.example.bright_storage.service.impl.StorageUnitServiceImpl;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.MessageDialog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

import javax.inject.Inject;

public class HomeFragment extends Fragment {
    @Inject
    StorageUnitService storageUnitService;

    private StorageUnitRepository storageUnitRepository;
    private HomeViewModel homeViewModel;
    private static View root;
    private static RecyclerView mRecyclerView;
    private static StorageUnitQuery select = new StorageUnitQuery();
    private static Stack<Long> p_id = new Stack<>();
    private static Stack<String> title_name = new Stack<>();
    private static List<StorageUnit> datas;
    private static List<StorageUnit> dataToDelete = new ArrayList<>();
    private static HomeAdapter homeAdapter;
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
                Intent intent = new Intent(root.getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
//      RecyclerView设置展示的的样式（listView样子，gridView样子，瀑布流样子）
//        listView纵向滑动样子
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(root.getContext(), 3));


//      获取数据，向适配器传数据，绑定适配器
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
        select.setParentId(getPid());
        StorageUnitRepository StorageUnitRepo = new StorageUnitRepository();
        datas = StorageUnitRepo.query(select);
    }

    protected void SetOnClick() {

        //      调用按钮返回事件回调的方法
        homeAdapter.layoutSetOnclick(new HomeAdapter.layoutInterface() {
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
                    intent.setClass(root.getContext(), ShowActivity.class);
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
                MessageDialog.show((AppCompatActivity) root.getContext(), "删除", "确定要删除" + TstorageUnit.getName() + "吗？", "确定", "取消")
                        .setOkButton("确定", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                storageUnitRepository = new StorageUnitRepository();
                                delete(TstorageUnit, storageUnitRepository);
                                refresh();

                                SetOnClick();
                                //Toast.makeText(getActivity(), "点击了OK！", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });
            }
        });
    }

    private void delete(StorageUnit storageUnit , StorageUnitRepository storageUnitRepository) {
        StorageUnitQuery query = new StorageUnitQuery();
        query.setParentId(storageUnit.getLocalId());
        storageUnitRepository.delete(storageUnit);
        storageUnitService = new StorageUnitServiceImpl();
        List<StorageUnit> res = storageUnitService.query(query);
        if(res == null || res.size() == 0)
            return;
        for(StorageUnit it : res) {
            delete(it, storageUnitRepository);
        }
    }

    public static Long getPid() {
        if (!p_id.empty())
            return p_id.peek();
        return 0l;
    }

    protected static void refresh() {
        initData();
        homeAdapter = new HomeAdapter(root.getContext(), datas);
        mRecyclerView.setAdapter(homeAdapter);
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
    }
}