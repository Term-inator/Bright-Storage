package com.example.bright_storage.tree;

import android.app.Activity;
import android.view.View;

import me.texy.treeview.base.BaseNodeViewBinder;
import me.texy.treeview.base.BaseNodeViewFactory;

public class MyNodeViewFactory extends BaseNodeViewFactory {
    Activity mActivity = null;
    public MyNodeViewFactory(Activity activity)
    {
        mActivity = activity;
    }

    @Override
    public BaseNodeViewBinder getNodeViewBinder(View view, int level) {
        switch (level) {
            case 0:
                return new FirstLevelNodeViewBinder(view, mActivity);
            case 1:
                return new SecondLevelNodeViewBinder(view, mActivity);
            case 2:
                return new ThirdLevelNodeViewBinder(view, mActivity);
            case 3:
                return new FourthLevelNodeViewBinder(view, mActivity);
            default:
                return null;
        }
    }
}
