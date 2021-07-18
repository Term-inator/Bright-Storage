package com.example.bright_storage.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.bright_storage.R;
import com.example.bright_storage.model.entity.StorageUnit;

import java.util.List;

/**
 * changed on 21.7.15
 */

public class SearchAdapter extends CommonAdapter<StorageUnit>{

    public SearchAdapter(Context context, List<StorageUnit> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, int position) {
        holder.setImage(R.id.item_search_iv_icon,mData.get(position).getImage())
                .setText(R.id.item_search_tv_title,mData.get(position).getName())
                .setText(R.id.item_search_tv_content,mData.get(position).getNote());
//                .setText(R.id.item_search_tv_comments,mData.get(position).getComments());
    }
}
