package com.example.bright_storage.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bright_storage.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by yetwish on 2015-05-11
 */

public class ViewHolder {

    private SparseArray<View> mViews;
    private Context mContext;
    private View mConvertView;
    private int mPosition;
    /**
     * init holder
     */
    public ViewHolder(Context context, int layoutId, ViewGroup parent, int position) {
        mConvertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        mViews = new SparseArray<>();
        mPosition = position;
        mConvertView.setTag(this);
    }

    /**
     *  获取viewHolder
     */
    public static ViewHolder getHolder(Context context, View convertView,
                                       int layoutId, ViewGroup parent, int position) {
        if(convertView == null){
            return new ViewHolder(context,layoutId,parent,position);
        }else{
            ViewHolder holder = (ViewHolder)convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    public View getConvertView(){
        return mConvertView;
    }

    /**
     * get view
     */
    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if(view == null){
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T)view;
    }

    /**
     * set text
     */
    public ViewHolder setText(int viewId, String text){
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     *  set image res
     */
    public ViewHolder setImageResource(int viewId, int resId){
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    /**
     *  set image bitmap
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap){
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }
    /**
     *  set image bitmap using Picasso
     */
    public ViewHolder setImage(int viewId, String imageuri){
        ImageView iv = getView(viewId);
        Picasso.get()
                .load(imageuri)//"https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3155097781,4164517483&fm=26&gp=0.jpg"
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(iv);
        return this;
    }
}
