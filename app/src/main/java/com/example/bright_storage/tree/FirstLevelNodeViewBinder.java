package com.example.bright_storage.tree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bright_storage.R;
import com.example.bright_storage.activity.PathSelectActivity;

import java.util.ArrayList;

import me.texy.treeview.TreeNode;
import me.texy.treeview.base.CheckableNodeViewBinder;

import static java.security.AccessController.getContext;

public class FirstLevelNodeViewBinder extends CheckableNodeViewBinder {
    private Activity mActivity;
    Button button;
    TextView textView;
    ImageView imageView;
    public FirstLevelNodeViewBinder(View itemView, Activity activity) {
        super(itemView);
        mActivity = activity;
        textView = itemView.findViewById(R.id.node_name_view);
        imageView = itemView.findViewById(R.id.arrow_img);
        button = itemView.findViewById(R.id.btn_path_submit);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_first_level;
    }

    @Override
    public int getCheckableViewId() {
        return R.id.checkBox;
    }

    @Override
    public void bindView(final TreeNode treeNode) {
        textView.setText(treeNode.getValue().toString());
        imageView.setRotation(treeNode.isExpanded() ? 90 : 0);
        imageView.setVisibility(treeNode.hasChild() ? View.VISIBLE : View.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> thePath = new ArrayList<String>();
                TreeNode temp = treeNode;
                while(!temp.isRoot())
                {
                    thePath.add(temp.getValue().toString());
                    temp = temp.getParent();
                }
                Intent intent = new Intent();
                intent.putExtra("pathName", thePath);
                mActivity.setResult(mActivity.RESULT_OK, intent);
                mActivity.finish();
            }
        });
    }

    @Override
    public void onNodeToggled(TreeNode treeNode, boolean expand) {
        if (expand) {
            imageView.animate().rotation(90).setDuration(200).start();
        } else {
            imageView.animate().rotation(0).setDuration(200).start();
        }
    }
}
