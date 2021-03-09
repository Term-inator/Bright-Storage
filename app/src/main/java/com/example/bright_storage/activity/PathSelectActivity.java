package com.example.bright_storage.activity;

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

import com.example.bright_storage.R;
import com.example.bright_storage.tree.MyNodeViewFactory;

import me.texy.treeview.TreeNode;
import me.texy.treeview.TreeView;

import static android.view.View.INVISIBLE;

public class PathSelectActivity extends AppCompatActivity
{
    private ViewGroup viewGroup;
    private TreeNode root;
    private TreeView treeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_select_path);
        initView();
        root = TreeNode.root();
        buildTree();
        treeView = new TreeView(root, this, new MyNodeViewFactory(this));
        View view = treeView.getView();
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        viewGroup.addView(view);
    }

    private void buildTree() {
        //TODO构建树
        for (int i = 0; i < 20; i++) {
            TreeNode treeNode = new TreeNode("Parent  " + "No." + i);
            treeNode.setLevel(0);
            if(i != 3) { // avoids creating child nodes for "parent" 3 (which then is not a parent, so the semantic in the displayed text becomes incorrect)
                for (int j = 0; j < 10; j++) {
                    TreeNode treeNode1 = new TreeNode("Child " + "No." + j);
                    treeNode1.setLevel(1);
                    if(j != 5) {
                        for (int k = 0; k < 5; k++) {
                            TreeNode treeNode2 = new TreeNode("Grand Child " + "No." + k);
                            treeNode2.setLevel(2);
                            for(int l = 0; l < 3; l ++)
                            {
                                TreeNode treeNode3 = new TreeNode("Grand Grand Child " + "No." + l);
                                treeNode3.setLevel(3);
                                treeNode2.addChild(treeNode3);
                            }
                            treeNode1.addChild(treeNode2);
                        }
                    }
                    treeNode.addChild(treeNode1);
                }
            }
            root.addChild(treeNode);
        }
    }


    private void initView() {
        viewGroup = (ConstraintLayout) findViewById(R.id.container);
        TextView thetitle = findViewById(R.id.title_text);
        thetitle.setText(R.string.title_select_path);
        Button title_back = (Button) findViewById(R.id.title_back);
        Button title_search = (Button) findViewById(R.id.title_search);
        title_search.setVisibility(INVISIBLE);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
