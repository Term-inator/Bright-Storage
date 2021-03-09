package com.example.bright_storage.tree;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bright_storage.R;

import java.util.ArrayList;

import me.texy.treeview.TreeNode;
import me.texy.treeview.base.CheckableNodeViewBinder;

public class FourthLevelNodeViewBinder extends CheckableNodeViewBinder {
    TextView textView;
    private Activity mActivity;
    Button button;
    public FourthLevelNodeViewBinder(View itemView, Activity activity) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.node_name_view);
        mActivity = activity;
        button = itemView.findViewById(R.id.btn_path_submit);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_fourth_level;
    }

    @Override
    public int getCheckableViewId() {
        return R.id.checkBox;
    }

    @Override
    public void bindView(TreeNode treeNode) {
        textView.setText(treeNode.getValue().toString());
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
}
